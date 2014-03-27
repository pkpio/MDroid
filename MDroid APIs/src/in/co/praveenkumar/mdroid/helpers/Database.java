package in.co.praveenkumar.mdroid.helpers;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

public class Database {
	private final String DEBUG_TAG = "DATABASE";
	private static final String APP_SHARED_PREFS = "MDROID_PREFERENCES";
	private SharedPreferences appSharedPrefs;
	private Editor prefsEditor;

	// Preferences
	public Database(Context context) {
		this.appSharedPrefs = context.getSharedPreferences(APP_SHARED_PREFS,
				Activity.MODE_PRIVATE);
		this.prefsEditor = appSharedPrefs.edit();
	}

	public String getmUrl() {
		return appSharedPrefs.getString("ldap", "");
	}

	public void setmUrl(String mUrl) {
		Log.d(DEBUG_TAG, "mUrl updated");
		prefsEditor.putString("mUrl", mUrl);
		prefsEditor.commit();
	}

}
