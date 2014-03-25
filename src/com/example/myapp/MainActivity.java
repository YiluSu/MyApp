package com.example.myapp;

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

	private void initView(){
		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setDisplayOptions(0, ActionBar.DISPLAY_SHOW_TITLE);

		actionBar.addTab(actionBar.newTab().setText("All Topics").setTabListener(new MyTabListener<TopicPage>(this,TopicPage.class)));

		actionBar.addTab(actionBar.newTab().setText("Favorites").setTabListener(new MyTabListener<FavoritePage>(this,FavoritePage.class)));

		actionBar.addTab(actionBar.newTab().setText("My Comments").setTabListener(new MyTabListener<MyCommentPage>(this,MyCommentPage.class)));
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu){

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
