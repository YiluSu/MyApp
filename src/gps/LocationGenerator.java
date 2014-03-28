package gps;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

/**
 * A class produce current or custom Location.
 * Adapted from https://github.com/baoliangwang/CurrentLocation
 */
public class LocationGenerator{
	private LocationManager lm = null;
	private Location currentLocation=null;
	private LocationListener locationListener=null;
	/**
	 * Construct a Location_Generator with a LocationManager and create a new Location Listener.
	 * @param lm a LocationManager
	 */
	public LocationGenerator(LocationManager lm){
		this.lm=lm;
		this.locationListener=new LocationListener(){

			@Override
			public void onLocationChanged(Location location){
				if(location!=null){
					System.out.println("Location Changed");
				}
				else{
					System.out.println("Error: can not get the location.");
				}
			}

			@Override
			public void onProviderDisabled(String provider){}

			@Override
			public void onProviderEnabled(String provider){}

			@Override
			public void onStatusChanged(String provider,int status,Bundle extras){}
			
		};
		this.lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,1000,0,this.locationListener);
	}
    /**
     * 
     * @return the current Location, or raise Exception if GPS is not running.
     */
	public Location getCurrentLocation(){
		this.currentLocation=this.lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		if (lm.isProviderEnabled(LocationManager.GPS_PROVIDER) != true) {
			throw new RuntimeException("GPS isn't running!");
		}
		return this.currentLocation;
	}
	/**
	 * 
	 * @param latitude custom latitude in double.
	 * @param longitude custom longitude in double.
	 * @return the custom setted location by given latitude, and the given longitude.
	 */
	
	public Location getCustomLocation(double latitude,double longitude){
		Location l=new Location(this.currentLocation);
		l.setLatitude(latitude);
		l.setLongitude(longitude);
		return l;
	}
	
}
