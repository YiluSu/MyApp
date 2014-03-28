package model;

import java.util.ArrayList;
import java.util.Collection;

/**
 * A class which used to store all of the top level comment ids.
 * @author xuping
 *
 */
public class IdSet{
	private ArrayList<String> set=null;
	
	/**
	 * Construct an empty IdSet class.
	 */
	public IdSet(){
		this.set=new ArrayList<String>();
	}
	/**
	 *
	 * @return  all of the Comment(top level) ids in an ArrayList.
	 */
	public ArrayList<String> getSet(){
		return this.set;
	}
	/**
	 * Add an Comment id to this IdSet
	 * @param id a String which is a comment id.
	 */
	public void add(String id){
		this.set.add(id);
	}
	
	/**
	 * Add a set of Comment ids to this IdSet.
	 * @param c which is a Collection of Strings.
	 */
	public void addAll(Collection<String> c){
		this.set.addAll(c);
	}
	
	/**
	 * Empty this IdSet.
	 */
	
	public void clear(){
		this.set.clear();
	}

}
