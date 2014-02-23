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

import in.co.praveenkumar.mdroid.NotificationsActivity;
import in.co.praveenkumar.mdroid.helpers.Database;
import in.co.praveenkumar.mdroid.models.Course;
import in.co.praveenkumar.mdroid.networking.DoLogin;
import in.co.praveenkumar.mdroid.parser.CoursesParser;
import in.co.praveenkumar.mdroid.sqlite.databases.SqliteTbCourses;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

@SuppressLint("SimpleDateFormat")
public class MDroidService extends Service {
	private final String DEBUG_TAG = "MDroid Services";
	private DoLogin l;
	private UpdatesChecker uc;
	private int totalUpdateCount = 0;
	private int fileUpdateCount = 0;
	private int forumUpdateCount = 0;
	private int replyUpdateCount = 0;

	Database db;

	protected int startId;

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d(DEBUG_TAG, "Started service!");
		this.startId = startId;

		// Check if the service started from NotificationActivity
		Bundle extras = intent.getExtras();
		if (extras != null) {
			if (extras.getBoolean("isComingFromNotifications", false))
				showNotification("Checking for content", "Please wait..",
						"You will be notified once complete", "", false);

		}

		// Login and check for content
		checkForContent();

		return Service.START_NOT_STICKY;
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO for communication return IBinder implementation
		return null;
	}

	private void checkForContent() {
		// Login first
		db = new Database(this);
		String uName = db.getLDAP();
		String pswd = db.getPswd();
		l = new DoLogin(db.getURL());
		new tryAsyncDetailsFetch().execute(uName, pswd);
	}

	private class tryAsyncDetailsFetch extends AsyncTask<String, Integer, Long> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			Log.d(DEBUG_TAG, "Logging in..");
		}

		protected Long doInBackground(String... credentials) {
			l.doLogin(credentials[0], credentials[1]);

			// If no error returned. Parse for courses.
			if (l.isLoggedIn()) {
				fetchFavCourseDetails();
			}
			return null;
		}

		protected void onPostExecute(Long result) {
			Log.d(DEBUG_TAG, "Service completed. Notifying.");
			
			// Save current time as last checked for content
			Date cDate = new Date(System.currentTimeMillis());
			SimpleDateFormat format = new SimpleDateFormat(
					"MM/dd/yyyy hh:mm:ss");
			String curTimeStrng = format.format(cDate);
			db.setLastChecked(curTimeStrng);

			if (totalUpdateCount > 0)
				setNotificationWithCounts(totalUpdateCount, fileUpdateCount,
						forumUpdateCount, replyUpdateCount);
			else
				setNotificationWithCounts(0, 0, 0, 0);

			Log.d(DEBUG_TAG, "Notified. Exiting.");
			stopSelf(startId);
		}
	}

	// Fetching details
	private void fetchFavCourseDetails() {
		// Get moodle courses.
		CoursesParser cp = new CoursesParser(l.getContent());
		ArrayList<Course> mCourses = cp.getCourses();

		// For each course in moodle check if it is in favs
		SqliteTbCourses stc = new SqliteTbCourses(getApplicationContext());
		for (int i = 0; i < mCourses.size(); i++) {
			if (stc.isFav(mCourses.get(i).getId())) {
				uc = new UpdatesChecker(getApplicationContext(),
						mCourses.get(i));
				uc.checkForUpdates();
				if (uc.getTotalUpdatesCount() > 0) {
					totalUpdateCount += uc.getTotalUpdatesCount();
					fileUpdateCount += uc.getFilesUpdatesCount();
					forumUpdateCount += uc.getForumsUpdatesCount();
					replyUpdateCount += uc.getRepliesUpdatesCount();
				}

			}
		}
	}

	// Building notifications
	private NotificationManager getNotificationManager() {
		return (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
	}

	private void setNotificationWithCounts(int total, int fCount, int tCount,
			int rCount) {
		// For debugging
		int count = db.getNotifedCount();
		db.setNotifedCount(count + 1);

		// Build strings for actual notification
		String contentTitle = total + " updates found!";
		String contentText = fCount + " files";
		String subText = tCount + " forum topics";
		String contentInfo = rCount + " forum replies";

		showNotification(contentTitle, contentText, subText, contentInfo, true);

	}

	private void showNotification(String contentTitle, String contentText,
			String subText, String contentInfo, Boolean autoCancel) {
		Intent intent = new Intent(this, NotificationsActivity.class);
		PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);

		NotificationCompat.Builder notification = new NotificationCompat.Builder(
				this).setContentTitle(contentTitle).setContentText(contentText)
				.setSmallIcon(in.co.praveenkumar.R.drawable.ic_launcher)
				.setSubText(subText).setContentInfo(contentInfo)
				.setContentIntent(pIntent).setAutoCancel(autoCancel);
		NotificationManager notificationManager = getNotificationManager();
		notificationManager.notify(1, notification.build());
	}

}
