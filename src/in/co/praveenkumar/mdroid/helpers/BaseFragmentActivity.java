package in.co.praveenkumar.mdroid.helpers;

import in.co.praveenkumar.R;
import in.co.praveenkumar.mdroid.SettingsActivity;
import android.app.Dialog;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;

public class BaseFragmentActivity extends FragmentActivity {

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_settings:
			Intent i = new Intent(this, SettingsActivity.class);
			startActivityForResult(i, 10);
			break;
		case R.id.action_help:
			Dialog d = new Dialog(this);
			d.setContentView(R.layout.help);
			d.setTitle("Help");
			d.show();
			break;
		}
		return true;
	}
}
