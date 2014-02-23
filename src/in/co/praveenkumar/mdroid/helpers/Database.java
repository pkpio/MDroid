/*
 * Author: 	Praveen Kumar Pendyala
 * Project: MDroid
 * Created:	28-12-2013
 * 
 * © 2013, Praveen Kumar Pendyala. 
 * Licensed under the Creative Commons Attribution-NonCommercial-ShareAlike 
 * 3.0 Unported license, http://creativecommons.org/licenses/by-nc-sa/3.0/ 
 * 
 * This is a part of the MDroid project. You may use the contents of this file
 * or the project only if you comply with license of the project, available at the 
 * Github repo: https://github.com/praveendath92/MDroid/blob/master/README.md
 * 
 */

package in.co.praveenkumar.mdroid.helpers;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

public class Database {
	private final String DEBUG_TAG = "MDROID PREFS DATABASE";
	private static final String APP_SHARED_PREFS = "MDROID_PREFERENCES";
	private SharedPreferences appSharedPrefs;
	private Editor prefsEditor;

	// Preferences
	public Database(Context context) {
		this.appSharedPrefs = context.getSharedPreferences(APP_SHARED_PREFS,
				Activity.MODE_PRIVATE);
		this.prefsEditor = appSharedPrefs.edit();
	}

	public String getLDAP() {
		return appSharedPrefs.getString("ldap", "");
	}

	public void setLDAP(String ldap) {
		prefsEditor.putString("ldap", ldap);
		prefsEditor.commit();
	}

	public String getPswd() {
		return appSharedPrefs.getString("pswd", "");
	}

	public void setPswd(String pswd) {
		prefsEditor.putString("pswd", pswd);
		prefsEditor.commit();
	}

	public String getURL() {
		return appSharedPrefs.getString("url", "http://moodle.iitb.ac.in");
	}

	public void setURL(String url) {
		prefsEditor.putString("url", url);
		prefsEditor.commit();
	}

	public int getAutoSaveState() {
		// 0 - only uname; 1 - both; 2 - none
		return appSharedPrefs.getInt("autosave", 1);
	}

	public void setAutoSaveState(int state) {
		// 0 - only uname; 1 - both; 2 - none
		prefsEditor.putInt("autosave", state);
		prefsEditor.commit();
	}

	public Boolean getAutoLoginState() {
		// 0 - false; 1 - true;
		return appSharedPrefs.getBoolean("autologin", false);
	}

	public void setAutoLoginState(Boolean state) {
		// 0 - false; 1 - true;
		prefsEditor.putBoolean("autologin", state);
		prefsEditor.commit();
	}

	public Boolean getNotificationsState() {
		// 0 - false; 1 - true;
		return appSharedPrefs.getBoolean("notifications", true);
	}

	public void setNotificationsState(Boolean state) {
		// 0 - false; 1 - true;
		prefsEditor.putBoolean("notifications", state);
		prefsEditor.commit();
	}

	public int getServiceFrequency() {
		return appSharedPrefs.getInt("servicefrequency", 2);
	}

	public void setServiceFrequency(int value) {
		Log.d(DEBUG_TAG, "setting frequency : " + value);
		prefsEditor.putInt("servicefrequency", value);
		prefsEditor.commit();
	}

	public int getNotifedCount() {
		return appSharedPrefs.getInt("Notifedcount", 0);
	}

	public void setNotifedCount(int value) {
		Log.d(DEBUG_TAG, "setting notified count: " + value);
		prefsEditor.putInt("Notifedcount", value);
		prefsEditor.commit();
	}

	public String getLastChecked() {
		return appSharedPrefs.getString("lastServiced", "NaN");
	}

	public void setLastChecked(String date) {
		Log.d(DEBUG_TAG, "last serviced : " + date);
		prefsEditor.putString("lastServiced", date);
		prefsEditor.commit();
	}
}
