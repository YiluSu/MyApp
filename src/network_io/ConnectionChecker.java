//package network_io;
//
//import android.app.Activity;
//import android.content.Context;
//import android.net.ConnectivityManager;
//import android.net.NetworkInfo;
//
///**
// * A Class which major task is to check the Internet is connected or not.
// * @author xuping
// */
//public class ConnectionChecker{
//	
//	/**
//	 * Construct a ConnectionCheker object.
//	 */
//	public ConnectionChecker(){}
//	/**
//	 * Adapted from http://stackoverflow.com/questions/9570237/android-check-internet-connection
//	 * @param activity
//	 * @return true if Internet is connected, false otherwise.
//	 */
//	public boolean isNetworkOnline(Activity activity) {
//		ConnectivityManager cm = (ConnectivityManager)activity.getSystemService(Context.CONNECTIVITY_SERVICE);
//		NetworkInfo ni = cm.getActiveNetworkInfo();
//		if(ni==null){
//			return false;
//		}
//		return ni.isConnected();
//	}  
//}
