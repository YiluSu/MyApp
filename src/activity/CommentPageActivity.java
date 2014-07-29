package activity;


import pop_up_window.CustomLocationLoader;
import gps.LocationGenerator;
import user.UserNameHandler;
import network_io.ConnectionChecker;
import network_io.IoStreamHandler;
import network_io.NetworkObserver;
import model.Comment;
import model.CommentMap;

import cache.CacheController;

import com.example.projectapp.R;

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
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;

/**
 * An Activity which display a specific Comment's Content, which allows user choose to edit, read/sort browse replies,
 * add a reply, add Comment as favorite, add Comment as indicated comment.
 * view author's profile (if exist).
 * @author Xuping Fang,Yilu Su
 */
public class CommentPageActivity extends Activity implements OnItemSelectedListener {
	
	Spinner spinnerOsversions;
	
	private TextView title=null;
	private TextView content=null;
	private TextView commentInfo=null;
	private ImageView picture=null;
	private ImageButton like=null;
	private ImageButton bookmark=null;
	private ImageButton edit=null;
	private ImageButton viewAuthorProfile=null;
	private TextView commentNewLocation=null;
	private ListView listView=null;
	
	private CommentMap replies=null;
	private ListViewAdapter listViewAdapter=null;
	private IoStreamHandler io=null;
	private ConnectionChecker connectionChecker=null;
	
	private LocationGenerator locationGenerator=null;
	
	private String commentID=null;
	private String authorName=null;
	
	private NetworkObserver netObs=null;
	
	
	
	/**
	 *  onCreate method.
	 *  Once the activity is created, first set the content view, and initialize ActionBar and a Spinner for sort options. 
	 *  Then, load the content of the Comment and adapt to the ListView with the Comment replies and set all the click listeners.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_comment_page);
		
		// Initialize Menu bar
		initView();
		
		title = (TextView)findViewById(R.id.comment_title);
		content = (TextView)findViewById(R.id.comment_content);
		commentInfo = (TextView)findViewById(R.id.comment_info);
		picture = (ImageView)findViewById(R.id.topic_image);
		like = (ImageButton)findViewById(R.id.comment_like);
		bookmark = (ImageButton)findViewById(R.id.comment_bookmark);
		edit = (ImageButton)findViewById(R.id.comment_edit);
		viewAuthorProfile = (ImageButton)findViewById(R.id.view_other_profile);
		commentNewLocation=(TextView)findViewById(R.id.comment_new_location);
		listView = (ListView)findViewById(R.id.reply_list);
		
		io=new IoStreamHandler();
		connectionChecker=new ConnectionChecker();
		replies=new CommentMap();
		listViewAdapter=new ListViewAdapter(this,R.layout.single_comment_layout,replies.getCurrentList());
		listView.setAdapter(listViewAdapter);
		replies.setArrayAdapter(listViewAdapter);
		
		commentID=((getIntent()).getStringExtra("commentID"));
		authorName=((getIntent()).getStringExtra("authorName"));
		
		listView.setOnItemClickListener(new RecurViewClick());
		
		edit.setOnClickListener(new EditClick());
		like.setOnClickListener(new LikeClick());
		bookmark.setOnClickListener(new MarkClick());
		viewAuthorProfile.setOnClickListener(new ViewProfileClick());
		
		locationGenerator=new LocationGenerator((LocationManager)getSystemService(Context.LOCATION_SERVICE));
		
		netObs=new NetworkObserver();
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
	 * A click listener which direct user to view the profile of the Comment author.
	 */
	class ViewProfileClick implements OnClickListener{
		
		/**
		 * Start OtherProfilePageActivity when user click on the button, with the author name attached to the Intent.
		 */
		@Override
		public void onClick(View v){
			Intent pushIntent=new Intent(CommentPageActivity.this,OtherProfilePageActivity.class);
			pushIntent.putExtra("userName",authorName);
			startActivity(pushIntent);
		}
		
	}
	
	/**
	 * A click listener which direct user to edit the content of the Comment's id the current user name is the author's name.
	 */
	class EditClick implements OnClickListener{
		
		/**
		 * Start EditCommentPageActivity with the comment id attached to the Intent 
		 * if the Comment author name is the current user name
		 * otherwise notify user that he cannot edit this Comment (After Click).
		 */
		@Override
		public void onClick(View v){
			if(!authorName.equals((new UserNameHandler()).getUserName(CommentPageActivity.this))){
				Toast.makeText(getApplicationContext(),"Only author can edit comment.",Toast.LENGTH_SHORT).show();
			}
			else if(connectionChecker.isNetworkOnline(CommentPageActivity.this)==false){
				Toast.makeText(getApplicationContext(),"Offline.",Toast.LENGTH_SHORT).show();
			}
			else{
				Intent pushIntent=new Intent(CommentPageActivity.this,EditCommentPageActivity.class);
				pushIntent.putExtra("commentID",commentID);
				startActivity(pushIntent);
			}
		}
		
	}
	
