package comparator;

import java.util.Comparator;

import model.Comment;

/**
 * A comparator class which compares 2 Comment objects and sort them by picture.
 * @author xuping
 */
public class PictureComparator implements Comparator<Comment>{

	/**
	 * @param a : left hand side Comment
	 * @param b : right hand side Comment
	 * @return -1 if right hand side Comment's picture is null but left hand side Comment's picture is not null,
	 * 1 if right hand side Comment's picture is not null but left hand side Comment's picture is null,
	 * 0 otherwise.
	 */
	@Override
	public int compare(Comment a, Comment b){
		if((a.getPicture()!=null)&&(b.getPicture()==null)){
			return -1;
		}
		else if((a.getPicture()==null)&&(b.getPicture()!=null)){
			return 1;
		}
		else{
			return 0;
		}
	}
	
}
