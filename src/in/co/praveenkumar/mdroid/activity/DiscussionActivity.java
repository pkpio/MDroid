package in.co.praveenkumar.mdroid.activity;

import android.os.Bundle;
import in.co.praveenkumar.mdroid.apis.R;
import in.co.praveenkumar.mdroid.helper.AppNavigationDrawer;

public class DiscussionActivity extends AppNavigationDrawer {
	int forumid;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_discussion);
		setUpDrawer();
	}
	
}
