/**************************************************************
 * Author: 	Praveen Kumar Pendyala
 * Project: MDroid
 * Created: 27th May, 2014.
 * License: http://creativecommons.org/licenses/by-nc-sa/3.0/
 **************************************************************/

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
		return appSharedPrefs.getString("mUrl", "");
	}

	public void setmUrl(String mUrl) {
		Log.d(DEBUG_TAG, "mUrl updated");
		prefsEditor.putString("mUrl", mUrl);
		prefsEditor.commit();
	}

	public String getToken() {
		return appSharedPrefs.getString("token", "");
	}

	public void setToken(String token) {
		Log.d(DEBUG_TAG, "token updated");
		prefsEditor.putString("token", token);
		prefsEditor.commit();
	}

	public String getUserFullname() {
		return appSharedPrefs.getString("fullname", "NaN");
	}

	public void setUserFullname(String fullname) {
		Log.d(DEBUG_TAG, "fullname updated");
		prefsEditor.putString("fullname", fullname);
		prefsEditor.commit();
	}

	public String getUserid() {
		return appSharedPrefs.getString("userid", "1");
	}

	public void setUserid(String userid) {
		Log.d(DEBUG_TAG, "userid updated");
		prefsEditor.putString("userid", userid);
		prefsEditor.commit();
	}

}
