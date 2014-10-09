package in.co.praveenkumar.mdroid.activity;

import in.co.praveenkumar.mdroid.legacy.R;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;

public class SettingsActivity extends PreferenceActivity implements
		OnPreferenceClickListener {

	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle("Settings");
		addPreferencesFromResource(R.xml.preferences);

		// Add preference click listeners
		findPreference("logout").setOnPreferenceClickListener(this);
		findPreference("resetCourseData").setOnPreferenceClickListener(this);
		findPreference("resetForumData").setOnPreferenceClickListener(this);
		findPreference("resetCalendarData").setOnPreferenceClickListener(this);

		findPreference("help").setOnPreferenceClickListener(this);
		findPreference("privacyPolicy").setOnPreferenceClickListener(this);

		findPreference("aboutMDroid").setOnPreferenceClickListener(this);
		findPreference("aboutDev").setOnPreferenceClickListener(this);
		findPreference("licenses").setOnPreferenceClickListener(this);
	}

	@Override
	public boolean onPreferenceClick(Preference preference) {
		String key = preference.getKey();

		if (key.contentEquals("aboutMDroid")) {
			Intent browserIntent = new Intent(Intent.ACTION_VIEW,
					Uri.parse("http://mdroid.praveenkumar.co.in"));
			startActivity(browserIntent);
		}

		if (key.contentEquals("aboutDev")) {
			Intent browserIntent = new Intent(Intent.ACTION_VIEW,
					Uri.parse("http://praveenkumar.co.in"));
			startActivity(browserIntent);
		}

		if (key.contentEquals("licenses")) {
			Intent i = new Intent(this, AppBrowserActivity.class);
			i.putExtra("url", "file:///android_asset/os_licenses.html");
			this.startActivity(i);
		}

		System.out.println(preference.getKey());
		return false;
	}

}
