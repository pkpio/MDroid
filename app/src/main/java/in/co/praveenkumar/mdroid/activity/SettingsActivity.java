package in.co.praveenkumar.mdroid.activity;

import in.co.praveenkumar.R;
import in.co.praveenkumar.mdroid.dialog.LogoutDialog;
import in.co.praveenkumar.mdroid.helper.ApplicationClass;
import in.co.praveenkumar.mdroid.helper.Param;
import in.co.praveenkumar.mdroid.helper.SessionSetting;
import in.co.praveenkumar.mdroid.service.ScheduleReceiver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;

public class SettingsActivity extends PreferenceActivity implements
		OnPreferenceClickListener, OnPreferenceChangeListener {
	SessionSetting session;
	BillingProcessor billing;
	SharedPreferences settings;

	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle("Settings");

		// Send a tracker
		((ApplicationClass) getApplication())
				.sendScreen(Param.GA_SCREEN_SETTING);

		// Setup billing
		session = new SessionSetting(this);
		settings = PreferenceManager.getDefaultSharedPreferences(this);
		billing = new BillingProcessor(this, Param.BILLING_LICENSE_KEY,
				new BillingProcessor.IBillingHandler() {
					@Override
					public void onProductPurchased(String productId,
							TransactionDetails details) {
						Toast.makeText(getApplicationContext(),
								"You purchased this already!",
								Toast.LENGTH_LONG).show();
					}

					@Override
					public void onBillingError(int errorCode, Throwable error) {
						Toast.makeText(getApplicationContext(),
								"Purchase failed! Please try again!",
								Toast.LENGTH_LONG).show();
					}

					@Override
					public void onBillingInitialized() {
					}

					@Override
					public void onPurchaseHistoryRestored() {
					}
				});

		// Set signature & adsPref in prefs to current account value
		SharedPreferences.Editor editor = settings.edit();
		editor.putString("messagingSignature", session.getMessageSignature());
		editor.putBoolean("hideAds", Param.hideAdsForSession);
		editor.commit();

		/*
		 * Note: Inflate xml after setting signature value or the new value
		 * won't reflect in the inflated setting
		 */
		addPreferencesFromResource(R.xml.preferences);

		// Enable donate only preferences
		if (isProUser()) {
			findPreference("messagingSignature").setEnabled(true);
			findPreference("notifications").setEnabled(true);
		}

		// Add preference click / change listeners
		findPreference("logout").setOnPreferenceClickListener(this);
		findPreference("messagingSignature")
				.setOnPreferenceChangeListener(this);
		findPreference("hideAds").setOnPreferenceChangeListener(this);

		findPreference("notifications").setOnPreferenceChangeListener(this);
		findPreference("notification_frequency").setOnPreferenceChangeListener(
				this);

		findPreference("help").setOnPreferenceClickListener(this);
		findPreference("privacyPolicy").setOnPreferenceClickListener(this);
		findPreference("tutorial").setOnPreferenceClickListener(this);

		findPreference("aboutMDroid").setOnPreferenceClickListener(this);
		findPreference("aboutDev").setOnPreferenceClickListener(this);
		findPreference("licenses").setOnPreferenceClickListener(this);
		findPreference("translate").setOnPreferenceClickListener(this);
	}

	@Override
	public boolean onPreferenceClick(Preference preference) {
		String key = preference.getKey();

		if (key.contentEquals("logout")) {
			LogoutDialog lod = new LogoutDialog(this,
					new SessionSetting(this).getCurrentSiteId());
			lod.show();
		}

		if (key.contentEquals("help")) {
			Intent browserIntent = new Intent(Intent.ACTION_VIEW,
					Uri.parse("http://mdroid.praveenkumar.co.in/#!faq.md"));
			startActivity(browserIntent);
		}

		if (key.contentEquals("privacyPolicy")) {
			Intent browserIntent = new Intent(
					Intent.ACTION_VIEW,
					Uri.parse("http://mdroid.praveenkumar.co.in/#!privacy-policy.md"));
			startActivity(browserIntent);
		}

		if (key.contentEquals("tutorial")) {
			Intent tutorialIntent = new Intent(this, TutorialActivity.class);
			tutorialIntent.putExtra("explicitCall", true);
			this.startActivity(tutorialIntent);
		}

		if (key.contentEquals("aboutMDroid")) {
			Intent browserIntent = new Intent(Intent.ACTION_VIEW,
					Uri.parse("http://mdroid.praveenkumar.co.in"));
			startActivity(browserIntent);
		}

		if (key.contentEquals("aboutDev")) {
			Intent browserIntent = new Intent(Intent.ACTION_VIEW,
					Uri.parse("https://github.com/praveendath92"));
			startActivity(browserIntent);
		}

		if (key.contentEquals("translate")) {
			Intent browserIntent = new Intent(Intent.ACTION_VIEW,
					Uri.parse("https://crowdin.com/project/mdroid"));
			startActivity(browserIntent);
		}

		if (key.contentEquals("licenses")) {
			Intent i = new Intent(this, AppBrowserActivity.class);
			i.putExtra("url", "file:///android_asset/os_licenses.html");
			i.putExtra("title", "Open Source Licences");
			this.startActivity(i);
		}

		return false;
	}

	@Override
	public boolean onPreferenceChange(Preference preference, Object newValue) {
		String key = preference.getKey();

		if (key.contentEquals("hideAds")) {
			int hideCount = session.getAdsHideCount();

			// Deny if he at max hide count already
			if (hideCount >= Param.maxAdsHideCount && !Param.hideAdsForSession) {
				Toast.makeText(
						this,
						"You have hidden ads more than "
								+ Param.maxAdsHideCount + " times already!",
						Toast.LENGTH_LONG).show();
				return false;
			}

			// Send a tracker event
			((ApplicationClass) getApplication()).sendEvent(
					Param.GA_EVENT_CAT_SETTING, Param.GA_EVENT_SETTING_HIDEADS);

			// Increment count only if he is indenting to hide ads
			if (!Param.hideAdsForSession)
				session.setAdsHideCount(++hideCount);

			Param.hideAdsForSession = !Param.hideAdsForSession;
		}

		if (key.contentEquals("notifications")) {
			if (newValue.toString().equals("true"))
				ScheduleReceiver.scheduleService(this);
			else
				ScheduleReceiver.unscheduleService(this);
		}

		if (key.contentEquals("notification_frequency") && isProUser())
			ScheduleReceiver.rescheduleService(this);

		if (key.contentEquals("messagingSignature"))
			session.setMessageSignature(newValue.toString());

		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (!billing.handleActivityResult(requestCode, resultCode, data))
			super.onActivityResult(requestCode, resultCode, data);
	}

	private Boolean isProUser() {
		return billing.isPurchased(Param.BILLING_DONATION_PID)
				|| billing.isPurchased(Param.BILLING_FEATURE_NOTIFICATIONS_PID)
				|| billing.isPurchased(Param.BILLING_FEATURE_PARTICIPANTS_PID)
				|| billing.isPurchased(Param.BILLING_FEATURE_SEARCH_PID)
				|| billing.isPurchased(Param.BILLING_FEATURE_UPLOADS_PID);
	}

	@Override
	public void onDestroy() {
		if (billing != null)
			billing.release();
		super.onDestroy();
	}
}
