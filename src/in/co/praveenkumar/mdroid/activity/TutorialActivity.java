package in.co.praveenkumar.mdroid.activity;

import in.co.praveenkumar.mdroid.fragment.TutorialFragment;
import in.co.praveenkumar.mdroid.helper.SessionSetting;
import in.co.praveenkumar.mdroid.legacy.R;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.viewpagerindicator.CirclePageIndicator;
import com.viewpagerindicator.PageIndicator;

public class TutorialActivity extends FragmentActivity {
	TutorialFragmentAdapter mAdapter;
	ViewPager mPager;
	PageIndicator mIndicator;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tutorial);

		// Skip login if user is logged in already
		SessionSetting session = new SessionSetting(this);
		if (session.isTutored()) {
			Intent i = new Intent(this, LoginActivity.class);
			this.startActivity(i);
			return;
		}

		mAdapter = new TutorialFragmentAdapter(getSupportFragmentManager());

		mPager = (ViewPager) findViewById(R.id.pager);
		mPager.setAdapter(mAdapter);
		mPager.setOffscreenPageLimit(TutorialFragment.TUTORIAL_PAGE_COUNT);

		mIndicator = (CirclePageIndicator) findViewById(R.id.indicator);
		mIndicator.setViewPager(mPager);
	}

	@Override
	protected void onResume() {
		super.onResume();
		mAdapter = new TutorialFragmentAdapter(getSupportFragmentManager());

		mPager = (ViewPager) findViewById(R.id.pager);
		mPager.setAdapter(mAdapter);
		mPager.setOffscreenPageLimit(TutorialFragment.TUTORIAL_PAGE_COUNT);

		mIndicator = (CirclePageIndicator) findViewById(R.id.indicator);
		mIndicator.setViewPager(mPager);
	}

	class TutorialFragmentAdapter extends FragmentPagerAdapter {
		private int mCount = TutorialFragment.TUTORIAL_PAGE_COUNT;

		public TutorialFragmentAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			return new TutorialFragment(position);
		}

		@Override
		public int getCount() {
			return mCount;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return "Tutorial";
		}

		public void setCount(int count) {
			if (count > 0 && count <= 10) {
				mCount = count;
				notifyDataSetChanged();
			}
		}
	}

	public void openLoginPage(View v) {
		// Skip tutorial from next time
		new SessionSetting(this).setTutored(true);
		startActivity(new Intent(this, LoginActivity.class));
	}
}
