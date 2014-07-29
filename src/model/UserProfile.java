package model;

import android.graphics.Bitmap;

/**
 * A class which stores all the information of the user.
 * @author xuping
 */
public class UserProfile{
	
	private String userName=null;
	private String biography=null;
	private String twitter=null;
	private String facebook=null;
	private Bitmap photo=null;
	
	/**
	 * Construct a UserProfile object which contains the user information.
	 * @param userName : a String which is the user name.
	 * @param biography : a String which is the user biography.
	 * @param twitter : a String which is the user's twitter.
	 * @param facebook : a String which is the user's facebook.
	 * @param photo : a Bitmap which is the user's profile picture.
	 */
	public UserProfile(String userName,String biography,String twitter,String facebook,Bitmap photo){
		this.userName=userName;
		this.biography=biography;
		this.twitter=twitter;
		this.facebook=facebook;
		this.photo=photo;
	}

	
	public String getUserName(){
	
		return userName;
	}

	
	public void setUserName(String userName){
	
		this.userName = userName;
	}

	
	public String getBiography(){
	
		return biography;
	}

	
	public void setBiography(String biography){
	
		this.biography = biography;
	}

	
	public String getTwitter(){
	
		return twitter;
	}

	
	public void setTwitter(String twitter){
	
		this.twitter = twitter;
	}

	
	public String getFacebook(){
	
		return facebook;
	}

	
	public void setFacebook(String facebook){
	
		this.facebook = facebook;
	}

	
	public Bitmap getPhoto(){
	
		return photo;
	}

	
	public void setPhoto(Bitmap photo){
	
		this.photo = photo;
	}
}
