/*
 * Author: 	Praveen Kumar Pendyala
 * Project: MDroid
 * Created:	28-12-2013
 * 
 * © 2013, Praveen Kumar Pendyala. 
 * Licensed under the Creative Commons Attribution-NonCommercial-ShareAlike 
 * 3.0 Unported license, http://creativecommons.org/licenses/by-nc-sa/3.0/ 
 * 
 * This is a part of the MDroid project. You may use the contents of this file
 * or the project only if you comply with license of the project, available at the 
 * Github repo: https://github.com/praveendath92/MDroid/blob/master/README.md
 * 
 */

package in.co.praveenkumar.mdroid.helpers;

import in.co.praveenkumar.R;
import in.co.praveenkumar.mdroid.NotificationsActivity;
import in.co.praveenkumar.mdroid.SettingsActivity;
import android.app.Dialog;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public abstract class BaseFragmentActivity extends FragmentActivity {
	MenuItem favMenu;

	// Dummy functions. Will be overridden
	public abstract void downloadAllFiles();

	public abstract void updateFavStatus();

	public abstract void checkFavStatus();

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.file_listing_menu, menu);
		favMenu = menu.findItem(R.id.action_favourite);
		checkFavStatus();
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
		case R.id.action_downloadall:
			downloadAllFiles();
			break;
		case R.id.action_favourite:
			updateFavStatus();
			break;
		case R.id.action_notifications:
			Intent j = new Intent(this, NotificationsActivity.class);
			startActivityForResult(j, 20);
			break;
		}
		return true;
	}

	public MenuItem getFavMenuItem() {
		return favMenu;
	}
}
