package in.co.praveenkumar.mdroid.playgames;

import in.co.praveenkumar.R;

import java.util.Calendar;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;

/**
 * For tracking all playGames related activity. All activity is tracked in a
 * local database and then updated to playGames on demand
 * 
 * @author praveen
 * 
 */
public class GameUnlocker {
	private final String SHARED_PREFS = "MDROID_GAME_PREFERENCES";
	private SharedPreferences appSharedPrefs;
	private Editor prefsEditor;
	private String DEBUG_TAG = "GameUnlocker";
	private Context context;

	/**
	 * Get an instance of game unlocker
	 * 
	 * @param context
	 */
	public GameUnlocker(Context context) {
		this.appSharedPrefs = context.getSharedPreferences(SHARED_PREFS,
				Activity.MODE_PRIVATE);
		this.prefsEditor = appSharedPrefs.edit();
		this.context = context;
	}

	/**
	 * Increment the number of times app has been used
	 */
	public void incrementUseCount() {
		Log.d(DEBUG_TAG, "Use count incremented");
		prefsEditor.putInt("usecount", getUseCount() + 1);
		prefsEditor.commit();
	}

	/**
	 * Update the maximum streak if needed
	 */
	public void updateMaxStreak() {
		Calendar c = Calendar.getInstance();
		int nowDay = c.get(Calendar.DATE);
		int nowMonth = c.get(Calendar.MONTH);
		int nowYear = c.get(Calendar.YEAR);

		c.setTimeInMillis(getLastPlayedTime());
		int lastDay = c.get(Calendar.DATE);
		int lastMonth = c.get(Calendar.MONTH);
		int lastYear = c.get(Calendar.YEAR);

		// Last played is today? Do nothing
		if (nowDay == lastDay && nowMonth == lastMonth && nowYear == lastYear) {
			Log.d(DEBUG_TAG, "Streak unchanged");
			setLastPlayedTime(); // This is to initialize last played
			return;
		}

		/**
		 * We need to check if last played is yesterday or some other day. Add
		 * 24 hours to last played. If the dates match - yesterday.
		 */
		c.setTimeInMillis(getLastPlayedTime() + 24 * 60 * 60 * 1000);
		lastDay = c.get(Calendar.DATE);
		lastMonth = c.get(Calendar.MONTH);
		lastYear = c.get(Calendar.YEAR);
		if (nowDay == lastDay && nowMonth == lastMonth && nowYear == lastYear) {
			Log.d(DEBUG_TAG, "Streak incremented " + getCurrentStreak());
			incrementCurrentStreak();
			setLastPlayedTime();
			if (getCurrentStreak() > getMaxStreak())
				incrementMaxStreak();
		} else {
			Log.d(DEBUG_TAG, "Streak reset");
			setLastPlayedTime();
			resetCurrentStreak();
		}
	}

