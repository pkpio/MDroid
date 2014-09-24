package in.co.praveenkumar.mdroid.helper;

import in.co.praveenkumar.mdroid.moodlemodel.MoodleSiteInfo;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

/**
 * All the session parameters for the current app session are available here.
 * Params included are token, mUrl, DebugMode etc.,.
 * 
 * @author praveen
 * 
 */
public class SessionSetting {
	private String token;
	private String mUrl;
	private long currentSiteId;
	private MoodleSiteInfo siteInfo;
	public static final long NO_SITE_ID = 999;

	private final String APP_SHARED_PREFS = "MDROID_PREFERENCES";
	private SharedPreferences appSharedPrefs;
	private Editor prefsEditor;
	private String DEBUG_TAG = "MDROID_PREFERENCES";

	// Preferences
	public SessionSetting(Context context) {
		this.appSharedPrefs = context.getSharedPreferences(APP_SHARED_PREFS,
				Activity.MODE_PRIVATE);
		this.prefsEditor = appSharedPrefs.edit();
		setCurrentValues();
	}

	private void setCurrentValues() {
		currentSiteId = appSharedPrefs.getLong("currentSiteId", NO_SITE_ID);
		siteInfo = MoodleSiteInfo.findById(MoodleSiteInfo.class, currentSiteId);

		// If no site found. Get the 1st site in database.
		if (siteInfo == null) {
			List<MoodleSiteInfo> sites = MoodleSiteInfo
					.listAll(MoodleSiteInfo.class);
			// Check if at least one site is present in database
			if (sites.size() != 0) {
				siteInfo = sites.get(0);

				// Save this as current site for all future references.
				// Do NOT use setCurrentSiteId call as it causes a loop.
				prefsEditor.putLong("currentSiteId", siteInfo.getId());
				prefsEditor.commit();
			}
		}

		// set other fields if siteInfo was set
		if (siteInfo != null) {
			mUrl = siteInfo.getSiteurl();
			token = siteInfo.getToken();
			currentSiteId = siteInfo.getId();
		} else {
			currentSiteId = NO_SITE_ID;
		}
	}

	/**
	 * Get the token for current site in use. This is just a convenience method.
	 * The token can also be retrieved from Sugar database using siteid or
	 * siteinfo object
	 * 
	 * @return
	 */
	public String getToken() {
		return token;
	}

	/**
	 * Get the url for current site in use. This is just a convenience method.
	 * The url can also be retrieved from Sugar database using siteid or
	 * siteinfo object
	 * 
	 * @return
	 */
	public String getmUrl() {
		return mUrl;
	}

	/**
	 * Get the debug mode status. If true, it allows more detailed status
	 * reporting.
	 * 
	 * @return
	 */
	public Boolean getDebugMode() {
		return appSharedPrefs.getBoolean("Debugmode", false);
	}

	/**
	 * Set the debug mode status. If true, it allows more detailed status
	 * reporting.
	 * 
	 * @param debugMode
	 */
	public void setDebugMode(Boolean debugMode) {
		Log.d(DEBUG_TAG, "Debugmode updated");
		prefsEditor.putBoolean("Debugmode", debugMode);
		prefsEditor.commit();
	}

	/**
	 * Get the id of the current site
	 * 
	 * @return
	 */
	public long getCurrentSiteId() {
		return currentSiteId;
	}

	/**
	 * Set the id of the current site. Useful on new login or when user requests
	 * account switching
	 * 
	 * @param currentSiteId
	 */
	public void setCurrentSiteId(long currentSiteId) {
		Log.d(DEBUG_TAG, "fullname updated");
		prefsEditor.putLong("currentSiteId", currentSiteId);
		prefsEditor.commit();
		setCurrentValues();
	}

	/**
	 * Get the siteInfo object of the current site in use. This is just a
	 * convenience method. The object can also be retrieved from Sugar database
	 * using siteid
	 * 
	 * @return
	 */
	public MoodleSiteInfo getSiteInfo() {
		return siteInfo;
	}

	/**
	 * Get whether the user has gone through the app tutorial.
	 * 
	 * @return TutoredStatus
	 */
	public Boolean isTutored() {
		return appSharedPrefs.getBoolean("isTutored", false);
	}

	/**
	 * Set whether the user has gone through the app tutorial. If true, tutorial
	 * won't be shown to the user again.
	 * 
	 * @param TutoredStatus
	 */
	public void setTutored(Boolean TutoredStatus) {
		Log.d(DEBUG_TAG, "isTutored updated");
		prefsEditor.putBoolean("isTutored", TutoredStatus);
		prefsEditor.commit();
	}
}
