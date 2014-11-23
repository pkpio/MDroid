package in.co.praveenkumar.mdroid.activity;

import in.co.praveenkumar.R;
import android.os.Bundle;

public class WorkInProgressActivity extends BaseNavigationActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_workinprogress);
		setUpDrawer();
		setTitle("Work in progress");
	}

}
