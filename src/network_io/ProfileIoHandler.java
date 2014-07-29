package network_io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.io.UnsupportedEncodingException;
//import java.lang.reflect.Type;

import model.UserProfile;

//import org.apache.http.HttpEntity;
//import org.apache.http.HttpResponse;
//import org.apache.http.client.ClientProtocolException;
//import org.apache.http.client.HttpClient;
//import org.apache.http.client.methods.HttpGet;
//import org.apache.http.client.methods.HttpPut;
//import org.apache.http.entity.StringEntity;
//import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.os.Environment;
//import android.util.Log;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
//import com.google.gson.reflect.TypeToken;

import customlized_gson.GsonConstructor;

/**
 * A network controller which controls the upload/download of UserProfile.
 * @author Yilu Su
 */
public class ProfileIoHandler{
	public static final String SERVER_URL="http://cmput301.softwareprocess.es:8080/cmput301w14t14/";
	public static final String LOG_TAG="Elastic Search";
	
	private Gson gson=(new GsonConstructor()).getGson();
	
	/**
	 * Construct a ProfileIoHandler object.
	 */
	public ProfileIoHandler(){}
	
	/**
	 * Update a UserProfile to the server, if the UserProfile with the same userName not exist, it will be add to the server.
	 * @param profile : a UserProfile which will be update/upload.
	 * @return a Thread which perform the upload operation.
	 */
	public Thread putOrUpdateProfile(final UserProfile profile){
		Thread thread=new Thread(){
			@Override
			public void run(){
				try {
					String fileName = Environment.getExternalStorageDirectory().getPath() + "/UserProfile-" + profile.getUserName().replaceAll("\\s","") + ".txt";
					FileWriter fileWriter = new FileWriter(fileName);
					BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
					bufferedWriter.write(gson.toJson(profile));
					bufferedWriter.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
				
//				HttpClient client=new DefaultHttpClient();
//				HttpPut request = new HttpPut(SERVER_URL+"UserProfile/"+profile.getUserName().replaceAll("\\s","")+"/");
//				try{
//					request.setEntity(new StringEntity(gson.toJson(profile)));
//				}
//				catch(UnsupportedEncodingException exception){
//					Log.w(LOG_TAG, "Error during Encoding: " + exception.getMessage());
//					return;
//				}
//				
//				HttpResponse response=null;
//				try{
//					response = client.execute(request);
//					Log.i(LOG_TAG, "ProfileUpdate: " + response.getStatusLine().toString());
//				}
//				catch(IOException exception){
//					Log.w(LOG_TAG, "Error during Update: " + exception.getMessage());
//				}
			}
		};
		thread.start();
		return thread;
	}
	
	/**
	 * Load and display the UserProfile in the UI and allow the user to edit.(if there are no such profile, then the only userNameInput EditText will be filled with the current user name);
	 * @param userName : a String which is the user name linked to the profile.
	 * @param activity : an Activity where the function will be called.
	 * @param photo : an ImageButton where will display the user profile image.
	 * @param userNameInput : an EditText will display the user name after calling this function.
	 * @param biographyInput : an EditText will display the biography(if the UserProfile is founded on the server) after calling this function.
	 * @param twitterInput : an EditText will display the twitter(if the UserProfile is founded on the server) after calling this function.
	 * @param facebookInput : an EditText will display the face book(if the UserProfile is founded on the server) after calling this function.
	 * @return a Thread which will perform the loading operation.
	 */
	public Thread loadSpecificProfileForUpdate(final String userName,final Activity activity,final ImageButton photo,
			final EditText userNameInput,final EditText biographyInput,final EditText twitterInput,final EditText facebookInput){
		
		Thread thread=new Thread(){
			@Override
			public void run(){
				String responseJson = "";
				try {
					FileReader fileReader = new FileReader(Environment.getExternalStorageDirectory().getPath() + "/UserProfile-" + userName.replaceAll("\\s","") + ".txt");
					BufferedReader bufferedReader = new BufferedReader(fileReader);
					String output = bufferedReader.readLine();
					while (output != null) {
						responseJson+= output;
						output = bufferedReader.readLine();
					}
					bufferedReader.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
				
//				HttpClient client=new DefaultHttpClient();
//				HttpGet request = new HttpGet(SERVER_URL+"UserProfile/"+userName.replaceAll("\\s","")+"/");
//				HttpResponse response=null;
//				String responseJson = "";
//				try{
//					response=client.execute(request);
//					Log.i(LOG_TAG, "ProfileLoad: " + response.getStatusLine().toString());
//					HttpEntity entity = response.getEntity();
//					BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent()));
//					String output = reader.readLine();
//					while (output != null) {
//						responseJson+= output;
//						output = reader.readLine();
//					}
//				} 
//				catch (ClientProtocolException e){
//					e.printStackTrace();
//				} 
//				catch (IOException e){
//					Log.w(LOG_TAG, "Error receiving query response: " + e.getMessage());
//					e.printStackTrace();
//				}
				
//				Type elasticSearchResponseType = new TypeToken<ElasticSearchResponse<UserProfile>>(){}.getType();
//				final ElasticSearchResponse<UserProfile> Data = gson.fromJson(responseJson,elasticSearchResponseType);
				
				final UserProfile loadedProfile = gson.fromJson(responseJson, UserProfile.class);
				
				Runnable getProfile = new Runnable() {
					@Override
					public void run() {
//						UserProfile loadedProfile=Data.getSource();
						userNameInput.setText(userName);
						if(loadedProfile!=null){
							photo.setImageBitmap(loadedProfile.getPhoto());
							biographyInput.setText(loadedProfile.getBiography());
							twitterInput.setText(loadedProfile.getTwitter());
							facebookInput.setText(loadedProfile.getFacebook());
						}
					}
				};
				activity.runOnUiThread(getProfile);
			}
		};
		thread.start();
		return thread;
	}
	
	/**
	 * Load and display the UserProfile in the UI and allow the user to view.(if there are no such profile, then the only userName TextView will be filled with the comment author name);
	 * @param userNameValue : a String which is the user name linked to the profile.
	 * @param activity : an Activity where the function will be called.
	 * @param photo : an ImageView where will display the user profile image.
	 * @param userName : an TextView will display the user name after calling this function.
	 * @param biography : an TextView will display the biography(if the UserProfile is founded on the server) after calling this function.
	 * @param twitter : an TextView will display the twitter(if the UserProfile is founded on the server) after calling this function.
	 * @param facebook : an TextView will display the face book(if the UserProfile is founded on the server) after calling this function.
	 * @return a Thread which will perform the loading operation.
	 */
	public Thread loadSpecificProfileForView(final String userNameValue,final Activity activity,final ImageView photo,
			final TextView userName,final TextView biography,final TextView twitter,final TextView facebook){
		
		Thread thread=new Thread(){
			@Override
			public void run(){
				String responseJson = "";
				try {
					FileReader fileReader = new FileReader(Environment.getExternalStorageDirectory().getPath() + "/UserProfile-" + userNameValue.replaceAll("\\s","") + ".txt");
					BufferedReader bufferedReader = new BufferedReader(fileReader);
					String output = bufferedReader.readLine();
					while (output != null) {
						responseJson+= output;
						output = bufferedReader.readLine();
					}
					bufferedReader.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
				
//				HttpClient client=new DefaultHttpClient();
//				HttpGet request = new HttpGet(SERVER_URL+"UserProfile/"+userNameValue.replaceAll("\\s","")+"/");
//				HttpResponse response=null;
//				String responseJson = "";
//				try{
//					response=client.execute(request);
//					Log.i(LOG_TAG, "ProfileLoad: " + response.getStatusLine().toString());
//					HttpEntity entity = response.getEntity();
//					BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent()));
//					String output = reader.readLine();
//					while (output != null) {
//						responseJson+= output;
//						output = reader.readLine();
//					}
//				} 
//				catch (ClientProtocolException e){
//					e.printStackTrace();
//				} 
//				catch (IOException e){
//					Log.w(LOG_TAG, "Error receiving query response: " + e.getMessage());
//					e.printStackTrace();
//				}
				
//				Type elasticSearchResponseType = new TypeToken<ElasticSearchResponse<UserProfile>>(){}.getType();
//				final ElasticSearchResponse<UserProfile> Data = gson.fromJson(responseJson,elasticSearchResponseType);
				
				final UserProfile loadedProfile = gson.fromJson(responseJson, UserProfile.class);
				
				Runnable getProfile = new Runnable() {
					@Override
					public void run() {
//						UserProfile loadedProfile=Data.getSource();
						userName.setText(userNameValue);
						if(loadedProfile!=null){
							photo.setImageBitmap(loadedProfile.getPhoto());
							biography.setText(loadedProfile.getBiography());
							twitter.setText(loadedProfile.getTwitter());
							facebook.setText(loadedProfile.getFacebook());
						}
					}
				};
				activity.runOnUiThread(getProfile);
			}
		};
		thread.start();
		return thread;
	}
}
