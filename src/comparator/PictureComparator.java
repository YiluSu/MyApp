package comparator;

import java.util.Comparator;

import model.Comment;


public class PictureComparator implements Comparator<Comment>{

	@Override
	public int compare(Comment a, Comment b){
		if((a.getPicture()!=null)&&(b.getPicture()==null)){
			return 1;
		}
		else if((a.getPicture()==null)&&(b.getPicture()!=null)){
			return -1;
		}
		else{
			return 0;
		}
	}
	
}
