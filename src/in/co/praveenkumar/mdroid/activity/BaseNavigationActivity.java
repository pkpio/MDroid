package in.co.praveenkumar.mdroid.activity;

import in.co.praveenkumar.R;
import in.co.praveenkumar.mdroid.helper.AppInterface.DonationInterface;
import in.co.praveenkumar.mdroid.helper.AppInterface.DrawerStateInterface;
import in.co.praveenkumar.mdroid.helper.Param;
import in.co.praveenkumar.mdroid.playgames.GameHelper;
import in.co.praveenkumar.mdroid.playgames.GameUnlocker;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.google.android.gms.common.api.GoogleApiClient;

/**
 * Extending this would implement a side navigation and billing capabilities
 * into a activity. <b>Requires:</b><br/>
 * 1. Drawerlayout in the activity xml layout<br/>
 * 2. Call to setUpDrawer() after inflating layout in onCreate()
 * 
 * @author Praveen Kumar Pendyala <praveen@praveenkumar.co.in>
 * 
 */
@SuppressWarnings("deprecation")
public abstract class BaseNavigationActivity extends ActionBarActivity
		implements DrawerStateInterface, DonationInterface,
		GameHelper.GameHelperListener {
	DrawerLayout mDrawerLayout;
	ActionBarDrawerToggle mDrawerToggle;
	GameHelper mHelper;
	BillingProcessor billing;

	// Title settings
	private CharSequence MenuTitle = "MDroid";
	private CharSequence LastTitle = "MDroid";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Play games related
		if (mHelper == null) {
			getGameHelper();
		}
		mHelper.setup(this);

		setTitle("");

		// Setup billing
		billing = new BillingProcessor(this, Param.BILLING_LICENSE_KEY,
				new BillingProcessor.IBillingHandler() {
					@Override
					public void onProductPurchased(String productId,
							TransactionDetails details) {
						Toast.makeText(getApplicationContext(),
								R.string.donations_already_purchased,
								Toast.LENGTH_LONG).show();
					}

					@Override
					public void onBillingError(int errorCode, Throwable error) {
						Toast.makeText(getApplicationContext(),
								R.string.donations_purchase_failed,
								Toast.LENGTH_LONG).show();
					}

					@Override
					public void onBillingInitialized() {
					}

					@Override
					public void onPurchaseHistoryRestored() {
					}
				});
	}

	@Override
	public void onResume() {
		super.onResume();
		// Game streak is hard, important and can't be mixed or broken!
		GameUnlocker mGameUnlocker = new GameUnlocker(getApplicationContext());
		mGameUnlocker.updateMaxStreak();
	}

	public void setUpDrawer() {
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerToggle = new ActionBarDrawerToggle(this, /* host Activity */
		mDrawerLayout, /* DrawerLayout object */
		R.drawable.ic_navigation_drawer, /* nav drawer icon to replace 'Up' caret */
		R.string.drawer_open, /* "open drawer" description */
		R.string.drawer_close /* "close drawer" description */
		) {

			/** Called when a drawer has settled in a completely closed state. */
			public void onDrawerClosed(View view) {
				getSupportActionBar().setTitle(LastTitle);
			}

			/** Called when a drawer has settled in a completely open state. */
			public void onDrawerOpened(View drawerView) {
				LastTitle = getSupportActionBar().getTitle();
				getSupportActionBar().setTitle(MenuTitle);
			}
		};

		// Set the drawer toggle as the DrawerListener
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
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
			if (!mDrawerLayout.isDrawerOpen(GravityCompat.START))
				mDrawerLayout.openDrawer(Gravity.LEFT);
			else
				mDrawerLayout.closeDrawer(Gravity.LEFT);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Pass the event to ActionBarDrawerToggle, if it returns
		// true, then it has handled the app icon touch event
		if (mDrawerToggle.onOptionsItemSelected(item))
			return true;

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
		if (state)
			mDrawerLayout.openDrawer(Gravity.LEFT);
		else
			mDrawerLayout.closeDrawer(Gravity.LEFT);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (!billing.handleActivityResult(requestCode, resultCode, data))
			super.onActivityResult(requestCode, resultCode, data);

		// GPG related
		mHelper.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onDestroy() {
		if (billing != null)
			billing.release();
		super.onDestroy();
	}

	@Override
	public Boolean isProUser() {
		if (billing == null)
			return false;

		return billing.isPurchased(Param.BILLING_DONATION_PID)
				|| billing.isPurchased(Param.BILLING_FEATURE_NOTIFICATIONS_PID)
				|| billing.isPurchased(Param.BILLING_FEATURE_PARTICIPANTS_PID)
				|| billing.isPurchased(Param.BILLING_FEATURE_SEARCH_PID)
				|| billing.isPurchased(Param.BILLING_FEATURE_UPLOADS_PID);
	}

	/**
	 * Google Play Games related
	 */

	public GameHelper getGameHelper() {
		if (mHelper == null) {
			mHelper = new GameHelper(this, GameHelper.CLIENT_GAMES);
			mHelper.enableDebugLog(false);
			mHelper.setMaxAutoSignInAttempts(0); // Never AutoSignIn
		}
		return mHelper;
	}

	@Override
	protected void onStart() {
		super.onStart();
		mHelper.onStart(this);
	}

	@Override
	protected void onStop() {
		super.onStop();
		mHelper.onStop();
	}

	protected GoogleApiClient getApiClient() {
		return mHelper.getApiClient();
	}

	protected boolean isSignedIn() {
		return mHelper.isSignedIn();
	}

	@Override
	public void onSignInFailed() {
		// TODO Auto-generated method stub
	}

	@Override
	public void onSignInSucceeded() {
		// TODO Auto-generated method stub
	}
}
