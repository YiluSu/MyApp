package activity;

import user.UserNameHandler;

import com.example.projectapp.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;

/**
 * An Activity provides user to login in to the Application.
 * @author Yilu Su
 */
public class LoginViewActivity extends Activity{
	
	private EditText userNameInput=null;
	private Button loginButton=null;
	
	private UserNameHandler userNameHandler=null;
	
	/**
	 *  onCreate method.
	 *  Once the activity is created, first set the content view,
	 *  Then, load views and set all the click listeners.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome_page);
		userNameInput=(EditText)findViewById(R.id.user_name_input);
		loginButton=(Button)findViewById(R.id.login_button);
		
		userNameHandler=new UserNameHandler();
		
		loginButton.setOnClickListener(new LoginClick());
	}

	/**
	 * onResume Method
	 * check if the user name is already exist in the shared preferences,
	 * if exist, automatically login with the existing user name(start the AllTopicPageActivity and finish the current Activity),
	 * Otherwise, notify user enter a user name to login.
	 */
	@Override
	protected void onResume(){
		super.onResume();
		if(userNameHandler.getUserName(this)!=null){
			Intent pushIntent=new Intent(LoginViewActivity.this,AllTopicPageActivity.class);
			startActivity(pushIntent);
			finish();
		}
		else{
			Toast.makeText(getApplicationContext(),"Please login.",Toast.LENGTH_SHORT).show();
		}
	}
	
	/**
	 * A click listener which will perform the login when user clicked on it.
	 */
	private class LoginClick implements OnClickListener{

		/**
		 * Get the user name input from the EditText and set the current user name,
		 * start the AllTopicPageActivity and finish the current Activity after click.
		 */
		@Override
		public void onClick(View v){
			String userName=userNameInput.getText().toString();
			userNameHandler.setUserName(LoginViewActivity.this,userName);
			Intent pushIntent=new Intent(LoginViewActivity.this,AllTopicPageActivity.class);
			startActivity(pushIntent);
			finish();
		}
	}
}