package comparator;

import java.util.Comparator;

import android.location.Location;

import model.Comment;

/**
 * A comparator class which compare 2 Comment's location with the given center location and sort them by distance.
 * @author Yilu Su
 */
public class LocationComparator implements Comparator<Comment>{
	
	private Location center=null;
	/**
	 * Construct the LocationComparator and set the center location.
	 * @param location : Location which will be used as center location.
	 */
	public LocationComparator(Location location){
		this.center=location;
	}

	/**
	 * @param a : left hand side Comment
	 * @param b : right hand side Comment
	 * @return 0 if both Comment object's location is null,
	 * -1 if left hand side Comment is closer to center or right hand side's Comment is null
	 * 1 if left hand side Comment is null or right hand side's Comment is closer to center
	 */
	@Override
	public int compare(Comment a, Comment b){
		if(a.getLocation()==null && b.getLocation()==null){
			return 0;
		}
		else if (a.getLocation()!=null && b.getLocation()==null){
			return -1;
		}
		else if(a.getLocation()==null && b.getLocation()!=null){
			return 1;
		}
		else if(center.distanceTo(a.getLocation())>=center.distanceTo(b.getLocation())){
			return 1;
		}
		else{
			return -1;
		}
	}
}
