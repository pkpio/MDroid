package in.co.praveenkumar.mdroid.activities;

import in.co.praveeenkumar.mdroid.extenders.DrawerActivity;
import in.co.praveenkumar.mdroid.apis.R;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;
import android.os.Bundle;

public class MoodleHomeActivity extends DrawerActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_moodle_home);
		super.onCreate(savedInstanceState);

		// StickyHeader List view
		StickyListHeadersListView listview;
		listview = (StickyListHeadersListView) findViewById(R.id.list);
		listview.setAreHeadersSticky(true);
		listview.setAdapter(myListAdapter);

	}
	
	
	

}
