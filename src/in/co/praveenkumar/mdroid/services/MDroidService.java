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

import in.co.praveenkumar.mdroid.MainActivity;
import in.co.praveenkumar.mdroid.helpers.Database;
import in.co.praveenkumar.mdroid.models.Course;
import in.co.praveenkumar.mdroid.networking.DoLogin;
import in.co.praveenkumar.mdroid.parser.CoursesParser;
import in.co.praveenkumar.mdroid.sqlite.databases.SqliteTbCourses;

import java.util.ArrayList;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class MDroidService extends Service {
	private final String DEBUG_TAG = "MDroid Services";
	private DoLogin l;
	private UpdatesChecker uc;
	private int totalUpdateCount = 0;
	private int fileUpdateCount = 0;
	private int forumUpdateCount = 0;
	private int replyUpdateCount = 0;

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO do something useful
		Log.d(DEBUG_TAG, "Started service!");

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
		Database db = new Database(this);
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

			if (totalUpdateCount > 0)
				showNotification(totalUpdateCount, fileUpdateCount,
						forumUpdateCount, replyUpdateCount);
			else
				showNotification(6, 3, 1, 2);

			Log.d(DEBUG_TAG, "Notified. Exiting.");
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
				uc = new UpdatesChecker(getApplicationContext(), mCourses
						.get(i).getId());
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
	public PendingIntent getPendingIntent() {
		return PendingIntent.getActivity(this, 0, new Intent(this,
				MainActivity.class), 0);
	}

	public NotificationManager getNotificationManager() {
		return (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
	}

	public void showNotification(int total, int fCount, int tCount, int rCount) {
		NotificationCompat.Builder notification = new NotificationCompat.Builder(
				this).setContentTitle(total + " updates found!")
				.setContentText(fCount + " files")
				.setSmallIcon(in.co.praveenkumar.R.drawable.ic_launcher)
				.setSubText(tCount + " forum topics")
				.setContentInfo(rCount + " forum responses");
		NotificationManager notificationManager = getNotificationManager();
		notificationManager.notify(1, notification.build());
	}
}
