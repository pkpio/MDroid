package in.co.praveenkumar.mdroid.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

public class ScheduleReceiver extends BroadcastReceiver {
	private static final String DEBUG_TAG = "MDroid Service";

	// restart service every x hours
	private static long REPEAT_TIME = 1000 * 60 * 60;

	@Override
	public void onReceive(Context context, Intent intent) {
		scheduleService(context);
	}

	/**
	 * Schedule MDroid Notification service.
	 * 
	 * @param context
	 *            Context
	 */
	public static void scheduleService(Context context) {
		Log.d(DEBUG_TAG, "Service scheduling request received !");

		// Get repeating frequency from database
		SharedPreferences settings = PreferenceManager
				.getDefaultSharedPreferences(context);
		REPEAT_TIME = REPEAT_TIME
				* Long.valueOf(settings.getString("notification_frequency",
						"24"));

		AlarmManager service = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);

		Intent i = new Intent(context, StartServiceReceiver.class);
		PendingIntent pending = PendingIntent.getBroadcast(context, 0, i,
				PendingIntent.FLAG_CANCEL_CURRENT);

		// Set the first firing based on last checked time and frequency
		long lastCheck = settings.getLong("notifications_lastchecked",
				System.currentTimeMillis());
		long startTime = lastCheck + REPEAT_TIME;

		// fetch every user setting time. Power optimized call.
		service.setInexactRepeating(AlarmManager.RTC_WAKEUP, startTime,
				REPEAT_TIME, pending);
	}

	/**
	 * Unschedule MDroid Notification service.
	 * 
	 * @param context
	 *            Context
	 */
	public static void unscheduleService(Context context) {
		AlarmManager service = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		Intent i = new Intent(context, StartServiceReceiver.class);
		PendingIntent pending = PendingIntent.getBroadcast(context, 0, i,
				PendingIntent.FLAG_CANCEL_CURRENT);
		service.cancel(pending);
	}

	/**
	 * Reschedule MDroid Notification service. Typical use case would be when
	 * the notification frequency has been changed.
	 * 
	 * @param context
	 *            Context
	 */
	public static void rescheduleService(Context context) {
		unscheduleService(context);
		scheduleService(context);
	}
}
