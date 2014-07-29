package network_io;

import java.util.Timer;
import java.util.TimerTask;

import activity.AllTopicPageActivity;
import activity.CommentPageActivity;

/**
 * A class which provides the tracking of Internet state and allowing the current Activity to repose the changing of Internet state.
 * @author xuping
 */
public class NetworkObserver{
	
	private ConnectionChecker cc=new ConnectionChecker();
	
	private Boolean observationStarted=null;
	
	/**
	 * Construct an NetworkObsever object.
	 */
	public NetworkObserver(){}
	
	/**
	 * Will be called when the Internet is not connected, start check 
	 * if the Internet connects each 5 seconds, if the Internet is connected, 
	 * then stop checking and call refresh method in the Activity.
	 * @param activity : AllTopicPageActivity where the function will be called.
	 */
	public void startObservation(final AllTopicPageActivity activity){
		Thread thread=new Thread(){
			@Override
			public void run(){
				final Timer timer=new Timer();
				timer.schedule(new TimerTask() {

		            @Override
		            public void run() {
		            	if(cc.isNetworkOnline(activity)){
		            		timer.cancel();
		            		timer.purge();
		            		Runnable action= new Runnable(){
		            			@Override
		            			public void run(){
		            				activity.refresh();
		            			}
		            		};
		            		activity.runOnUiThread(action);
		            	}

		            }
		        }, 5000, 5000);
			}
		};
		if(observationStarted==null || !observationStarted){
			thread.start();
			observationStarted=true;
		}
	}
	
	/**
	 * Will be called when the Internet is not connected, start check 
	 * if the Internet connects each 5 seconds, if the Internet is connected, 
	 * then stop checking and call refresh method in the Activity.
	 * @param activity : CommentPageActivity where the function will be called.
	 */
	public void startObservation(final CommentPageActivity activity){
		Thread thread=new Thread(){
			@Override
			public void run(){
				final Timer timer=new Timer();
				timer.schedule(new TimerTask() {

		            @Override
		            public void run() {
		            	if(cc.isNetworkOnline(activity)){
		            		timer.cancel();
		            		timer.purge();
		            		Runnable action= new Runnable(){
		            			@Override
		            			public void run(){
		            				activity.refresh();
		            			}
		            		};
		            		activity.runOnUiThread(action);
		            	}

		            }
		        }, 5000, 5000);
			}
		};
		if(observationStarted==null || !observationStarted){
			thread.start();
			observationStarted=true;
		}
	}
	
	/**
	 * Will be called when the Internet is connected, start check 
	 * if the Internet goes off each 5 seconds, if the Internet goes off, 
	 * then stop checking and call refresh method in the Activity.
	 * @param activity : AllTopicPageActivity where the function will be called.
	 */
	public void setObserver(final AllTopicPageActivity activity){
		Thread thread=new Thread(){
			@Override
			public void run(){
				final Timer timer=new Timer();
				timer.schedule(new TimerTask() {

		            @Override
		            public void run() {
		            	if(cc.isNetworkOnline(activity)==false){
		            		timer.cancel();
		            		timer.purge();
		            		Runnable action= new Runnable(){
		            			@Override
		            			public void run(){
		            				activity.refresh();
		            			}
		            		};
		            		activity.runOnUiThread(action);
		            	}

		            }
		        }, 5000, 5000);
			}
		};
		if(observationStarted==null || observationStarted){
			thread.start();
			observationStarted=false;
		}
	}
	
	/**
	 * Will be called when the Internet is connected, start check 
	 * if the Internet goes off each 5 seconds, if the Internet goes off, 
	 * then stop checking and call refresh method in the Activity.
	 * @param activity : CommentPageActivity where the function will be called.
	 */
	public void setObserver(final CommentPageActivity activity){
		Thread thread=new Thread(){
			@Override
			public void run(){
				final Timer timer=new Timer();
				timer.schedule(new TimerTask() {

		            @Override
		            public void run() {
		            	if(cc.isNetworkOnline(activity)==false){
		            		timer.cancel();
		            		timer.purge();
		            		Runnable action= new Runnable(){
		            			@Override
		            			public void run(){
		            				activity.refresh();
		            			}
		            		};
		            		activity.runOnUiThread(action);
		            	}

		            }
		        }, 5000, 5000);
			}
		};
		if(observationStarted==null || observationStarted){
			thread.start();
			observationStarted=false;
		}
	}
}
