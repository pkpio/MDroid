/*
 * Author: 	Praveen Kumar Pendyala
 * Project: MDroid
 * Created:	01-25-2014
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

package in.co.praveenkumar.mdroid.services;

import in.co.praveenkumar.mdroid.helpers.Database;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class ScheduleReceiver extends BroadcastReceiver {
	private final String DEBUG_TAG = "MDroid Service";

	// restart service every x hours
	private long REPEAT_TIME = 1000 * 60 * 60;

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d(DEBUG_TAG, "Service request received !");

		// Get repeating frequency from database
		Database db = new Database(context);
		REPEAT_TIME = REPEAT_TIME * db.getServiceFrequency();

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
