package in.co.praveenkumar.mdroid.service;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

public class ScheduleReceiver extends BroadcastReceiver {
	private final String DEBUG_TAG = "MDroid Service";

	// restart service every x hours
	private long REPEAT_TIME = 1000 * 60 * 60;

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d(DEBUG_TAG, "Service request received !");

		// Get repeating frequency from database
		SharedPreferences settings = PreferenceManager
				.getDefaultSharedPreferences(context);
		REPEAT_TIME = REPEAT_TIME
				* settings.getInt("notification_frequency", 24);

		AlarmManager service = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);

		Intent i = new Intent(context, StartServiceReceiver.class);
		PendingIntent pending = PendingIntent.getBroadcast(context, 0, i,
				PendingIntent.FLAG_CANCEL_CURRENT);

		Calendar cal = Calendar.getInstance();
		// start 30 seconds after boot completed
		cal.add(Calendar.SECOND, 30);

		// Set first firing time to current time + frequency time
		long startTime = cal.getTimeInMillis() + REPEAT_TIME;

		// fetch every user setting time. Power optimized call.
		service.setInexactRepeating(AlarmManager.RTC_WAKEUP, startTime,
				REPEAT_TIME, pending);

	}

}