	/**
	 * A click listener will save the Comment as favorite locally if network is connected,
	 * otherwise notify user that network is not connected.
	 */
	class LikeClick implements OnClickListener{
		
		/**
		 * When user click on the button,save the Comment as favorite locally if 
		 * network is connected, otherwise notify user that network is not connected.
		 */
		@Override
		public void onClick(View v){
			if(connectionChecker.isNetworkOnline(CommentPageActivity.this)==false){
				Toast.makeText(getApplicationContext(),"Offline.",Toast.LENGTH_SHORT).show();
				return;
			}
			CacheController cc=new CacheController();
			io.addCache(commentID,null,cc,"fav",CommentPageActivity.this);
			Toast.makeText(getApplicationContext(),"Liked.",Toast.LENGTH_SHORT).show();
		}
	}
	
	/**
	 * A click listener will save the Comment as indicated Comment locally if network is connected, otherwise notify user that network is not connected.
	 */
	class MarkClick implements OnClickListener{
		
		/**
		 * When user click on the button,save the Comment as indicated Comment locally if 
		 * network is connected, otherwise notify user that network is not connected.
		 */
		@Override
		public void onClick(View v){
			if(connectionChecker.isNetworkOnline(CommentPageActivity.this)==false){
				Toast.makeText(getApplicationContext(),"Offline.",Toast.LENGTH_SHORT).show();
				return;
			}
			CacheController cc=new CacheController();
			io.addCache(commentID,null,cc,"indicated",CommentPageActivity.this);
			Toast.makeText(getApplicationContext(),"Marked.",Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * A click listener which direct user to view the details of a specific reply after user click on that comment in the ListView.
	 */
	class RecurViewClick implements OnItemClickListener{

		/**
		 * Get the Comment in the position which user clicked on, and start CommentPageActivity 
		 * ,Intent is attached with the Comment's id and the name of the Comment's author.
		 */
		@Override
		public void onItemClick(AdapterView<?> arg0,View arg1,int pos,long arg3){
			Comment comment=(Comment)arg0.getItemAtPosition(pos);
			Intent viewIntent=new Intent(CommentPageActivity.this,CommentPageActivity.class);
			viewIntent.putExtra("commentID",comment.getId());
			viewIntent.putExtra("authorName",comment.getUserName());
			startActivity(viewIntent);
		}
		
	}
	
	/**
	 * When the Internet is on : reload all information regard to this Comment and let the NetworkObserver keep checking if the network goes off.
	 * When the Internet is off: notify user cannot reload data and let the NetworkObserver keep checking if the network is back online.
	 */
	public void refresh(){
		if(connectionChecker.isNetworkOnline(this)){
			replies.clear();
			io.loadAndSetSpecificComment(commentID,title,content,commentInfo,picture,replies,this);
			netObs.setObserver(this);
		}
		else{
			Toast.makeText(getApplicationContext(),"Offline",Toast.LENGTH_SHORT).show();
			netObs.startObservation(this);
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
		spinnerOsversions = (Spinner) findViewById(R.id.comment_spinner);
		ArrayAdapter<String> sortArray = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, AllTopicPageActivity.sortOption);
		sortArray.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerOsversions.setAdapter(sortArray);
		spinnerOsversions.setOnItemSelectedListener(this);
	}
	
	/**
	 * Set ActionBar's click listener, which allows user to create a new reply,
	 * view favorite Comment,view indicated Comment,view his own profile,refresh the reply list view
	 * due to which button user clicked on.
	 * @param item : MenuItem which contains the menu item.
	 */
	@Override
	public boolean onOptionsItemSelected(final MenuItem item) {
		Intent intent;
		switch (item.getItemId()) {

		case R.id.action_create:
			if((new UserNameHandler()).getUserName(this).equals("")){
				Toast.makeText(getApplicationContext(),"Guest cannot reply to comment.",Toast.LENGTH_SHORT).show();
				return true;
			}
			intent = new Intent(this, CreateCommentPageActivity.class);
			intent.putExtra("parentID",commentID);
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
		}
		return super.onOptionsItemSelected(item);
	}
	
	/**
	 * Inflate the menu; this adds items to the action bar if it is present.
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.comment_page, menu);
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
		
		commentNewLocation.setText("");
		
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
			(new CustomLocationLoader()).loadWindow(commentNewLocation,listViewAdapter,locationGenerator,this,this);
		} 
		else if (sortSelect == AllTopicPageActivity.sortByPicture) {
			listViewAdapter.setSortingOption(ListViewAdapter.SORT_BY_PIC);
		}
		else if(sortSelect == AllTopicPageActivity.sortByDefault){
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
