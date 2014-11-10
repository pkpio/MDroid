package in.co.praveenkumar.mdroid.activity;

import in.co.praveenkumar.R;
import android.os.Bundle;

public class ForumActivity extends BaseNavigationActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_forum);
		setUpDrawer();
		getSupportActionBar().setTitle("Forums");
		getSupportActionBar().setIcon(R.drawable.icon_forum);
	}

}
