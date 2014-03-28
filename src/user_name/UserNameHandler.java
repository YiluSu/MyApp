package user_name;

import android.app.Activity;
import android.content.SharedPreferences;


public class UserNameHandler{
	
	public static final String CACHES_KEY="cachesKey";
	public static final String USER_NAME_KEY="userNameKey";
	
	public UserNameHandler(){}
	
	public String getUserName(Activity activity){
		SharedPreferences caches=activity.getSharedPreferences(CACHES_KEY,0);
		String userName=caches.getString(USER_NAME_KEY,null);
		return userName;
	}
	
	public void setUserName(Activity activity,String userName){
		SharedPreferences caches=activity.getSharedPreferences(CACHES_KEY,0);
		caches.edit().putString(USER_NAME_KEY,userName).commit();
	}
	
	public void emptyUserName(Activity activity){
		SharedPreferences caches=activity.getSharedPreferences(CACHES_KEY,0);
		caches.edit().remove(USER_NAME_KEY).commit();
	}
}
