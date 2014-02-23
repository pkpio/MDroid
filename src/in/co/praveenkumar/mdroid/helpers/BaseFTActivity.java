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
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

public abstract class BaseFTActivity extends Activity {

	// Dummy function. Will be overridden
	public abstract void replyInThread();

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.forum_thread_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_reply:
			replyInThread();
			break;
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
		case R.id.action_notifications:
			Intent j = new Intent(this, NotificationsActivity.class);
			startActivityForResult(j, 20);
			break;
		}
		return true;
	}

}
