package in.co.praveenkumar;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class AppPreferences {
	private static final String APP_SHARED_PREFS = "sosmate_preferences";
	private SharedPreferences appSharedPrefs;
	private Editor prefsEditor;

	// Preferences
	public AppPreferences(Context context) {
		this.appSharedPrefs = context.getSharedPreferences(APP_SHARED_PREFS,
				Activity.MODE_PRIVATE);
		this.prefsEditor = appSharedPrefs.edit();
	}

	public String getPrefs(String name) {
		String value = "";
		value = appSharedPrefs.getString(name,
				"I'm in great danger! Please help me! My location is:");
		return value;
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