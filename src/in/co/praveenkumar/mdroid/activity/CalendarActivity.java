package in.co.praveenkumar.mdroid.activity;

import in.co.praveenkumar.R;
import android.os.Bundle;

public class CalendarActivity extends BaseNavigationActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_calendar);
		setUpDrawer();
		getSupportActionBar().setTitle("Calendar");
		getSupportActionBar().setIcon(R.drawable.icon_today);
	}
}
