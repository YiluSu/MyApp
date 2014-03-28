package comparator;

import java.util.Comparator;

import android.location.Location;

import model.Comment;


public class LocationComparator implements Comparator<Comment>{
	
	private Location center=null;

	@Override
	public int compare(Comment a, Comment b){
		if(a.getLocation().distanceTo(this.center)<=b.getLocation().distanceTo(this.center)){
			return 1;
		}
		else{
			return 0;
		}
	}
	
	public void SetCenter(Location l){
		this.center=l;
	}
}
