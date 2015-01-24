package in.co.praveenkumar.mdroid.fragment;

import in.co.praveenkumar.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class TutorialFragment extends Fragment {
	public static final int TUTORIAL_PAGE_COUNT = 5;
	int index = 0;

	/**
	 * Index of the tutorial page to be shown
	 * 
	 * @param index
	 */
	public TutorialFragment(int index) {
		this.index = index;
	}

	public TutorialFragment() {
		this.index = TUTORIAL_PAGE_COUNT;
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
		case 2:
			rootView = inflater.inflate(R.layout.frag_tutorial_page2,
					container, false);
			break;
		case 3:
			rootView = inflater.inflate(R.layout.frag_tutorial_page3,
					container, false);
			break;
		case 4:
			rootView = inflater.inflate(R.layout.frag_tutorial_page4,
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
