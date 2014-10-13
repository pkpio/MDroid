package in.co.praveenkumar.mdroid.activity;

import in.co.praveenkumar.mdroid.helper.AppNavigationDrawer;
import in.co.praveenkumar.mdroid.legacy.R;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class WorkInProgressActivity extends AppNavigationDrawer {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_workinprogress);
		setUpDrawer();
		setTitle("Work in progress");
	}

	public void requestFeature(View v) {
		this.startActivity(new Intent(this, DonationActivity.class));
	}

}
