package activity;

import user_name.UserNameHandler;
import network_io.IoStreamHandler;
import model.Comment;
import model.CommentMap;

import com.example.projectapp.R;

import adapter.ListViewAdapter;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;

public class AllTopicPageActivity extends Activity implements OnItemSelectedListener {
	
	static String sortByDate = "Sort By Date";
	static String sortByMyLocation = "Sort By My Location";
	static String sortByOtherLocation = "Sort By Other Location";
	static String sortByPicture = "Sort By Picture";
	static String[] sortOption = { sortByDate, sortByMyLocation,
			sortByOtherLocation, sortByPicture };
	
	private ListView listView=null;
	private Spinner spinnerOsversions=null;
	
	private ListViewAdapter listViewAdapter=null;
	private CommentMap topics=null;
	private IoStreamHandler io=null;

	/**
	 *  onCreate method. </br>
	 *  Once the activity is created, first set the content view, and initialize ActionBat and a Spinner for sort options. 
	 *  Then, load all the topics and adapt to the ListView.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_all_topic_page);

		// Initialize View
		initView();
		
		listView = (ListView)findViewById(R.id.topic_list);
		
		io=new IoStreamHandler();
		//io.clean();
		topics=new CommentMap();
		listViewAdapter=new ListViewAdapter(this,R.layout.single_comment_layout,topics.getCurrentList());
		listView.setAdapter(listViewAdapter);
		topics.setArrayAdapter(listViewAdapter);
		listView.setOnItemClickListener(new ViewClick());
	}
	
	class ViewClick implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> arg0,View arg1,int pos,long arg3){
			Comment comment=(Comment)arg0.getItemAtPosition(pos);
			Intent viewIntent=new Intent(AllTopicPageActivity.this,CommentPageActivity.class);
			viewIntent.putExtra("commentID",comment.getId());
			startActivity(viewIntent);
		}
		
	}
	
	@Override
	protected void onResume(){
		super.onResume();
		refresh();
	}
	
	private void refresh(){
		topics.clear();
		io.loadTopLevelComments(this);
		Thread wait = new Thread();
		try {
			wait.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		topics = io.getCommentMap();
//		io.loadTopLevelComments(topics, this);
	}

	/**
	 *  Initialize View. First, get ActionBar to enable title and disable title.
	 *  Second, initialize spinner for sort options
	 */
	private void initView() {
		// ActionBar
		final ActionBar actionBar = getActionBar();
		actionBar.setDisplayUseLogoEnabled(true);
		actionBar.setDisplayOptions(0, ActionBar.DISPLAY_SHOW_TITLE);

		// Spinner for sort options
		spinnerOsversions = (Spinner) findViewById(R.id.welcome_button);
		ArrayAdapter<String> sortArray = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, sortOption);
		sortArray.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerOsversions.setAdapter(sortArray);
		spinnerOsversions.setOnItemSelectedListener(this);
	}

	/**
	 * 
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	/**
	 * 
	 */
	@Override
	public boolean onOptionsItemSelected(final MenuItem item) {
		Intent intent;
		switch (item.getItemId()) {

		case R.id.action_create:
			intent = new Intent(this, CreateCommentPageActivity.class);
			intent.putExtra("isTopLevel",true);
			startActivity(intent);
			return true;
		case R.id.action_favorite:
			 intent = new Intent(this, FavoritePageActivity.class);
			 startActivity(intent);
			 return true;
		case R.id.action_my_comment:
			intent = new Intent(this, MyCommentPageActivity.class);
			startActivity(intent);
			return true;
		case R.id.action_profile:
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

		if (sortSelect == sortByDate) {

		} else if (sortSelect == sortByMyLocation) {

		} else if (sortSelect == sortByOtherLocation) {

		} else if (sortSelect == sortByPicture) {

		}
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
