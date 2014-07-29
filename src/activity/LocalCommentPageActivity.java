package activity;

import gps.LocationGenerator;

import java.util.Date;

import pop_up_window.CustomLocationLoader;

import model.Comment;
import model.CommentList;

import cache.CacheController;

import com.example.projectapp.R;
import com.google.gson.Gson;

import customlized_gson.GsonConstructor;

import adapter.ListViewAdapter;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;

/**
 * An Activity will display the content of a locally cached Comment to the user with the option to sort the Comment replies.
 * @author Xuping Fang, Yilu Su
 */
public class LocalCommentPageActivity extends Activity implements OnItemSelectedListener {
	
	Spinner spinnerOsversions;

	private TextView title=null;
	private TextView content=null;
	private TextView commentInfo=null;
	private TextView localNewLocation=null;
	private ImageView picture=null;
	private ListView listView=null;
	
	private Gson gson=(new GsonConstructor()).getGson();
	
	private LocationGenerator locationGenerator=null;
	
	private ListViewAdapter listViewAdapter=null;
	
	/**
	 *  onCreate method.
	 *  Once the activity is created, first set the content view, and initialize ActionBar and a Spinner for sort options. 
	 *  Then, load the content of the Comment and adapt to the ListView with the Comment replies and set all the click listeners.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_local_comment_page);
		
		// Initialize Menu bar
		initView();
		
		title = (TextView)findViewById(R.id.local_comment_title);
		content = (TextView)findViewById(R.id.local_comment_content);
		commentInfo = (TextView)findViewById(R.id.local_comment_info);
		localNewLocation=(TextView)findViewById(R.id.local_comment_new_location);
		picture = (ImageView)findViewById(R.id.local_topic_image);
		listView = (ListView)findViewById(R.id.local_reply_list);
		
		Intent intent=getIntent();
		Comment comment=gson.fromJson(intent.getStringExtra("commentJson"),Comment.class);
		title.setText(comment.getTitle());
		content.setText(comment.getText());
		picture.setImageBitmap(comment.getPicture());
		Location location=comment.getLocation();
		if(location!=null){
			double lat=location.getLatitude();
			double lng=location.getLongitude();
			String lngS=String.valueOf(lng);
			String latS=String.valueOf(lat);
			commentInfo.setText("Posted By : "+comment.getUserName()+"\nAt : "+((new Date(comment.getTimePosted())).toString())+"\nLongitude: "+lngS+"\nLatitude: "+latS);
		}
		else{
			commentInfo.setText("Posted By : "+comment.getUserName()+"\nAt : "+((new Date(comment.getTimePosted())).toString()));
		}
		CommentList replies=(new CacheController()).getReply(comment.getId(),this);
		listViewAdapter=new ListViewAdapter(this,R.layout.single_comment_layout,replies.getCurrentList());
		listView.setAdapter(listViewAdapter);
		listView.setOnItemClickListener(new RecurLocalView());
		
		locationGenerator=new LocationGenerator((LocationManager)getSystemService(Context.LOCATION_SERVICE));
	}
	
	/**
	 * A click listener will direct user to view the content of a Comment if user clicked on that Comment in the ListView.
	 */
	class RecurLocalView implements OnItemClickListener{

		/**
		 * Start the LocalCommentPageActivity with Intent attached with the Json String of that Comment.
		 */
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int pos,long arg3){
			Intent pushIntent=new Intent(LocalCommentPageActivity.this,LocalCommentPageActivity.class);
			pushIntent.putExtra("commentJson",gson.toJson((Comment)arg0.getItemAtPosition(pos)));
			startActivity(pushIntent);
		}
		
	}
	
	/**
	 * Initialize View. First, get ActionBar to enable title and disable title.
	 * Second, initialize spinner for sort options
	 */
	private void initView() {
		// ActionBar
		final ActionBar actionBar = getActionBar();
		actionBar.setDisplayUseLogoEnabled(true);
		actionBar.setDisplayOptions(0, ActionBar.DISPLAY_SHOW_TITLE);
		
		// Spinner for sort options
		spinnerOsversions = (Spinner) findViewById(R.id.local_comment_spinner);
		ArrayAdapter<String> sortArray = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, AllTopicPageActivity.sortOption);
		sortArray.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerOsversions.setAdapter(sortArray);
		spinnerOsversions.setOnItemSelectedListener(this);
	}

	/**
	 * Inflate the menu; this adds items to the action bar if it is present.
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.local_comment_page, menu);
		return true;
	}

	/**
	 * Callback method to be invoked when an item in this view has been selected. 
	 * This callback is invoked only when the newly selected position is different 
	 * from the previously selected position or if there was no selected item.
	 * @param	parent		The AdapterView where the selection happened.
	 * @param	view		The view within the AdapterView that was clicked
	 * @param	position	The position of the view in the adapter
	 * @param	id			The row id of the item that is selected 
	 */
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		spinnerOsversions.setSelection(position);
		String sortSelect = (String) spinnerOsversions.getSelectedItem();
		
		localNewLocation.setText("");

		if (sortSelect == AllTopicPageActivity.sortByDate) {
			listViewAdapter.setSortingOption(ListViewAdapter.SORT_BY_TIME);
		} 
		else if (sortSelect == AllTopicPageActivity.sortByMyLocation) {
			Location currentLocation=locationGenerator.getCurrentLocation();
			if(currentLocation==null){
				Toast.makeText(getApplicationContext(),"GPS is not functional, cannot sort.",Toast.LENGTH_SHORT).show();
			}
			else{
				listViewAdapter.setSortingLocation(currentLocation);
			}
		} 
		else if (sortSelect == AllTopicPageActivity.sortByOtherLocation) {
			(new CustomLocationLoader()).loadWindow(localNewLocation,listViewAdapter,locationGenerator,this,this);
		} 
		else if (sortSelect == AllTopicPageActivity.sortByPicture) {
			listViewAdapter.setSortingOption(ListViewAdapter.SORT_BY_PIC);
		}
		else if(sortSelect == AllTopicPageActivity.sortByDefault){
			Location currentLocation=locationGenerator.getCurrentLocation();
			if(currentLocation==null){
				Toast.makeText(getApplicationContext(),"GPS is not functional, cannot sort by deafult.",Toast.LENGTH_SHORT).show();
			}
			else{
				listViewAdapter.sortByDefault(currentLocation);
			}
		}
		listViewAdapter.notifyDataSetChanged();
	}

	/**
	 * Callback method to be invoked when the selection disappears from this view. 
	 * The selection can disappear for instance when touch is activated or when the 
	 * adapter becomes empty.
	 * @param	arg0	The AdapterView that now contains no selected item. 
	 */
	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		
	}

}
