package comparator;

import java.util.Comparator;

import model.Comment;

/**
 * A Comparator which compare and sort 2 Comment objects by time posted.
 * @author xuping
 */
public class TimeComparator implements Comparator<Comment>{
    /**
     * A method which compares the difference between 2 comment's post time while sorting.
     * @param a left hand side Comment object.
     * @param b right hand side Comment object.
     * @return -1 if left hand side Comment is posted later than right hand side Comment
     * 1 otherwise
     */
	@Override
	public int compare(Comment a, Comment b) {
		if(a.getTimePosted()>=b.getTimePosted()){
			return -1;
		}
		else{
			return 1;
		}
	}
	
}
