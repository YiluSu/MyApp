package activity;

import user_name.UserNameHandler;

import com.example.projectapp.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;

public class LoginViewActivity extends Activity{
	
	private EditText userNameInput=null;
	private Button loginButton=null;
	
	private UserNameHandler userNameHandler=null;
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome_page);
		userNameInput=(EditText)findViewById(R.id.user_name_input);
		loginButton=(Button)findViewById(R.id.login_button);
		
		userNameHandler=new UserNameHandler();
		
		loginButton.setOnClickListener(new LoginClick());
	}

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
	
	private class LoginClick implements OnClickListener{

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