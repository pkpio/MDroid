package in.co.praveenkumar.mdroid.activity;

import android.os.Bundle;
import in.co.praveenkumar.R;
import in.co.praveenkumar.mdroid.helper.AppNavigationDrawer;

public class ContactActivity extends AppNavigationDrawer {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contact);
		setUpDrawer();
		setTitle("Contacts");
	}

}
