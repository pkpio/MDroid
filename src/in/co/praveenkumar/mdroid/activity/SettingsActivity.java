package in.co.praveenkumar.mdroid.activity;

import in.co.praveenkumar.R;
import in.co.praveenkumar.mdroid.dialog.LogoutDialog;
import in.co.praveenkumar.mdroid.helper.Param;
import in.co.praveenkumar.mdroid.helper.SessionSetting;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;

public class SettingsActivity extends PreferenceActivity implements
		OnPreferenceClickListener, OnPreferenceChangeListener {
	SessionSetting session;

	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle("Settings");
		addPreferencesFromResource(R.xml.preferences);
		session = new SessionSetting(this);

		// Enable donate only preferences
		findPreference("messagingSignature").setEnabled(true);
		findPreference("messagingSignature").setSummary(
				Param.DefaultMessageSignature);
		findPreference("messagingSignature")
				.setOnPreferenceChangeListener(this);

		// Add preference click listeners
		findPreference("logout").setOnPreferenceClickListener(this);

		findPreference("help").setOnPreferenceClickListener(this);
		findPreference("privacyPolicy").setOnPreferenceClickListener(this);

		findPreference("aboutMDroid").setOnPreferenceClickListener(this);
		findPreference("aboutDev").setOnPreferenceClickListener(this);
		findPreference("licenses").setOnPreferenceClickListener(this);
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

		if (!key.contentEquals("messagingSignature"))
			return true;
		session.setMessageSignature(newValue.toString());

		return false;
	}

}
