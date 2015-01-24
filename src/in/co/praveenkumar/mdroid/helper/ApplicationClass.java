package in.co.praveenkumar.mdroid.helper;

import in.co.praveenkumar.R;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
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
		// analytics.getLogger().setLogLevel(LogLevel.VERBOSE);
		return mTracker;
	}

	/**
	 * Send a Google Analytics screen hit
	 * 
	 * @param ScreenName
	 *            Name as it appears on the Analytics page
	 */
	public void sendScreen(String ScreenName) {
		if (mTracker == null)
			getTracker();

		// Setup a tracker
		Tracker t = mTracker;

		// Set screen name.
		t.setScreenName(ScreenName);

		// Send a screen view.
		t.send(new HitBuilders.AppViewBuilder().build());
	}

	/**
	 * Send a Google Analytics event hit
	 * 
	 * @param ScreenName
	 *            Name as it appears on the Analytics page
	 */
	public void sendEvent(String EventCategory, String EventAction) {
		if (mTracker == null)
			getTracker();

		// Setup a tracker
		Tracker t = mTracker;

		// Send a event hit.
		t.send(new HitBuilders.EventBuilder().setCategory(EventCategory)
				.setAction(EventAction).build());
	}
}
