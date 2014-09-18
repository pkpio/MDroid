package in.co.praveenkumar.mdroid.fragment;

import in.co.praveenkumar.mdroid.apis.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class TutorialFragment extends Fragment {
	int index = 0;

	/**
	 * Index of the tutorial page to be shown
	 * 
	 * @param index
	 */
	public TutorialFragment(int index) {
		this.index = index;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView;
		switch (index) {
		case 0:
			rootView = inflater.inflate(R.layout.frag_tutorial_page0,
					container, false);
			break;
		case 1:
			rootView = inflater.inflate(R.layout.frag_tutorial_page1,
					container, false);
			break;

		default:
			rootView = inflater.inflate(R.layout.frag_tutorial_page0,
					container, false);
			break;
		}

		return rootView;
	}

}
