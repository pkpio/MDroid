package in.co.praveenkumar.mdroid.activity;

import android.os.Bundle;
import in.co.praveenkumar.R;
import in.co.praveenkumar.mdroid.helper.BaseNavigationActivity;

public class ContactActivity extends BaseNavigationActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contact);
		setUpDrawer();
		getSupportActionBar().setTitle("Contacts");
		getSupportActionBar().setIcon(R.drawable.icon_people);
	}

}
