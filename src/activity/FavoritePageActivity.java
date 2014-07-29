package activity;

import pop_up_window.CustomLocationLoader;
import gps.LocationGenerator;
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
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

/**
 * An activity which will Load and show all comments which has been marked as favorite 
 * or indicated Comment to the user from the shared preferences 
 * (with 5 different sorting options) in a ListView, user is able to
 *  view a comment, and provide the access to the local comment 
 *  which stored as favorites or indicated comment.
 * @author Xuping Fang, Yilu Su
 */
public class FavoritePageActivity extends Activity implements OnItemSelectedListener {

	Spinner spinnerOsversions;
	
	private ListView listView = null;
	private CacheController cacheController=null;
	private CommentList list=null;
	private ListViewAdapter listViewAdapter=null;
	private LocationGenerator locationGenerator=null;
	
	private Gson gson=(new GsonConstructor()).getGson();
	
	private TextView favNewLocation=null;
	
	/**
	 *  onCreate method.
	 *  Once the activity is created, first set the content view, and initialize ActionBar and a Spinner for sort options. 
	 *  Then, load the Comment been marked as favorite or indicated depends on the intent extra, and adapt to the ListView with the Comment
	 *  and set all the click listeners.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_favorite_page);
		
		initView();
		
		listView = (ListView)findViewById(R.id.favorite_list);
		
		cacheController=new CacheController();
		Intent intent=getIntent();
		if(intent.getBooleanExtra("isFav",true)){
			list=cacheController.getResource(this,"fav");
		}
		else{
			list=cacheController.getResource(this,"indicated");
		}
		listViewAdapter=new ListViewAdapter(this,R.layout.single_comment_layout,list.getCurrentList());
		listView.setAdapter(listViewAdapter);
		
		listView.setOnItemClickListener(new FavViewClick());
		
		locationGenerator=new LocationGenerator((LocationManager)getSystemService(Context.LOCATION_SERVICE));
		
		favNewLocation=(TextView)findViewById(R.id.fav_new_location);
	}
	/**
	 * A click listener will direct user to view the content of a Comment if user clicked on that Comment in the ListView.
	 */
	class FavViewClick implements OnItemClickListener{

		/**
		 * Start the LocalCommentPageActivity with Intent attached with the Json String of that Comment.
		 */
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int pos,long arg3){
			Intent pushIntent=new Intent(FavoritePageActivity.this,LocalCommentPageActivity.class);
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
		spinnerOsversions = (Spinner) findViewById(R.id.fav_spinner);
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
		getMenuInflater().inflate(R.menu.favorite_page, menu);
		return true;
	}

	/**
	 * Set ActionBar's click listener, which allows user to create a new Comment,
	 * view favorite Comment,view indicated Comment,view his own profile,refresh the list view
	 * and logout due to which button user clicked on.
	 * @param item : MenuItem which contains the menu item.
	 */
	@Override
	public boolean onOptionsItemSelected(final MenuItem item) {
		Intent intent;
		switch (item.getItemId()) {

		case R.id.action_profile:
			intent = new Intent(this,ProfilePageActivity.class);
			startActivity(intent);
			return true;
		}
		return super.onOptionsItemSelected(item);
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
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		
		favNewLocation.setText("");
		spinnerOsversions.setSelection(position);
		String sortSelect = (String) spinnerOsversions.getSelectedItem();

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
			(new CustomLocationLoader()).loadWindow(favNewLocation,listViewAdapter,locationGenerator,this,this);
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
