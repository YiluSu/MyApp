package network_io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.Date;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import model.Comment;
import model.CommentMap;
import model.IdSet;

import android.app.Activity;
import android.location.Location;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import cache.CacheController;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import customlized_gson.GsonConstructor;



/**
 * A network controller used to get and update comment from/to the server.
 * Adapted From https://github.com/zjullion/PicPosterComplete/blob/master/src/ca/ualberta/cs/picposter/network/ElasticSearchOperations.java
 * @author xuping
 */
public class IoStreamHandler {
	
	public static final String SERVER_URL="http://cmput301.softwareprocess.es:8080/cmput301w14t14/";
	public static final String LOG_TAG="Elastic Search";
	
	private Gson gson=(new GsonConstructor()).getGson();
	
	/**
	 * Construct a IoStreamHandler controller object.
	 */
	
	public IoStreamHandler(){}
	
	/**
	 * Update a Comment with its own id to the web server, if the Comment with the same id doesn't exist, then this Comment will be added to the server.
	 * @param comment : a single comment object.
	 * @return the thread which perform the upload operation.
	 */
	public Thread addOrUpdateComment(final Comment comment){
		Thread thread=new Thread(){
			@Override
			public void run(){
				HttpClient client=new DefaultHttpClient();
				HttpPut request = new HttpPut(SERVER_URL+"Comment/"+comment.getId()+"/");
				try{
					request.setEntity(new StringEntity(gson.toJson(comment)));
				}
				catch(UnsupportedEncodingException exception){
					Log.w(LOG_TAG, "Error during Encoding: " + exception.getMessage());
					return;
				}
				
				HttpResponse response=null;
				try{
					response = client.execute(request);
					Log.i(LOG_TAG, "CommentUpdate: " + response.getStatusLine().toString());
				}
				catch(IOException exception){
					Log.w(LOG_TAG, "Error during Update: " + exception.getMessage());
				}
			}
		};
		thread.start();
		return thread;
	}
	
	
	/**
	 * Put a Comment with the specific id into the CommentMap from the web server.
	 * @param commentID : a String which is the comment id.
	 * @param commentMap : a CommentMap to be update.
	 * @param activity : an Activity where the function will be called.
	 * @return the thread which perform the download operation.
	 */
	
