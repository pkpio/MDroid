package in.co.praveenkumar.mdroid.activity;

import in.co.praveenkumar.R;
import in.co.praveenkumar.mdroid.fragment.CalenderFragment;
import in.co.praveenkumar.mdroid.fragment.ContentFragment;
import in.co.praveenkumar.mdroid.fragment.ForumFragment;
import in.co.praveenkumar.mdroid.fragment.ParticipantFragment;
import in.co.praveenkumar.mdroid.helper.ApplicationClass;
import in.co.praveenkumar.mdroid.helper.Param;
import in.co.praveenkumar.mdroid.helper.SessionSetting;
import in.co.praveenkumar.mdroid.model.MoodleCourse;
import in.co.praveenkumar.mdroid.view.SlidingTabLayout;

import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.Toast;

public class CourseContentActivity extends BaseNavigationActivity {
	private int courseid;
	private ViewPager viewPager;
	private static final String[] TABS = { "Contents", "Forums", "Calendar",
			"Participants" };

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_course_contents);
		setUpDrawer();

		// Send a tracker
		((ApplicationClass) getApplication())
				.sendScreen(Param.GA_SCREEN_CONTENT);

		Bundle extras = getIntent().getExtras();
		courseid = extras.getInt("courseid");

		// Get course details
		SessionSetting session = new SessionSetting(this);
		List<MoodleCourse> dbCourses = MoodleCourse.find(MoodleCourse.class,
				"site = ? and courseid = ?", session.getCurrentSiteId() + "",
				courseid + "");
		if (dbCourses == null || dbCourses.size() == 0) {
			Toast.makeText(this, "Course not found in database!",
					Toast.LENGTH_LONG).show();
			return;
		}
		MoodleCourse mCourse = dbCourses.get(0);

		getSupportActionBar().setTitle(mCourse.getFullname());
		getSupportActionBar().setIcon(R.drawable.icon_school);

		FragmentPagerAdapter mAdapter = new CourseContentTabsAdapter(
				getSupportFragmentManager());

		viewPager = (ViewPager) findViewById(R.id.course_content_pager);
		viewPager.setOffscreenPageLimit(TABS.length);
		viewPager.setAdapter(mAdapter);

		SlidingTabLayout mSlidingTabLayout = (SlidingTabLayout) findViewById(R.id.sliding_tabs);
		mSlidingTabLayout.setViewPager(viewPager);
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
				return new ContentFragment(courseid);
			case 1:
				// Course Forum
				return new ForumFragment(courseid);
			case 2:
				// Course Calendar
				return new CalenderFragment(courseid);
			case 3:
				// Course Participants
				return new ParticipantFragment(courseid);
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