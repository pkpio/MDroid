package in.co.praveenkumar.mdroid.adapter;

import in.co.praveenkumar.mdroid.fragment.ContentFragment;
import in.co.praveenkumar.mdroid.fragment.CourseFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class CourseContentTabsAdapter extends FragmentPagerAdapter {
	int courseid;
	long coursedbid;

	public CourseContentTabsAdapter(FragmentManager fm, int courseid,
			long coursedbid) {
		super(fm);
		this.coursedbid = coursedbid;
		this.courseid = courseid;
	}

	@Override
	public Fragment getItem(int index) {

		switch (index) {
		case 0:
			// List all courses
			return new ContentFragment(courseid, coursedbid);
		case 1:
			// List only user courses
			return new CourseFragment(CourseFragment.TYPE_USER_COURSES);
		case 2:
			// List only fav courses
			return new CourseFragment(CourseFragment.TYPE_FAV_COURSES);
		}
		return null;
	}

	@Override
	public int getCount() {
		// 3 tabs
		return 3;
	}

}
