package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.widget.ArrayAdapter;
/**
 * A class used to store Comment Objects loaded from the server and able to return a list which can used by list view adapter construct
 * @author xuping
 */

public class CommentMap{
	private Map<String,Comment> comments=null;
	private ArrayList<Comment> comments_list=null;
	private ArrayAdapter<Comment> adapter=null;
	
	/**
	 * Construct a empty CommentMap object 
	 */
	
	public CommentMap(){
		this.comments=new HashMap<String,Comment>();
		this.comments_list=new ArrayList<Comment>();
	}
	
	/**
	 * Add a Comment object in the CommentMap with its own id,
	 * if Comment with the same ID exists,do nothing,
	 * notify the ArrayAdapter<Comment> the data set has changed
	 * after update if the ArrayAdapter<Comment> has been set.
	 * @param comment a Comment object.
	 */
	public void addComment(Comment comment){
		if(comments_list.contains(comment)){
			return;
		}
		this.comments.put(comment.getId(),comment);
		this.comments_list.add(comment);
		if(this.adapter!=null){
			this.adapter.notifyDataSetChanged();
		}
	}
	
	/**
	 * Get a specific Comment with its own id from the CommentMap.
	 * @param id a String which is the comment id.
	 * @return the Comment object pointed by the comment id.
	 */
	
	public Comment getComment(String id){
		return this.comments.get(id);
	}
	
	/**
	 * @return a list which contains all Comment objects in this CommentMap which can be used to construct a ListView adapter.
	 */
	
	public List<Comment> getCurrentList(){
		return this.comments_list;
	}
	/**
	 * Set this CommentMap's ArrayAdapter.
	 * @param adapter which is a ArrayAdapter object.
	 */
	public void setArrayAdapter(ArrayAdapter<Comment> adapter){
		this.adapter=adapter;
	}
	
	/**
	 * Empty this CommentMap and notify the ArrayAdapter data set has changed if ArrayAdapter has been set.
	 */
	public void clear(){
		this.comments.clear();
		this.comments_list.clear();
		if(this.adapter!=null){
			this.adapter.notifyDataSetChanged();
		}
	}
}
