package comparator;

import java.util.Comparator;

import android.location.Location;

import model.Comment;

/**
 * A comparator class which compute 2 Comment object's score and sort them by score.
 * @author xuping
 */
public class ScoreSystem implements Comparator<Comment>{
	
	private Location center=null;
	/**
	 * Construct a ScoreSystem with a given center location.
	 * @param location : Location will be consider as center location.
	 */
	public ScoreSystem(Location location){
		this.center=location;
	}

	/**
	 * @param a : left hand side Comment
	 * @param b : right hand side Comment
	 * @return -1 if left hand side Comment's score is higher than the right hand side Comment's score
	 * 1 otherwise.
	 */
	@Override
	public int compare(Comment a, Comment b){
		if(getScore(a)>=getScore(b)){
			return -1;
		}
		else{
			return 1;
		}
	}
	/**
	 * Compute a Comment's score based on distance to center location and timePosted.
	 * @param comment : a Comment object.
	 * @return a Long integer which is the Comment's score.
	 */
	private long getScore(Comment comment){
		long timeScore=comment.getTimePosted()/1000;
		long locationScore=0;
		
		if(comment.getLocation()!=null){
			locationScore=-(long)comment.getLocation().distanceTo(center);
		}
		return timeScore+locationScore;
	}

}
