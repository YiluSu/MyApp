package ca.ualberta.cs.myapp;

import com.example.myapp.R;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		initView();
	}	

	/**
	 * Initialize ActionBar
	 */
	private void initView(){
		//get ActionBar
		final ActionBar actionBar = getActionBar();
		
		// set navigation mode to tab
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		
		//enable Logo
		actionBar.setDisplayUseLogoEnabled(true);
		
		actionBar.setDisplayOptions(0, ActionBar.DISPLAY_SHOW_TITLE);

		//add Tabs
		actionBar.addTab(actionBar.newTab().setText("All Topics").setTabListener(new MyTabListener<FragmentAllTopicPage>(this,FragmentAllTopicPage.class)));
		actionBar.addTab(actionBar.newTab().setText("Favorites").setTabListener(new MyTabListener<FragmentFavoritePage>(this,FragmentFavoritePage.class)));
		actionBar.addTab(actionBar.newTab().setText("My Comments").setTabListener(new MyTabListener<FragmentMyCommentPage>(this,FragmentMyCommentPage.class)));
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(final MenuItem item) {
		Intent intent;
		switch (item.getItemId()) {

		case R.id.action_create:
			intent = new Intent(this, CommentPageActivity.class);
			startActivity(intent);
			return true;
		case R.id.action_sort:
			intent = new Intent(this, SortPageActivity.class);
			startActivity(intent);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
