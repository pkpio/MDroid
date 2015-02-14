package in.co.praveenkumar.mdroid.activity;

import in.co.praveenkumar.R;
import in.co.praveenkumar.mdroid.fragment.NormalLoginFragment;
import in.co.praveenkumar.mdroid.fragment.ParanoidLoginFragment;
import in.co.praveenkumar.mdroid.helper.ApplicationClass;
import in.co.praveenkumar.mdroid.helper.Param;
import in.co.praveenkumar.mdroid.helper.SessionSetting;
import in.co.praveenkumar.mdroid.playgames.GameUnlocker;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;

public class LoginActivity extends FragmentActivity {
	private final String DEBUG_TAG = "LoginActivity";
	LoginFragmentAdapter mAdapter;
	ViewPager mPager;
	private String[] tabs = { "Normal", "Paranoid" };

	@SuppressLint("InlinedApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		// Increment usercount for game
		GameUnlocker mGameUnlocker = new GameUnlocker(getApplicationContext());
		mGameUnlocker.incrementUseCount();
		mGameUnlocker.updateMaxStreak();

		// Is Login called by user to add an account
		Boolean explicitCall = false;
		try {
			explicitCall = getIntent().getExtras().getBoolean("explicitCall");
		} catch (Exception e) {
			Log.d(DEBUG_TAG, "Not an explicit call");
		}

		// Skip to courses if logged in and not requested to add a new account
		SessionSetting session = new SessionSetting(this);
		if (session.getCurrentSiteId() != SessionSetting.NO_SITE_ID
				&& !explicitCall) {
			Intent i = new Intent(this, CourseActivity.class);
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
					| Intent.FLAG_ACTIVITY_CLEAR_TASK);
			this.startActivity(i);
			return;
		}

		// Send a tracker
		((ApplicationClass) getApplication()).sendScreen(Param.GA_SCREEN_LOGIN);

		mAdapter = new LoginFragmentAdapter(getSupportFragmentManager());

		mPager = (ViewPager) findViewById(R.id.pager);
		mPager.setAdapter(mAdapter);
		mPager.setOffscreenPageLimit(2);
	}

	class LoginFragmentAdapter extends FragmentPagerAdapter {

		public LoginFragmentAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			if (position == 0)
				return new NormalLoginFragment();
			else
				return new ParanoidLoginFragment();
		}

		@Override
		public int getCount() {
			return tabs.length;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return tabs[position];
		}
	}

	public void changePage(View v) {
		if (mPager.getCurrentItem() == 0)
			mPager.setCurrentItem(1);
		else
			mPager.setCurrentItem(0);
	}

}
