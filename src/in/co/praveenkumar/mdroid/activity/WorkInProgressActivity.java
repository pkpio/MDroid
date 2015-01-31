package in.co.praveenkumar.mdroid.activity;

import in.co.praveenkumar.R;
import in.co.praveenkumar.mdroid.helper.ApplicationClass;
import in.co.praveenkumar.mdroid.helper.Param;
import in.co.praveenkumar.mdroid.service.MDroidService;
import android.content.Intent;
import android.os.Bundle;

public class WorkInProgressActivity extends BaseNavigationActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_workinprogress);
		setUpDrawer();

		// Send a tracker
		((ApplicationClass) getApplication())
				.sendScreen(Param.GA_SCREEN_WORKINPROGRESS);

		setTitle("Work in progress");

		// Testing services
		Intent i = new Intent(WorkInProgressActivity.this, MDroidService.class);
		i.putExtra("forceCheck", true);
		startService(i);
	}

}
