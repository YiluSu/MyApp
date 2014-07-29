package user;

import android.app.Activity;
import android.content.SharedPreferences;

/**
 * A controller which controls the locally cached user name.
 * @author xuping
 */
public class UserNameHandler{
	
	public static final String CACHES_KEY="cachesKey";
	public static final String USER_NAME_KEY="userNameKey";
	
	/**
	 * Construct an UserNameHandler object.
	 */
	public UserNameHandler(){}
	
	/**
	 * @param activity : the Activity where the function will be called.
	 * @return the current user name.
	 */
	public String getUserName(Activity activity){
		SharedPreferences caches=activity.getSharedPreferences(CACHES_KEY,0);
		String userName=caches.getString(USER_NAME_KEY,null);
		return userName;
	}
	/**
	 * Set the current user name.
	 * @param activity : the Activity where the function will be called.
	 * @param userName : a String which is the user name will be set.
	 */
	public void setUserName(Activity activity,String userName){
		SharedPreferences caches=activity.getSharedPreferences(CACHES_KEY,0);
		caches.edit().putString(USER_NAME_KEY,userName).commit();
	}
	
	/**
	 * Empty the current user name.
	 * @param activity : the Activity where the function will be called.
	 */
	public void emptyUserName(Activity activity){
		SharedPreferences caches=activity.getSharedPreferences(CACHES_KEY,0);
		caches.edit().remove(USER_NAME_KEY).commit();
	}
}
