package in.co.praveenkumar.mdroid.adapter;

import in.co.praveenkumar.mdroid.fragment.NormalLogin;
import in.co.praveenkumar.mdroid.fragment.ParanoidLogin;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class LoginTabsAdapter extends FragmentPagerAdapter {

	public LoginTabsAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int index) {

		switch (index) {
		case 0:
			// Normal login fragment activity
			return new NormalLogin();
		case 1:
			// Paranoid login fragment activity
			return new ParanoidLogin();
		}

		return null;
	}

	@Override
	public int getCount() {
		// get item count - equal to number of tabs
		return 2;
	}

}
