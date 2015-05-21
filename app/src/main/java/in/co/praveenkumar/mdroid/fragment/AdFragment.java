package in.co.praveenkumar.mdroid.fragment;

import in.co.praveenkumar.R;
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

public class AdFragment extends Fragment {
	final String DEBUG_TAG = "AdFragment";
	AdView mAdView;
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

}
