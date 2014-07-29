package activity;

import pop_up_window.CustomLocationLoader;
import gps.LocationGenerator;
import user.UserNameHandler;
import network_io.ConnectionChecker;
import network_io.IoStreamHandler;
import network_io.NetworkObserver;
import model.Comment;
import model.CommentMap;

import com.example.projectapp.R;

import adapter.ListViewAdapter;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
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
 * An activity which will Load and show all top-level comments to the user from the Internet(with 5 different sorting options),it also allow user to publish
 * a new top level comment, view a comment, and provide the access to the local comment which stored as favorites or indicated comment and logout option.
 * @author Xuping Fang, Yilu Su
 */

public class AllTopicPageActivity extends Activity implements OnItemSelectedListener {
	
	static String sortByDefault="Sort By Default";
	static String sortByDate = "Sort By Date";
	static String sortByMyLocation = "Sort By My Location";
	static String sortByOtherLocation = "Sort By Other Location";
	static String sortByPicture = "Sort By Picture";
	static String[] sortOption = { sortByDefault, sortByDate, sortByMyLocation,
			sortByOtherLocation, sortByPicture };
	
	// TextView to display the new location
	private TextView newLocation=null;
	
	private ListView listView=null;
	private Spinner spinnerOsversions=null;
	
	private ListViewAdapter listViewAdapter=null;
	
	private CommentMap topics=null;
	private IoStreamHandler io=null;
	private ConnectionChecker connectionChecker=null;
	
	private LocationGenerator locationGenerator=null;
	
	private NetworkObserver netObs=null;

	/**
	 *  onCreate method.
	 *  Once the activity is created, first set the content view, and initialize ActionBar and a Spinner for sort options. 
	 *  Then, load all the topics and adapt to the ListView and set all the click listeners.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_all_topic_page);

		// Initialize View
		initView();
		
		newLocation = (TextView)findViewById(R.id.new_location);
		
		listView = (ListView)findViewById(R.id.topic_list);
		
		io=new IoStreamHandler();
		connectionChecker=new ConnectionChecker();
		//io.clean();
		topics=new CommentMap();
		
		listViewAdapter=new ListViewAdapter(this,R.layout.single_comment_layout,topics.getCurrentList());
		locationGenerator= new LocationGenerator((LocationManager)getSystemService(Context.LOCATION_SERVICE));
		
		listView.setAdapter(listViewAdapter);
		topics.setArrayAdapter(listViewAdapter);
		listView.setOnItemClickListener(new ViewClick());
		
		netObs=new NetworkObserver();
	}
	
	/**
	 * A click listener which direct user to view the details of a specific comment after user click on that comment in the ListView.
	 */
	class ViewClick implements OnItemClickListener{
		/**
		 * Get the Comment in the position which user clicked on, and start CommentPageActivity 
		 * ,Intent is attached with the Comment's id and the name of the Comment's author after click.
		 */
		@Override
		public void onItemClick(AdapterView<?> arg0,View arg1,int pos,long arg3){
			Comment comment=(Comment)arg0.getItemAtPosition(pos);
			Intent viewIntent=new Intent(AllTopicPageActivity.this,CommentPageActivity.class);
			viewIntent.putExtra("commentID",comment.getId());
			viewIntent.putExtra("authorName",comment.getUserName());
			startActivity(viewIntent);
		}
		
	}
	
	/**
	 * onResume method.
	 * This method only calls refresh when the activity calls this method.
	 */
	@Override
	protected void onResume(){
		super.onResume();
		refresh();
	}
	
	/**
	 * When the Internet is on : reload all new top level comments in to the ListView and let the NetworkObserver keep checking if the network goes off.
	 * When the Internet is off: notify user cannot reload data and let the NetworkObserver keep checking if the network is back online.
	 */
	public void refresh(){
		if(connectionChecker.isNetworkOnline(this)){
			topics.clear();
			io.loadTopLevelComments(topics,this);
			netObs.setObserver(this);
		}
		else{
			Toast.makeText(getApplicationContext(),"Offline",Toast.LENGTH_SHORT).show();
			netObs.startObservation(this);
		}
	}

	/**
	 *  Initialize View. Get ActionBar to enable title and disable title.
	 *  setup spinner with sorting options.
	 */
	private void initView() {
		// ActionBar
		final ActionBar actionBar = getActionBar();
		actionBar.setDisplayUseLogoEnabled(true);
		actionBar.setDisplayOptions(0, ActionBar.DISPLAY_SHOW_TITLE);

		// Spinner for sort options
		spinnerOsversions = (Spinner) findViewById(R.id.all_topic_spinner);
		ArrayAdapter<String> sortArray = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, sortOption);
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
		getMenuInflater().inflate(R.menu.main, menu);
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

		case R.id.action_create:
			if((new UserNameHandler()).getUserName(this).equals("")){
				Toast.makeText(getApplicationContext(),"Guest cannot publish comment.",Toast.LENGTH_SHORT).show();
				return true;
			}
			intent = new Intent(this, CreateCommentPageActivity.class);
			intent.putExtra("isTopLevel",true);
			startActivity(intent);
			return true;
		case R.id.action_favorite:
			 intent = new Intent(this, FavoritePageActivity.class);
			 startActivity(intent);
			 return true;
		case R.id.action_my_comment:
			intent = new Intent(this,FavoritePageActivity.class);
			intent.putExtra("isFav",false);
			startActivity(intent);
			return true;
		case R.id.action_profile:
			if((new UserNameHandler()).getUserName(this).equals("")){
				Toast.makeText(getApplicationContext(),"Guest cannot create/edit profile.",Toast.LENGTH_SHORT).show();
				return true;
			}
			intent = new Intent(this, ProfilePageActivity.class);
			startActivity(intent);
			return true;
		case R.id.action_refresh:
			refresh();
			return true;
		case R.id.action_logout:
			intent=new Intent(this,LoginViewActivity.class);
			(new UserNameHandler()).emptyUserName(this);
			startActivity(intent);
			finish();
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
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		spinnerOsversions.setSelection(position);
		String sortSelect = (String) spinnerOsversions.getSelectedItem();
		
		newLocation.setText("");
		
		if (sortSelect == sortByDate) {
			listViewAdapter.setSortingOption(ListViewAdapter.SORT_BY_TIME);
		} 
		else if (sortSelect == sortByMyLocation) {
			Location currentLocation=locationGenerator.getCurrentLocation();
			if(currentLocation==null){
				Toast.makeText(getApplicationContext(),"GPS is not functional, cannot sort.",Toast.LENGTH_SHORT).show();
			}
			else{
				listViewAdapter.setSortingLocation(currentLocation);
			}
		}
		
		else if (sortSelect == sortByOtherLocation) {
			(new CustomLocationLoader()).loadWindow(newLocation,listViewAdapter,locationGenerator,this,this);
		}
		
		else if (sortSelect == sortByPicture) {
			listViewAdapter.setSortingOption(ListViewAdapter.SORT_BY_PIC);
		}
		
		else if(sortSelect == sortByDefault){
			Location currentLocation=locationGenerator.getCurrentLocation();
			if(currentLocation==null){
				Toast.makeText(getApplicationContext(),"GPS is not functional, cannot sort by default.",Toast.LENGTH_SHORT).show();
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
