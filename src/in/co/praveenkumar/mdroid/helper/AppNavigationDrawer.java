package in.co.praveenkumar.mdroid.helper;

import in.co.praveenkumar.mdroid.helper.AppInterface.DrawerStateInterface;
import in.co.praveenkumar.R;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.DrawerLayout.DrawerListener;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;

public abstract class AppNavigationDrawer extends FragmentActivity implements
		DrawerStateInterface, DrawerListener {
	private DrawerLayout mDrawerLayout;
	private ActionBarDrawerToggle mDrawerToggle;
	private CharSequence title = "MDroid";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle("");
	}

	public void setUpDrawer() {
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerToggle = new ActionBarDrawerToggle(this, /* host Activity */
		mDrawerLayout, /* DrawerLayout object */
		R.drawable.ic_navigation_drawer, /* nav drawer icon to replace 'Up' caret */
		R.string.drawer_open, /* "open drawer" description */
		R.string.drawer_close /* "close drawer" description */
		);

		// Set the drawer toggle as the DrawerListener
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	/**
	 * Overriding menu key press to show left navigation menu. All other menu
	 * related functions like onPrepareOptionsMenuare, onCreateOptionsMenu are
	 * also called once when the Activity is created. So, we are taking this
	 * approach.
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_MENU) {
			if (!mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
				mDrawerLayout.openDrawer(Gravity.START);
				mDrawerLayout.closeDrawer(Gravity.END);
			} else
				mDrawerLayout.closeDrawer(Gravity.START);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Pass the event to ActionBarDrawerToggle, if it returns
		// true, then it has handled the app icon touch event
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			mDrawerLayout.closeDrawer(Gravity.END); // to prevent overlapping
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	/**
	 * Sets the drawer state.
	 * 
	 * @param state
	 *            True: Open drawer if closed. False: Close drawer if open.
	 */
	@Override
	public void setDrawerState(Boolean state) {
		if (state) {
			mDrawerLayout.openDrawer(Gravity.START);
			mDrawerLayout.closeDrawer(Gravity.END); // to prevent overlapping
		} else
			mDrawerLayout.closeDrawer(Gravity.START);
	}

	@Override
	public void onDrawerClosed(View arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDrawerOpened(View arg0) {
		// this.title = getActivityTitle();
		setTitle("MDroid");
	}

	@Override
	public void onDrawerSlide(View arg0, float arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDrawerStateChanged(int arg0) {
		// TODO Auto-generated method stub

	}

	// public abstract CharSequence getActivityTitle();
}
