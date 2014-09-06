package in.co.praveenkumar.mdroid.activity;

import in.co.praveenkumar.mdroid.adapter.CourseContentTabsAdapter;
import in.co.praveenkumar.mdroid.adapter.NavigationDrawer;
import in.co.praveenkumar.mdroid.apis.R;
import in.co.praveenkumar.mdroid.helper.ActionBarTabs;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

public class CourseContentActivity extends NavigationDrawer implements
		ActionBar.TabListener {

	private ViewPager viewPager;
	private CourseContentTabsAdapter mAdapter;
	private ActionBar actionBar;
	private String[] tabs = { "Contents", "Forums", "Calendar" };

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_course_contents);
		setUpDrawer();

		Bundle extras = getIntent().getExtras();
		Long coursedbid = extras.getLong("coursedbid");
		int courseid = extras.getInt("courseid");
		// Initialization
		viewPager = (ViewPager) findViewById(R.id.course_content_pager);
		viewPager.setOffscreenPageLimit(3);
		actionBar = getActionBar();
		mAdapter = new CourseContentTabsAdapter(getSupportFragmentManager(),
				courseid, coursedbid);
		viewPager.setAdapter(mAdapter);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		ActionBarTabs.setHasEmbeddedTabs(actionBar, false);

		// Adding Tabs
		for (String tab_name : tabs) {
			actionBar.addTab(actionBar.newTab().setText(tab_name)
					.setTabListener(this));
		}

		/**
		 * on swiping the viewpager make respective tab selected
		 * */
		viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				actionBar.setSelectedNavigationItem(position);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		// on tab selected. Show respected fragment view
		viewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
	}

}