	public Thread loadSpecificComment(final String commentID,final CommentMap commentMap,final Activity activity){
		Thread thread=new Thread(){
			@Override
			public void run(){
				HttpClient client=new DefaultHttpClient();
				HttpGet request = new HttpGet(SERVER_URL+"Comment/"+commentID+"/");
				HttpResponse response=null;
				String responseJson = "";
				try{
					response=client.execute(request);
					Log.i(LOG_TAG, "CommentLoad: " + response.getStatusLine().toString());
					HttpEntity entity = response.getEntity();
					BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent()));
					String output = reader.readLine();
					while (output != null) {
						responseJson+= output;
						output = reader.readLine();
					}
				} 
				catch (ClientProtocolException e){
					e.printStackTrace();
				} 
				catch (IOException e){
					Log.w(LOG_TAG, "Error receiving query response: " + e.getMessage());
					e.printStackTrace();
				}
				
				Type elasticSearchResponseType = new TypeToken<ElasticSearchResponse<Comment>>(){}.getType();
				final ElasticSearchResponse<Comment> Data = gson.fromJson(responseJson,elasticSearchResponseType);
				
				Runnable getComment = new Runnable() {
					@Override
					public void run() {
						Comment comment=Data.getSource();
						if(comment!=null){
							commentMap.addComment(comment);
						}
					}
				};
				activity.runOnUiThread(getComment);
			}
		};
		thread.start();
		return thread;
	}
	
	/**
	 * Update the top level comment's IdSet which stored in the server, if there's nothing in the server, then add it to the server.
	 * @param idSet : top level comment's IdSet.
	 * @return the thread which perform the upload operation.
	 */
	
	public Thread updateTopLevelIdSet(final IdSet idSet){
		Thread thread=new Thread(){
			@Override
			public void run(){
				HttpClient client=new DefaultHttpClient();
				HttpPut request = new HttpPut(SERVER_URL+"IdSet/topLevelId/");
				try {
					request.setEntity(new StringEntity(gson.toJson(idSet)));
				} 
				catch (UnsupportedEncodingException e) {
					Log.w(LOG_TAG, "Error during Encoding: " + e.getMessage());
					e.printStackTrace();
				}
				HttpResponse response=null;
				try {
					response = client.execute(request);
					Log.i(LOG_TAG, "SetUpdate: " + response.getStatusLine().toString());
				} 
				catch (ClientProtocolException e) {
					e.printStackTrace();
				} 
				catch (IOException e) {
					Log.w(LOG_TAG, "Error during Update: " + e.getMessage());
					e.printStackTrace();
				}
			}
		};
		thread.start();
		return thread;
	}
	
	/**
	 * Load all of the top level comments in to a CommentMap.
	 * @param commentMap : a CommentMap.
	 * @param activity : an Activity (AllTopicPageActivity) where the function will be called.
	 * @return the Thread which perform the download operation.
	 */
	public Thread loadTopLevelComments(final CommentMap commentMap,final Activity activity){
		Thread thread=new Thread(){
			@Override
			public void run(){
				HttpClient client=new DefaultHttpClient();
				HttpGet request = new HttpGet(SERVER_URL+"IdSet/topLevelId/");
				HttpResponse response=null;
				String responseJson = "";
				try {
					response=client.execute(request);
					Log.i(LOG_TAG, "SetLoad: " + response.getStatusLine().toString());
					HttpEntity entity = response.getEntity();
					BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent()));
					String output = reader.readLine();
					while (output != null) {
						responseJson+= output;
						output = reader.readLine();
					}
				} 
				catch (ClientProtocolException e) {
					e.printStackTrace();
				} 
				catch (IOException e) {
					Log.w(LOG_TAG, "Error receiving query response: " + e.getMessage());
					e.printStackTrace();
				}
				
				Type elasticSearchResponseType = new TypeToken<ElasticSearchResponse<IdSet>>(){}.getType();
				final ElasticSearchResponse<IdSet> Data = gson.fromJson(responseJson,elasticSearchResponseType);
				IdSet idSet=Data.getSource();
				if(idSet!=null){
					for(String id : idSet.getSet()){
						loadSpecificComment(id,commentMap,activity);
					}
				}
			}
		};
		thread.start();
		return thread;
	}
	
	/**
	 * Add a comment id to the top level IdSet and update the IdSet in the server while publish a new top level comment.
	 * @param commentID : a String which is the comment id to be add to the IdSet.
	 * @param activity : an Activity where the function will be called.
	 * @return the Thread which perform the update operation.
	 */
	
	public Thread loadAndUpdateTopLevelIdSet(final String commentID,final Activity activity){
		Thread thread=new Thread(){
			@Override
			public void run(){
				HttpClient client=new DefaultHttpClient();
				HttpGet request = new HttpGet(SERVER_URL+"IdSet/topLevelId/");
				HttpResponse response=null;
				String responseJson = "";
				try {
					response=client.execute(request);
					Log.i(LOG_TAG, "SetLoad: " + response.getStatusLine().toString());
					HttpEntity entity = response.getEntity();
					BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent()));
					String output = reader.readLine();
					while (output != null) {
						responseJson+=output;
						output = reader.readLine();
					}
				}
				catch (ClientProtocolException e) {
					e.printStackTrace();
				} 
				catch (IOException e){
					Log.w(LOG_TAG, "Error receiving query response: " + e.getMessage());
					e.printStackTrace();
				}
				
				Type elasticSearchResponseType = new TypeToken<ElasticSearchResponse<IdSet>>(){}.getType();
				final ElasticSearchResponse<IdSet> Data = gson.fromJson(responseJson,elasticSearchResponseType);
				Runnable updateIdSet = new Runnable() {
					@Override
					public void run() {
						IdSet idSet=Data.getSource();
						if(idSet==null){
							idSet=new IdSet();
						}
						idSet.add(commentID);
						updateTopLevelIdSet(idSet);
					}
				};
				activity.runOnUiThread(updateIdSet);
			}
		};
		thread.start();
		return thread;
	}
	/**
	 * Load and display a specific Comment's major content in CommentPageActivity (an Activity displays a specific Comment's content).
	 * @param commentID : a String which is the comment id of the Comment.
	 * @param title : a TextView which suppose display the title of the Comment after calling this function.
	 * @param content : a TextView which suppose display the content of the Comment after calling this function.
	 * @param commentInfo : a TextView which suppose display the posted time and location (if location is not null) of the Comment after calling this function.
	 * @param picture : an ImageView which suppose display the picture of the Comment (if not null) after calling this function.
	 * @param commentMap : a CommentMap which will contain all of the replies of this Comment after calling this function.
	 * @param activity : an Activity (CommentPageActivity) where the function will be called.
	 * @return a Thread which will perform the loading operation.
	 */
	public Thread loadAndSetSpecificComment(final String commentID,final TextView title,final TextView content,final TextView commentInfo,final ImageView picture,final CommentMap commentMap,final Activity activity){
		Thread thread=new Thread(){
			@Override
			public void run(){
				HttpClient client=new DefaultHttpClient();
				HttpGet request = new HttpGet(SERVER_URL+"Comment/"+commentID+"/");
				HttpResponse response=null;
				String responseJson = "";
				try{
					response=client.execute(request);
					Log.i(LOG_TAG, "CommentLoadAndSet: " + response.getStatusLine().toString());
					HttpEntity entity = response.getEntity();
					BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent()));
					String output = reader.readLine();
					while (output != null) {
						responseJson+= output;
						output = reader.readLine();
					}
				} 
				catch (ClientProtocolException e){
					e.printStackTrace();
				} 
				catch (IOException e){
					Log.w(LOG_TAG, "Error receiving query response: " + e.getMessage());
					e.printStackTrace();
				}
				
				Type elasticSearchResponseType = new TypeToken<ElasticSearchResponse<Comment>>(){}.getType();
				final ElasticSearchResponse<Comment> Data = gson.fromJson(responseJson,elasticSearchResponseType);
				Runnable getAndSetComment = new Runnable() {
					@Override
					public void run() {
						Comment comment=Data.getSource();
						if(comment!=null){
							title.setText(comment.getTitle());
							content.setText(comment.getText());
							picture.setImageBitmap(comment.getPicture());
							Location location=comment.getLocation();
							if(location!=null){
								double lat=location.getLatitude();
								double lng=location.getLongitude();
								String lngS=String.valueOf(lng);
								String latS=String.valueOf(lat);
								commentInfo.setText("Posted By : "+comment.getUserName()+"\nAt : "+((new Date(comment.getTimePosted())).toString())+"\nLongitude: "+lngS+"\nLatitude: "+latS);
							}
							else{
								commentInfo.setText("Posted By : "+comment.getUserName()+"\nAt : "+((new Date(comment.getTimePosted())).toString()));
							}
							for(String commentIDs : comment.getReplies()){
								loadSpecificComment(commentIDs,commentMap,activity);
							}
						}
					}
				};
				activity.runOnUiThread(getAndSetComment);
			}
		};
		thread.start();
		return thread;
	}
	
	/**
	 * Load a specific Comment by it's id and add an reply comment's id to this comment's reply IdSet, then update the Comment to the server.
	 * @param parentID : a String which is the comment id of the comment has been replied.
	 * @param replyID :  a String which is the comment id of the reply comment.
	 * @return a Thread which will perform the update operation.
	 */
	public Thread replySpecificComment(final String parentID,final String replyID){
		Thread thread=new Thread(){
			@Override
			public void run(){
				HttpClient client=new DefaultHttpClient();
				HttpGet request = new HttpGet(SERVER_URL+"Comment/"+parentID+"/");
				HttpResponse response=null;
				String responseJson = "";
				try{
					response=client.execute(request);
					Log.i(LOG_TAG, "CommentLoad: " + response.getStatusLine().toString());
					HttpEntity entity = response.getEntity();
					BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent()));
					String output = reader.readLine();
					while (output != null) {
						responseJson+= output;
						output = reader.readLine();
					}
				} 
				catch (ClientProtocolException e){
					e.printStackTrace();
				} 
				catch (IOException e){
					Log.w(LOG_TAG, "Error receiving query response: " + e.getMessage());
					e.printStackTrace();
				}
				
				Type elasticSearchResponseType = new TypeToken<ElasticSearchResponse<Comment>>(){}.getType();
				final ElasticSearchResponse<Comment> Data = gson.fromJson(responseJson,elasticSearchResponseType);
				Comment parent=Data.getSource();
				parent.addReply(replyID);
				addOrUpdateComment(parent);
			}
		};
		thread.start();
		return thread;
	}
	/**
	 * Load and display a specific Comment's major content in EditCommentPageActivity (an Activity displays a specific Comment's content and allows user to edit).
	 * @param commentID : a String which is the Comment's id.
	 * @param title : an EditText which will contain the Comment's title after calling this function.
	 * @param content : an EditText which will contain the Comment's content after calling this function.
	 * @param latitude : an EditText which will contain the Comment's location's latitude after calling this function if the location is not null, otherwise it contains nothing after calling this function.
	 * @param longitude : an EditText which will contain the Comment's location's longitude after calling this function if the location is not null, otherwise it contains nothing after calling this function.
	 * @param picture : an ImageView which will contain the image of the Comment(if not null).
	 * @param activity : an Activity where the function will be called.
	 * @return the thread which perform the loading operation.
	 */
	public Thread setupEditPage(final String commentID,final EditText title,final EditText content,final EditText latitude,final EditText longitude,final ImageView picture,final Activity activity){
		Thread thread=new Thread(){
			@Override
			public void run(){
				HttpClient client=new DefaultHttpClient();
				HttpGet request = new HttpGet(SERVER_URL+"Comment/"+commentID+"/");
				HttpResponse response=null;
				String responseJson = "";
				try{
					response=client.execute(request);
					Log.i(LOG_TAG, "CommentLoad: " + response.getStatusLine().toString());
					HttpEntity entity = response.getEntity();
					BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent()));
					String output = reader.readLine();
					while (output != null) {
						responseJson+= output;
						output = reader.readLine();
					}
				} 
				catch (ClientProtocolException e){
					e.printStackTrace();
				} 
				catch (IOException e){
					Log.w(LOG_TAG, "Error receiving query response: " + e.getMessage());
					e.printStackTrace();
				}
				
				Type elasticSearchResponseType = new TypeToken<ElasticSearchResponse<Comment>>(){}.getType();
				final ElasticSearchResponse<Comment> Data = gson.fromJson(responseJson,elasticSearchResponseType);
				Runnable getAndSetEditComment = new Runnable(){
					@Override
					public void run(){
						Comment comment=Data.getSource();
						title.setText(comment.getTitle());
						content.setText(comment.getText());
						Location loc=comment.getLocation();
						if(loc!=null){
							latitude.setText(String.valueOf(loc.getLatitude()));
							longitude.setText(String.valueOf(loc.getLongitude()));
						}
						picture.setImageBitmap(comment.getPicture());
					}
					
				};
				activity.runOnUiThread(getAndSetEditComment);
			}
		};
		thread.start();
		return thread;
	}
	

	/**
	 * Update a edited Comment in to the server.
	 * @param commentID : a String which is comment id.
	 * @param editedTitle : a String which is the title of the Comment after edit.
	 * @param editedText : a String which is the content of the Comment after edit.
	 * @return a thread which perform the uploading operation.
	 */
	public Thread commitEdit(final String commentID,final String editedTitle,final String editedText,final Location editedLocation){
		Thread thread=new Thread(){
			@Override
			public void run(){
				HttpClient client=new DefaultHttpClient();
				HttpGet request = new HttpGet(SERVER_URL+"Comment/"+commentID+"/");
				HttpResponse response=null;
				String responseJson = "";
				try{
					response=client.execute(request);
					Log.i(LOG_TAG, "CommentLoad: " + response.getStatusLine().toString());
					HttpEntity entity = response.getEntity();
					BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent()));
					String output = reader.readLine();
					while (output != null) {
						responseJson+= output;
						output = reader.readLine();
					}
				} 
				catch (ClientProtocolException e){
					e.printStackTrace();
				} 
				catch (IOException e){
					Log.w(LOG_TAG, "Error receiving query response: " + e.getMessage());
					e.printStackTrace();
				}
				
				Type elasticSearchResponseType = new TypeToken<ElasticSearchResponse<Comment>>(){}.getType();
				final ElasticSearchResponse<Comment> Data = gson.fromJson(responseJson,elasticSearchResponseType);
				Comment comment=Data.getSource();
				comment.setTitle(editedTitle);
				comment.setText(editedText);
				comment.setLocation(editedLocation);
				addOrUpdateComment(comment);
			}
		};
		thread.start();
		return thread;
	}
	
	/**
	 * Load a Comment and all it's replies for cache ,store it in the shared preferences depends on tag.
	 * @param commentID : a String which is the comment id.
	 * @param parentID : a String which is the parent Comment's id.
	 * @param cc : a CacheController object.
	 * @param tag : a String which indicates how the comment will be stored
	 * @param activity : an Activity where the function will be called.
	 * @return a Thread which perform the loading operation.
	 */
	public Thread addCache(final String commentID,final String parentID,final CacheController cc,final String tag,final Activity activity){
		Thread thread=new Thread(){
			@Override
			public void run(){
				HttpClient client=new DefaultHttpClient();
				HttpGet request = new HttpGet(SERVER_URL+"Comment/"+commentID+"/");
				HttpResponse response=null;
				String responseJson = "";
				try{
					response=client.execute(request);
					Log.i(LOG_TAG, "CommentLoad: " + response.getStatusLine().toString());
					HttpEntity entity = response.getEntity();
					BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent()));
					String output = reader.readLine();
					while (output != null) {
						responseJson+=output;
						output = reader.readLine();
					}
				} 
				catch (ClientProtocolException e){
					e.printStackTrace();
				} 
				catch (IOException e){
					Log.w(LOG_TAG, "Error receiving query response: " + e.getMessage());
					e.printStackTrace();
				}
				
				Type elasticSearchResponseType = new TypeToken<ElasticSearchResponse<Comment>>(){}.getType();
				final ElasticSearchResponse<Comment> Data = gson.fromJson(responseJson,elasticSearchResponseType);
				
				Runnable putCacheReply = new Runnable() {
					@Override
					public void run() {
						Comment comment=Data.getSource();
						if(!tag.equals("reply")){
							cc.addCacheAsTopLevel(activity,comment,tag);
						}
						else{
							cc.addCacheAsReply(activity,parentID,comment);
						}
						for(String replyID : comment.getReplies()){
							addCache(replyID,comment.getId(),cc,"reply",activity);
						}
					}
				};
				activity.runOnUiThread(putCacheReply);
			}
		};
		thread.start();
		return thread;
	}
	
	/**
	 * Trash all stuff on the server.
	 */
	public void clean(){
		Thread thread=new Thread(){
			@Override
			public void run(){
				HttpClient client=new DefaultHttpClient();
				HttpDelete request = new HttpDelete(SERVER_URL);
				HttpResponse response=null;
				try{
					response = client.execute(request);
					Log.i(LOG_TAG, "Clean: " + response.getStatusLine().toString());
				}
				catch(IOException exception){
					Log.w(LOG_TAG, "Error during Clean: " + exception.getMessage());
				}
			}
		};
		thread.start();
	}
}
