package model;

import java.util.ArrayList;

import java.util.List;


/**
 * A list class used to store locally cached Comment objects in shared preferences with Json format and able to return a list for the listView adapter construction.
 * @author Yilu Su
 */
public class CommentList{
	
	private ArrayList<Comment> comments=null;
	
	/**
	 * Constructs an empty CommentList object.
	 */
	
	public CommentList(){
		comments=new ArrayList<Comment>();
	}
	/**
	 * Add a Comment object to the CommentList, if a Comment with a same id already exists, then the old one will be removed before the new one been added. 
	 * @param comment : a Comment object.
	 */
	public void add(Comment comment){
		this.comments.remove(comment);
		this.comments.add(comment);
	}
	/**
	 * Empty the CommentList.
	 */
	
	public void clear(){
		this.comments.clear();
	}
	/**
	 * @return a list which can be used to construct a ListView adapter.
	 */
	public List<Comment> getCurrentList(){
		return this.comments;
	}
}
