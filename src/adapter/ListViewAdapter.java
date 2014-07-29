package adapter;
import java.util.Date;
import java.util.List;

import com.example.projectapp.R;

import comparator.LocationComparator;
import comparator.PictureComparator;
import comparator.ScoreSystem;
import comparator.TimeComparator;

import model.Comment;

import android.content.Context;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * A List View Adapter which maps information from the comment model to the view
 * Adapted from https://github.com/YiluSu/PicPosterComplete/blob/master/src/ca/ualberta/cs/picposter/view/PicPostModelAdapter.java
 */
public class ListViewAdapter extends ArrayAdapter<Comment>{
	
	private String sortingOption="default";
	private Location center=null;
	
	public static final String SORT_BY_TIME="sortByTime";
	public static final String SORT_BY_PIC="sortByPicture";
	public static final String SORT_BY_LOC="sortByLocation";
	
	public static final String SORT_BY_DEF="sortByDefault";
	
	/**
	 * Construct a ListViewAdapter.
	 * @param context a Context.
	 * @param textViewResourceId id of the Layout.
	 * @param objects a list of the Comment objects.
	 */

	public ListViewAdapter(Context context,int textViewResourceId,List<Comment> objects) {
		super(context,textViewResourceId,objects);
	}
	/**
	 * Maps different components of the comment object to the views of the CustomListView layout.
	 * @param position of the Object in the list.
	 * @param convertView the view will be set.
	 * @param parent the parent view.
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		if (convertView == null) {
			LayoutInflater inflater = LayoutInflater.from(this.getContext());
			convertView = inflater.inflate(R.layout.single_comment_layout, null);
		}
		Comment comment = this.getItem(position);
		
		if(comment!=null){
			ImageView comment_pic=(ImageView)convertView.findViewById(R.id.comment_pic);
			comment_pic.setImageBitmap(comment.getPicture());
			TextView comment_text=(TextView)convertView.findViewById(R.id.comment_text);
			comment_text.setText(comment.getTitle());
			TextView comment_info=(TextView)convertView.findViewById(R.id.user_name_and_time_posted);
			comment_info.setText("Posted by: "+comment.getUserName()+" At: "+(new Date(comment.getTimePosted()).toString()));
		}
		return convertView;
	}
	
	/**
	 * Sort the ListView by the location provided(Comment more close to this location will appear at front).
	 * @param location : a Location object which will be used as the sorting reference.
	 */
	public void setSortingLocation(Location location){
		this.center=location;
		this.setSortingOption(SORT_BY_LOC);
	}
	/**
	 * Sort the ListView by the location provided and the ScoreSystem (Comment more close to this location and more fresh will appear at front).
	 * @param location : a Location object which will be used as the sorting reference.
	 */
	public void sortByDefault(Location location){
		this.center=location;
		this.setSortingOption(SORT_BY_DEF);
	}
	/**
	 * Overrides the notifyDateSetChanged method, Before display the new content of the ListView, sort the Data with current sorting option.
	 */
	@Override
	public void notifyDataSetChanged(){
		
		this.setNotifyOnChange(false);
		
		if(sortingOption.equals(SORT_BY_TIME)){
			this.sort(new TimeComparator());
		}
		
		else if(sortingOption.equals(SORT_BY_PIC)){
			this.sort(new PictureComparator());
		}
		
		else if(sortingOption.equals(SORT_BY_LOC)){
			this.sort(new LocationComparator(center));
		}
		
		else if(sortingOption.equals(SORT_BY_DEF)){
			this.sort(new ScoreSystem(center));
		}
		
		this.setNotifyOnChange(true);
		
		super.notifyDataSetChanged();
	}
	
	/**
	 * Set the current sorting option.
	 * @param option : a String which is one of the sorting options.
	 */
	public void setSortingOption(String option){
		this.sortingOption=option;
	}
	
}
