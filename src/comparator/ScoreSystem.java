package comparator;

import java.util.Comparator;
import java.util.Date;

import android.location.Location;

import model.Comment;

//This class is not done!
public class ScoreSystem implements Comparator<Comment>{
	
	private Location center=null;

	@Override
	public int compare(Comment a, Comment b){
		if(computeScore(a)<=computeScore(b)){
			return 1;
		}
		else{
			return -1;
		}
	}

	private Long computeDistance(Comment comment){
		Long dis=(long)comment.getLocation().distanceTo(this.center);
		return dis;
	}
	
	private Long computeScore(Comment comment){
		Long timeScore=((new Date()).getTime()-comment.getTimePosted())/1000;
		Long dis=computeDistance(comment);
		return dis+timeScore;
	}
	
	public void SetCenter(Location l){
		this.center=l;
	}
}
