package activity;

import network_io.ConnectionChecker;
import network_io.ProfileIoHandler;

import com.example.projectapp.R;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * An Activity allows user to view Comment author's profile (if it exists).
 */
public class OtherProfilePageActivity extends Activity {
	
	private String userNameValue=null;
	
	private TextView userName=null;
	private TextView biography=null;
	private TextView twitter=null;
	private TextView facebook=null;
	private ImageView profilePicture=null;
	
	private ProfileIoHandler profileIoHandler=null;
	
	private ConnectionChecker connectionChecker=null;

	/**
	 * onCreate Method
	 * Set content view, then set Action bar and views used to display the profile content, then check if the network
	 * is online, if it is, load the profile and display it, otherwise, notify user it is off line.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_other_profile_page);
		
		// Prepare content of ActionBar
		final ActionBar actionBar = getActionBar();
		actionBar.setDisplayUseLogoEnabled(true);
		actionBar.setDisplayOptions(0, ActionBar.DISPLAY_SHOW_TITLE);
		
		userName = (TextView)findViewById(R.id.other_profile_user_name);
		biography = (TextView)findViewById(R.id.other_profile_biography);
		twitter = (TextView)findViewById(R.id.other_profile_twitter);
		facebook = (TextView)findViewById(R.id.other_profile_facebook);
		profilePicture = (ImageView)findViewById(R.id.other_profile_picture);
		
		profileIoHandler=new ProfileIoHandler();
		connectionChecker=new ConnectionChecker();
		
		Intent intent=getIntent();
		userNameValue=intent.getStringExtra("userName");
		
		if(connectionChecker.isNetworkOnline(this)){
			profileIoHandler.loadSpecificProfileForView(userNameValue,this,profilePicture,userName,biography,twitter,facebook);
		}
		else{
			Toast.makeText(getApplicationContext(),"Offline.",Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * Inflate the menu; this adds items to the action bar if it is present.
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.other_profile_page, menu);
		return true;
	}

}
