package in.co.praveenkumar.mdroid.activity;

import in.co.praveenkumar.mdroid.helper.AppNavigationDrawer;
import in.co.praveenkumar.R;
import android.os.Bundle;

public class ForumActivity extends AppNavigationDrawer {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_forum);
		setUpDrawer();
		setTitle("Forums");
	}

}
