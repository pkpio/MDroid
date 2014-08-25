package in.co.praveenkumar.mdroid.adapter;

import in.co.praveenkumar.mdroid.apis.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class TabsPagerAdapter extends FragmentPagerAdapter {

	public TabsPagerAdapter(FragmentManager fm) {
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

	// Normal login fragment
	public class NormalLoginFragment extends Fragment {

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {

			View rootView = inflater.inflate(R.layout.normal_login, container,
					false);

			return rootView;
		}
	}

	// Paranoid login fragment
	public class ParanoidLoginFragment extends Fragment {

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {

			View rootView = inflater.inflate(R.layout.paranoid_login,
					container, false);

			return rootView;
		}
	}

}
