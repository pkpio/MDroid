package in.co.praveenkumar.mdroid.fragment;

import in.co.praveenkumar.R;
import in.co.praveenkumar.mdroid.activity.CourseActivity;
import in.co.praveenkumar.mdroid.helper.AppInterface.DonationInterface;
import in.co.praveenkumar.mdroid.helper.Param;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.startapp.android.publish.Ad;
import com.startapp.android.publish.AdDisplayListener;
import com.startapp.android.publish.AdEventListener;
import com.startapp.android.publish.StartAppAd;
import com.startapp.android.publish.StartAppSDK;

public class AdFragment extends Fragment {
	final String DEBUG_TAG = "AdFragment";
	AdView mAdView;
	StartAppAd startAppAd;
	DonationInterface donation;
	Context context;
	SharedPreferences settings;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_ad, container, false);
	}

	@Override
	public void onAttach(Activity a) {
		super.onAttach(a);
		context = getActivity();

		try {
			this.donation = (DonationInterface) a;
		} catch (ClassCastException e) {
			e.printStackTrace();
			Log.d(DEBUG_TAG, a.toString()
					+ " did not implement ForumIdInterface.");
		}
		// StartApp Init
		StartAppSDK.init(context, Param.STARTAPP_DEV_ID, Param.STARTAPP_APP_ID,
				true);
		startAppAd = new StartAppAd(context);

		// Setup Shared preferences
		settings = PreferenceManager.getDefaultSharedPreferences(context);
	}

	@Override
	public void onActivityCreated(Bundle bundle) {
		super.onActivityCreated(bundle);
		mAdView = (AdView) getView().findViewById(R.id.adView);

		// Hide ads if he is a pro user
		if (donation != null && donation.isProUser() || Param.hideAdsForSession) {
			mAdView.setVisibility(AdView.GONE);
			return;
		}
		loadAds();
	}

	private void loadAds() {
		mAdView.setVisibility(AdView.VISIBLE);
		AdRequest adRequest = new AdRequest.Builder().addTestDevice(
				"B3EEABB8EE11C2BE770B684D95219ECB").build();
		mAdView.loadAd(adRequest);

		// Show a startApp interstitial - Ad shown based on policies
		startAppAd.loadAd(new StartAppAdEventListener());
	}

	/** Called when leaving the activity */
	@Override
	public void onPause() {
		if (mAdView != null) {
			mAdView.pause();
		}
		super.onPause();
	}

	/** Called when returning to the activity */
	@Override
	public void onResume() {
		super.onResume();
		if (mAdView != null) {
			mAdView.resume();

			// If Prouser, just skip settings check.
			if (donation != null && donation.isProUser())
				return;

			// Ads based on setting update
			if (Param.hideAdsForSession)
				mAdView.setVisibility(AdView.GONE);
			else
				loadAds();
		}
	}

	/** Called before the activity is destroyed */
	@Override
	public void onDestroy() {
		if (mAdView != null) {
			mAdView.destroy();
		}
		super.onDestroy();
	}

	/**
	 * StartApp listeners custom implementation. <br/>
	 * We implemented some custom policies on when to show an Ad.
	 */
	class StartAppAdEventListener implements AdEventListener {

		@Override
		public void onFailedToReceiveAd(Ad arg0) {
		}

		@Override
		public void onReceiveAd(Ad arg0) {
			/**
			 * Show Ad only if:
			 * 
			 * 1. There is sufficient time gap with last shown <br/>
			 * 2. The Activity is not CourseActivity
			 */
			if (context.getClass().getName()
					.contentEquals(CourseActivity.class.getName()))
				return;

			long now = System.currentTimeMillis();
			long last = settings.getLong("startapp_last_served", now
					- Param.STARTAPP_INTERSTITIAL_MAX_FREQ);

			if (now - last >= Param.STARTAPP_INTERSTITIAL_MAX_FREQ)
				startAppAd.showAd(new StartAppAdDisplayListener());

		}
	}

	class StartAppAdDisplayListener implements AdDisplayListener {

		@Override
		public void adClicked(Ad arg0) {
		}

		@Override
		public void adDisplayed(Ad arg0) {
			// Set last ad shown time
			SharedPreferences.Editor editor = settings.edit();
			editor.putLong("startapp_last_served", System.currentTimeMillis());
			editor.commit();
		}

		@Override
		public void adHidden(Ad arg0) {
		}
	}

}
