package activity;

import gps.LocationGenerator;
import user_name.UserNameHandler;
import model.Comment;
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
	 * Direct user to camera in order to take the attached photo.
	 */
	public void takeAPhoto(){
		Intent camIntent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		startActivityForResult(camIntent,OBTAIN_PIC_REQUEST_CODE);
	}
	/**
	 * Set the image preview if the photo has been taken.
	 */
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == OBTAIN_PIC_REQUEST_CODE && resultCode == RESULT_OK) {
			attachedPic = (Bitmap)data.getExtras().get("data");
			picture.setImageBitmap(attachedPic);
		}
	}
	/**
	 * This click listener creates a new comment and commit it to the server,
	 * if this comment is a top level comment,add its id to the topLevelIdSet.
	 * Otherwise add its id to the comment it replies to.
	 */
	class CommitClick implements OnClickListener{
        @Override
		public void onClick(View v){
        	String commentTitle=title.getText().toString();
        	String commentContent=content.getText().toString();
        	Comment comment=new Comment(commentTitle,commentContent,currentLocation,attachedPic,userNameHandler.getUserName(CreateCommentPageActivity.this));
        	if(isTopLevel){
        		io.loadAndUpdateTopLevelIdSet(comment.getId(),CreateCommentPageActivity.this);
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
	
	class AttachClick implements OnClickListener{
        @Override
		public void onClick(View v){
        	takeAPhoto();
		}
	}
	
	
	class CancelClick implements OnClickListener{
        @Override
		public void onClick(View v){
        	finish();
		}
	}

	/**
	 *  Initialize View. Change the title of the ActionBar
	 */
	private void initView() {
		// ActionBar
		final ActionBar actionBar = getActionBar();
		actionBar.setDisplayUseLogoEnabled(true);
		actionBar.setDisplayOptions(0, ActionBar.DISPLAY_SHOW_TITLE);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu){
		getMenuInflater().inflate(R.menu.create_comment_page, menu);
		return true;
	}

}
