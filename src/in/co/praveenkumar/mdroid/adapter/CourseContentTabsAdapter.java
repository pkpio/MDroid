package in.co.praveenkumar.mdroid.adapter;

import in.co.praveenkumar.mdroid.fragment.CalenderFragment;
import in.co.praveenkumar.mdroid.fragment.ContentFragment;
import in.co.praveenkumar.mdroid.fragment.ForumFragment;
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
	public int getCount() {
		// 3 tabs
		return 3;
	}

}
