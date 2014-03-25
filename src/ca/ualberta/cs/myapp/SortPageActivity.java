package ca.ualberta.cs.myapp;

import com.example.myapp.R;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;

public class SortPageActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sort_page);
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowTitleEnabled(false);
	}

}
