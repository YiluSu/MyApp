package comparator;

import java.util.Comparator;

import model.Comment;

public class TimeComparator implements Comparator<Comment>{
    /**
     * A method which compares the difference between 2 comment's post time while sorting.
     * @param a a Comment object.
     * @param b a Comment object.
     */
	@Override
	public int compare(Comment a, Comment b) {
		if(a.getTimePosted()>=b.getTimePosted()){
			return 1;
		}
		else{
			return -1;
		}
	}
	
}
