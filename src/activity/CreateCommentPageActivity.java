package activity;

import gps.LocationGenerator;
import user.UserNameHandler;
import model.Comment;
import network_io.ConnectionChecker;
import network_io.IoStreamHandler;

import com.example.projectapp.R;

import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * An Activity which provide user option to create/reply a Comment with a location if GPS is enabled,
 * attach a picture to the Comment or cancel the publish.
 * @author Xuping Fang,Yilu Su
 */
public class CreateCommentPageActivity extends Activity {
	
	public static final int OBTAIN_PIC_REQUEST_CODE=252;
	
	private EditText title=null;
	private EditText content=null;
	private TextView location=null;
	private ImageView picture=null;
	private ImageButton pictureButton=null;
	private ImageButton commit=null;
	private ImageButton cancel=null;
	
	private IoStreamHandler io=null;
	private UserNameHandler userNameHandler=null;
	private Boolean isTopLevel=null;
	private LocationGenerator locationGenerator=null;
	private Location currentLocation=null;
	
	private Bitmap attachedPic=null;

	
	/**
	 *  onCreate method.
	 *  Once the activity is created, first set the content view, and initialize ActionBar. 
	 *  Then, load the view objects which allows user to publish or reply to a Comment, at last,set the click
	 *  listeners.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_comment_page);
		
		// Initialize View
		initView();
		
		title = (EditText)findViewById(R.id.create_title);
		content = (EditText)findViewById(R.id.create_content);
		
		location=(TextView)findViewById(R.id.location);
		pictureButton = (ImageButton)findViewById(R.id.create_image);
		picture = (ImageView)findViewById(R.id.create_image_review);
		commit = (ImageButton)findViewById(R.id.create_commit);
		cancel = (ImageButton)findViewById(R.id.create_cancel);
		
		io=new IoStreamHandler();
		userNameHandler=new UserNameHandler();
		locationGenerator=new LocationGenerator((LocationManager)getSystemService(Context.LOCATION_SERVICE));
		
		Intent intent=getIntent();
		isTopLevel=intent.getBooleanExtra("isTopLevel",false);
		//Location:
		currentLocation=locationGenerator.getCurrentLocation();
		if(currentLocation!=null){
			location.setText("Latitude: "+String.valueOf(currentLocation.getLatitude())+"\nLongitude: "+String.valueOf(currentLocation.getLongitude()));
		}
		else{
			location.setText("GPS is not running!");
		}
		//SetClickListener
		cancel.setOnClickListener(new CancelClick());
		pictureButton.setOnClickListener(new AttachClick());
		commit.setOnClickListener(new CommitClick());
	}
	
	/**
	 * Direct user to the camera in order to take the attached photo.
	 */
	public void takeAPhoto(){
		Intent camIntent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		startActivityForResult(camIntent,OBTAIN_PIC_REQUEST_CODE);
	}
	/**
	 * Set the image preview if the photo has been taken and put the image Bitmap in to an attribute.
	 * @param requestCode : a request code which allows user to take a photo.
	 * @param data : an Intent object which contains the photo if the photo has been taken.
	 * @param resultCode: a resultCode which shows the status of the photo.
	 */
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == OBTAIN_PIC_REQUEST_CODE && resultCode == RESULT_OK) {
			attachedPic = (Bitmap)data.getExtras().get("data");
			picture.setImageBitmap(attachedPic);
		}
	}
	/**
	 * This click listener creates a new comment and commit it to the server after click,
	 * if this comment is a top level comment,add its id to the topLevelIdSet.
	 * Otherwise add its id to the Comment's reply set which it replies to.
	 */
	class CommitClick implements OnClickListener{
		/**
		 * After user click the button, first check the network state, if the network is off line
		 * notify user that he cannot publish a Comment at this time. Otherwise, get the user input from different views 
		 * to create a new Comment object, then check if this new Comment
		 * should be a reply or a top level comment and perform operation due to different ,at last add the new Comment
		 * to the server, and finish the Activity.
		 */
        @Override
		public void onClick(View v){
        	ConnectionChecker connectionChecker=new ConnectionChecker();
			if(connectionChecker.isNetworkOnline(CreateCommentPageActivity.this)==false){
				Toast.makeText(getApplicationContext(),"Offline.",Toast.LENGTH_SHORT).show();
				return;
			}
        	String commentTitle=title.getText().toString();
        	String commentContent=content.getText().toString();
        	Comment comment=new Comment(commentTitle,commentContent,currentLocation,attachedPic,userNameHandler.getUserName(CreateCommentPageActivity.this));
        	if(isTopLevel){
        		io.loadAndUpdateTopLevelIdSet(comment.getId(),CreateCommentPageActivity.this);
        	}
        	else{
        		String parentID=CreateCommentPageActivity.this.getIntent().getStringExtra("parentID");
        		io.replySpecificComment(parentID,comment.getId());
        	}
        	io.addOrUpdateComment(comment);
        	try{
				Thread.sleep(500);
			} 
        	catch (InterruptedException e){
				e.printStackTrace();
			}
        	finish();
        }
	}
	/**
	 * A click listener which will direct user to take a photo for Comment's attached picture after click.
	 */
	class AttachClick implements OnClickListener{
		/**
		 * Call takeAPhoto function after click.
		 */
        @Override
		public void onClick(View v){
        	takeAPhoto();
		}
	}
	
	/**
	 * A click listener which will finish the current Activity after click.
	 */
	class CancelClick implements OnClickListener{
		/**
		 * Finish the current Activity.
		 */
        @Override
		public void onClick(View v){
        	finish();
		}
	}

	/**
	 *  Initialize View. Change the title of the ActionBar
	 */
	private void initView() {
		final ActionBar actionBar = getActionBar();
		actionBar.setDisplayUseLogoEnabled(true);
		actionBar.setDisplayOptions(0,ActionBar.DISPLAY_SHOW_TITLE);
	}
	
	/**
	 * Inflate the menu; this adds items to the action bar if it is present.
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu){
		getMenuInflater().inflate(R.menu.create_comment_page, menu);
		return true;
	}

}