	/**
	 * Uploads the acheivements to Google Play Games if signed in
	 * 
	 * @param mApiClient
	 *            GoogleApiClient
	 */
	public void publishAcheivements(GoogleApiClient mApiClient) {
		// if (!mApiClient.isConnected()) {
		// System.out.println("Not connected");
		// return;
		// }

		// Update use counts
		int countDiff = getUseCount() - getUploadedUseCount();
		System.out.println("Diff Count is:" + countDiff);
		Games.Achievements.increment(mApiClient,
				getString(R.string.achievement_use_5_times), countDiff);
		Games.Achievements.increment(mApiClient,
				getString(R.string.achievement_use_10_times), countDiff);
		Games.Achievements.increment(mApiClient,
				getString(R.string.achievement_use_15_times), countDiff);
		Games.Achievements.increment(mApiClient,
				getString(R.string.achievement_use_25_times), countDiff);
		Games.Achievements.increment(mApiClient,
				getString(R.string.achievement_use_35_times), countDiff);
		Games.Achievements.increment(mApiClient,
				getString(R.string.achievement_use_50_times), countDiff);
		Games.Achievements.increment(mApiClient,
				getString(R.string.achievement_use_75_times), countDiff);
		Games.Achievements.increment(mApiClient,
				getString(R.string.achievement_use_100_times), countDiff);
		updateUploadedUseCount();

		// Days streak unlocks
		int maxStreak = getMaxStreak();
		if (maxStreak > 1)
			Games.Achievements.unlock(mApiClient,
					getString(R.string.achievement_2_days_straight));
		if (maxStreak > 2)
			Games.Achievements.unlock(mApiClient,
					getString(R.string.achievement_3_days_straight));
		if (maxStreak > 3)
			Games.Achievements.unlock(mApiClient,
					getString(R.string.achievement_4_days_straight));
		if (maxStreak > 4)
			Games.Achievements.unlock(mApiClient,
					getString(R.string.achievement_5_days_straight));
		if (maxStreak > 5)
			Games.Achievements.unlock(mApiClient,
					getString(R.string.achievement_6_days_straight));
		if (maxStreak > 6)
			Games.Achievements.unlock(mApiClient,
					getString(R.string.achievement_7_days_straight));
		if (maxStreak > 7)
			Games.Achievements.unlock(mApiClient,
					getString(R.string.achievement_8_days_straight));
		if (maxStreak > 8)
			Games.Achievements.unlock(mApiClient,
					getString(R.string.achievement_9_days_straight));
		if (maxStreak > 9)
			Games.Achievements.unlock(mApiClient,
					getString(R.string.achievement_10_days_straight));
		if (maxStreak > 10)
			Games.Achievements.unlock(mApiClient,
					getString(R.string.achievement_11_days_straight));
		if (maxStreak > 11)
			Games.Achievements.unlock(mApiClient,
					getString(R.string.achievement_12_days_straight));
		if (maxStreak > 12)
			Games.Achievements.unlock(mApiClient,
					getString(R.string.achievement_13_days_straight));
	}

	/**
	 * 
	 * Priavte helper methods to realize above functionality
	 * 
	 */

	/**
	 * Return a string from resource id
	 */
	private String getString(int resourceid) {
		return context.getString(resourceid);
	}

	/**
	 * Returns the uploaded use count
	 */
	private int getUploadedUseCount() {
		return appSharedPrefs.getInt("uploadedusecount", 0);
	}

	/**
	 * Updates the uploaded use count to current use count
	 */
	private void updateUploadedUseCount() {
		prefsEditor.putInt("uploadedusecount", getUseCount());
		prefsEditor.commit();
	}

	/**
	 * Returns the number of times app has been used
	 */
	private int getUseCount() {
		return appSharedPrefs.getInt("usecount", 0);
	}

	/**
	 * Returns the number of days in the max game streak
	 */
	private int getMaxStreak() {
		return appSharedPrefs.getInt("maxstreak", 1);
	}

	/**
	 * Increment the number of days in the max. game streak
	 */
	private void incrementMaxStreak() {
		Log.d(DEBUG_TAG, "Max. Streak incremented");
		prefsEditor.putInt("maxstreak", getCurrentStreak() + 1);
		prefsEditor.commit();
	}

	/**
	 * Returns the number of days in the current game streak
	 */
	private int getCurrentStreak() {
		return appSharedPrefs.getInt("currentstreak", 1);
	}

	/**
	 * Increment the number of days in the current game streak
	 */
	private void incrementCurrentStreak() {
		Log.d(DEBUG_TAG, "Streak incremented");
		prefsEditor.putInt("currentstreak", getCurrentStreak() + 1);
		prefsEditor.commit();
	}

	/**
	 * Reset the number of days in the current game streak
	 */
	private void resetCurrentStreak() {
		prefsEditor.putInt("currentstreak", 1);
		prefsEditor.commit();
	}

	/**
	 * Get the last played time stamp
	 */
	private long getLastPlayedTime() {
		return appSharedPrefs.getLong("lastplayedtime",
				System.currentTimeMillis());
	}

	/**
	 * Set the last played time stamp to now
	 */
	private void setLastPlayedTime() {
		prefsEditor.putLong("lastplayedtime", System.currentTimeMillis());
		prefsEditor.commit();
	}
}
