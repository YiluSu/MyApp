package ca.ualberta.cs.myapp;

import com.example.myapp.R;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

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
		actionBar.addTab(actionBar.newTab().setText("All Topics").setTabListener(new MyTabListener<TopicPage>(this,TopicPage.class)));
		actionBar.addTab(actionBar.newTab().setText("Favorites").setTabListener(new MyTabListener<FavoritePage>(this,FavoritePage.class)));
		actionBar.addTab(actionBar.newTab().setText("My Comments").setTabListener(new MyTabListener<MyCommentPage>(this,MyCommentPage.class)));
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
