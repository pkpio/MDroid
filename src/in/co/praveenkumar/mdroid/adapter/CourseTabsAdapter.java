package in.co.praveenkumar.mdroid.adapter;

import in.co.praveenkumar.mdroid.fragment.Course;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class CourseTabsAdapter extends FragmentPagerAdapter {

	public CourseTabsAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int index) {

		switch (index) {
		case 0:
			// List all courses
			return new Course();
		case 1:
			// List only user courses
			return new Course(Course.TYPE_USER_COURSES);
		case 2:
			// List only fav courses
			return new Course(Course.TYPE_FAV_COURSES);
		}
		return null;
	}

	@Override
	public int getCount() {
		// 3 tabs
		return 3;
	}

}
