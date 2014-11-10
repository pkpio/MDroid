package in.co.praveenkumar.mdroid.activity;

import in.co.praveenkumar.mdroid.fragment.CalenderFragment;
import in.co.praveenkumar.mdroid.fragment.ContentFragment;
import in.co.praveenkumar.mdroid.fragment.ForumFragment;
import in.co.praveenkumar.R;
import in.co.praveenkumar.mdroid.moodlemodel.MoodleCourse;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.viewpagerindicator.TabPageIndicator;

public class CourseContentActivity extends BaseNavigationActivity {
	private long coursedbid;
	private int courseid;
	private ViewPager viewPager;
	private static final String[] TABS = { "Contents", "Forums", "Calendar" };

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_course_contents);
		setUpDrawer();

		Bundle extras = getIntent().getExtras();
		coursedbid = extras.getLong("coursedbid");
		courseid = extras.getInt("courseid");
		MoodleCourse mCourse = MoodleCourse.findById(MoodleCourse.class,
				coursedbid);
		getSupportActionBar().setTitle(mCourse.getFullname());
		getSupportActionBar().setIcon(R.drawable.icon_school);

		FragmentPagerAdapter mAdapter = new CourseContentTabsAdapter(
				getSupportFragmentManager());

		viewPager = (ViewPager) findViewById(R.id.course_content_pager);
		viewPager.setOffscreenPageLimit(3);
		viewPager.setAdapter(mAdapter);

		TabPageIndicator indicator = (TabPageIndicator) findViewById(R.id.indicator);
		indicator.setViewPager(viewPager);
	}

	class CourseContentTabsAdapter extends FragmentPagerAdapter {
		public CourseContentTabsAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			switch (position) {
			case 0:
				// Course Content
				return new ContentFragment(courseid, coursedbid);
			case 1:
				// Course Forum
				return new ForumFragment(courseid);
			case 2:
				// Course Calendar
				return new CalenderFragment(courseid);
			}
			return null;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return TABS[position];
		}

		@Override
		public int getCount() {
			return TABS.length;
		}
	}

}