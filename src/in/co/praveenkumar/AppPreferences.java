package in.co.praveenkumar;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class AppPreferences {
	private static final String APP_SHARED_PREFS = "mdroid_preferences";
	private SharedPreferences appSharedPrefs;
	private Editor prefsEditor;

	// Preferences
	public AppPreferences(Context context) {
		this.appSharedPrefs = context.getSharedPreferences(APP_SHARED_PREFS,
				Activity.MODE_PRIVATE);
		this.prefsEditor = appSharedPrefs.edit();
	}

	public int getIntPrefs(String name) {
		int value = appSharedPrefs.getInt(name, 0);
		return value;
	}

	public void saveIntPrefs(String prefName, int prefValue) {
		prefsEditor.putInt(prefName, prefValue);
		prefsEditor.commit();
	}
}