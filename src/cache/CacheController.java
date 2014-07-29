package cache;

import model.Comment;
import model.CommentList;

import android.app.Activity;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import customlized_gson.GsonConstructor;

/**
 * A controller which used to add Comment in to the shared preferences and load Comment from the sharedPreferences.
 * @author Yilu Su
 */
public class CacheController{
	
	public static final String CACHES_KEY="cachesKey";
	public static final String FAV_SUB_KEY="fav";
	public static final String INDICATED_KEY="indicated";
	private Gson gson=(new GsonConstructor()).getGson();
	
	/**
	 * Construct a CacheController controller.
	 */
	public CacheController(){}
	
	/**
	 * @param activity : an Activity where the function will be called.
	 * @param tag : a String indicates which cached CommenList will be returned. (favorites or indicated comment)
	 * @return a CommentList indicated by the tag.
	 */
	public CommentList getResource(Activity activity,String tag){
		SharedPreferences caches=activity.getSharedPreferences(CACHES_KEY,0);
		String Json=null;
		if(tag.equals("fav")){
			Json=caches.getString(FAV_SUB_KEY,null);
		}
		else if(tag.equals("indicated")){
			Json=caches.getString(INDICATED_KEY,null);
		}
		CommentList cl=null;
		if(Json==null){
			cl=new CommentList();
		}
		else{
			cl=gson.fromJson(Json,CommentList.class);
		}
		return cl;
	}
	
	/**
	 * Add a Comment to shared preferences as either favorite or indicated Comment(the key of shared preferences is indicated by tag).
	 * @param activity : the Activity where the function will be called.
	 * @param comment : a Comment object will be added to the shared preferences.
	 * @param tag : a String which indicates the comment is a favorite Comment or indicated Comment.
	 */
	public void addCacheAsTopLevel(Activity activity,Comment comment,String tag){
		SharedPreferences caches=activity.getSharedPreferences(CACHES_KEY,0);
		String key=null;
		if(tag.equals("fav")){
			key=FAV_SUB_KEY;
		}
		else if(tag.equals("indicated")){
			key=INDICATED_KEY;
		}
		String Json=caches.getString(key,null);
		CommentList cl=null;
		if(Json==null){
			cl=new CommentList();
		}
		else{
			cl=gson.fromJson(Json,CommentList.class);
		}
		cl.add(comment);
		String newJson=gson.toJson(cl);
		
		caches.edit().putString(key,newJson).commit();
	}
	
	/**
	 * Add a Comment to shared preferences as either favorite or indicated Comment's reply Comment.
	 * @param activity : the Activity where the function will be called.
	 * @param parentID : a String which is the parent Comment's id of this reply.
	 * @param reply : a Comment object.(reply)
	 */
	public void addCacheAsReply(Activity activity,String parentID,Comment reply){
		SharedPreferences caches=activity.getSharedPreferences(CACHES_KEY,0);
		String replyJson=caches.getString(parentID,null);
		CommentList cl=null;
		if(replyJson==null){
			cl=new CommentList();
		}
		else{
			cl=gson.fromJson(replyJson,CommentList.class);
		}
		cl.add(reply);
		String newJson=gson.toJson(cl);
		caches.edit().putString(parentID,newJson).commit();
	}
	
	/**
	 * Get a Cached Comment's replies as a CommentList.
	 * @param commentID : a String which is the parent Comment's id.
	 * @param activity : an Activity where the function will be called.
	 * @return a CommentList contains all of this comment's replies Comment.
	 */
	public CommentList getReply(String commentID,Activity activity){
		SharedPreferences caches=activity.getSharedPreferences(CACHES_KEY,0);
		String replyJson=caches.getString(commentID,null);
		CommentList cl=null;
		if(replyJson==null){
			cl=new CommentList();
		}
		else{
			cl=gson.fromJson(replyJson,CommentList.class);
		}
		return cl;
	}
}
