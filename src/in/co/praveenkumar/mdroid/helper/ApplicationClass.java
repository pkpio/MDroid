package in.co.praveenkumar.mdroid.helper;

import in.co.praveenkumar.R;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.orm.SugarApp;

public class ApplicationClass extends SugarApp {

	public ApplicationClass() {
		super();
	}

	Tracker mTracker;

	synchronized public Tracker getTracker() {
		GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
		mTracker = analytics.newTracker(R.xml.app_tracker);
		return mTracker;
	}
}
