package in.co.praveenkumar.mdroid.activity;

import in.co.praveenkumar.mdroid.apis.R;
import in.co.praveenkumar.mdroid.helper.AppNavigationDrawer;
import android.os.Bundle;

public class PostActivity extends AppNavigationDrawer {
	int forumid;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_post);
		setUpDrawer();
	}
}
