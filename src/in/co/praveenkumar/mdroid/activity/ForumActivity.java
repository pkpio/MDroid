package in.co.praveenkumar.mdroid.activity;

import in.co.praveenkumar.R;
import in.co.praveenkumar.mdroid.helper.ApplicationClass;
import in.co.praveenkumar.mdroid.helper.Param;
import android.os.Bundle;

public class ForumActivity extends BaseNavigationActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_forum);
		setUpDrawer();

		// Send a tracker
		((ApplicationClass) getApplication()).sendScreen(Param.GA_SCREEN_FORUM);

		getSupportActionBar().setTitle("Forums");
		getSupportActionBar().setIcon(R.drawable.icon_forum);
	}

}
