package in.co.praveenkumar.mdroid.adapter;

import in.co.praveenkumar.mdroid.fragment.NormalLoginFragment;
import in.co.praveenkumar.mdroid.fragment.ParanoidLoginFragment;
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
			return new NormalLoginFragment();
		case 1:
			// Paranoid login fragment activity
			return new ParanoidLoginFragment();
		}

		return null;
	}

	@Override
	public int getCount() {
		// get item count - equal to number of tabs
		return 2;
	}

}
