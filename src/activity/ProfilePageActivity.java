package activity;

import user.UserNameHandler;
import model.UserProfile;
//import network_io.ConnectionChecker;
import network_io.ProfileIoHandler;

import com.example.projectapp.R;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
//import android.widget.Toast;

/**
 * An Activity which allows user to reset user name, add/edit his profile to the server.
 * @author Yilu Su
 */
public class ProfilePageActivity extends Activity {
	
	private TextView profileTitle;
	private ImageButton photo;
	private EditText userNameInput;
	private EditText biographyInput;
	private EditText twitterInput;
	private EditText facebookInput;
	private ImageButton cancel;
	private ImageButton commit;
	
	private ProfileIoHandler profileIoHandler=null;
	private UserNameHandler userNameHandler=null;
	
//	private ConnectionChecker connectionChecker=null;
	
	public static final int OBTAIN_PIC_REQUEST_CODE=252;
	
	private Bitmap profilePhoto;
	
	/**
	 *  onCreate method.
	 *  Once the activity is created, first set the content view, and initialize ActionBar 
	 *  Then, load each content of the profile to the corresponding EditText for edit(if exist) and set all the click listeners.
	 *  if the network is off, then nothing will be loaded, and user will be notify.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile_page);
		
		// Prepare content of ActionBar
		final ActionBar actionBar = getActionBar();
		actionBar.setDisplayUseLogoEnabled(true);
		actionBar.setDisplayOptions(0, ActionBar.DISPLAY_SHOW_TITLE);
		
		profileTitle=(TextView)findViewById(R.id.profile_title);
		photo=(ImageButton)findViewById(R.id.profile_picture);
		userNameInput=(EditText)findViewById(R.id.user_name);
		biographyInput=(EditText)findViewById(R.id.biography);
		twitterInput=(EditText)findViewById(R.id.twitter);
		facebookInput=(EditText)findViewById(R.id.facebook);
		
		cancel=(ImageButton)findViewById(R.id.cancel_profile);
		commit=(ImageButton)findViewById(R.id.commit_profile);
		
		profileIoHandler=new ProfileIoHandler();
		userNameHandler=new UserNameHandler();
		
//		connectionChecker=new ConnectionChecker();
		
		profileTitle.setText("User Profile: ");
		
//		if(connectionChecker.isNetworkOnline(this)){
			profileIoHandler.loadSpecificProfileForUpdate(userNameHandler.getUserName(this),this, photo, userNameInput, biographyInput, twitterInput, facebookInput);
//		}
//		else{
//			Toast.makeText(getApplicationContext(),"Offline.",Toast.LENGTH_SHORT).show();
//		}
		
		photo.setOnClickListener(new AttachPhotoClick());
		cancel.setOnClickListener(new CancelClick());
		commit.setOnClickListener(new UpdateProfileClick());
	}
	
	/**
	 * Direct user to the camera and take a photo for profile picture.
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
			profilePhoto = (Bitmap)data.getExtras().get("data");
			photo.setImageBitmap(profilePhoto);
		}
	}
	
	/**
	 * A click listener which allows user to attach a photo to the profile after click.
	 */
	class AttachPhotoClick implements OnClickListener{

		/**
		 * After click call the takeAPhoto function.
		 */
		@Override
		public void onClick(View v){
			takeAPhoto();
		}
		
	}
	
	/**
	 * A click listener will update/add a user profile to the server after click and reset the user name locally
	 * if the network is online, other wise only the user name will be reset locally.
	 */
	class UpdateProfileClick implements OnClickListener{

		/**
		 * update/add a user profile to the server after click and reset the user name locally then finish the activity
	     * if the network is online, other wise only the user name will be reset locally, and user will be notify.
		 */
		@Override
		public void onClick(View v){
			UserProfile newProfile=new UserProfile(userNameInput.getText().toString(),biographyInput.getText().toString(),
					twitterInput.getText().toString(),facebookInput.getText().toString(),profilePhoto);
			userNameHandler.setUserName(ProfilePageActivity.this,userNameInput.getText().toString());
//			if(connectionChecker.isNetworkOnline(ProfilePageActivity.this)){
				profileIoHandler.putOrUpdateProfile(newProfile);
				finish();
//			}
//			else{
//				Toast.makeText(getApplicationContext(),"Offline, only user name has been reset locally.",Toast.LENGTH_SHORT).show();
//			}
		}
	}
	/**
	 * A click listener will finish the current Activity after click on the button.
	 */
	class CancelClick implements OnClickListener{

		/**
		 * finish the current activity
		 */
		@Override
		public void onClick(View v){
			finish();
		}
		
	}
	
}
