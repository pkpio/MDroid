package in.co.praveenkumar.mdroid.service;

import in.co.praveenkumar.R;
import in.co.praveenkumar.mdroid.activity.CourseActivity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.orm.Database;

public class MDroidService extends Service {
	final String DEBUG_TAG = "MDroid Services";
	int totalUpdateCount = 0;
	int fileUpdateCount = 0;
	int forumUpdateCount = 0;
	int replyUpdateCount = 0;

	Database db;
	Boolean forceChecked = false;

	protected int startId;

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d(DEBUG_TAG, "Started service!");
		this.startId = startId;

		// Check if the service started from NotificationActivity
		Bundle extras = intent.getExtras();
		if (extras != null)
			if (extras.getBoolean("isComingFromNotifications", false)) {
				forceChecked = true;
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
		new tryAsyncDetailsFetch().execute("", "");
	}

	private class tryAsyncDetailsFetch extends AsyncTask<String, Integer, Long> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		protected Long doInBackground(String... credentials) {

			return null;
		}

		protected void onPostExecute(Long result) {
			Log.d(DEBUG_TAG, "Service completed. Notifying.");

			if (totalUpdateCount > 0)
				setNotificationWithCounts(totalUpdateCount, fileUpdateCount,
						forumUpdateCount, replyUpdateCount);
			else if (forceChecked)
				showNotification("No updated found",
						"Did you star your courses ?",
						"Open files section to star a course", "", true);
			else {
				showNotification("No updated found",
						"Did you star your courses ?",
						"Open files section to star a course", "", true);
			}

			Log.d(DEBUG_TAG, "Notified. Exiting.");
			stopSelf(startId);
		}
	}

	// Building notifications
	private NotificationManager getNotificationManager() {
		return (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
	}

	private void setNotificationWithCounts(int total, int fCount, int tCount,
			int rCount) {
		// For debugging
		// int count = 2;

		// Build strings for actual notification
		String contentTitle = total + " updates found!";
		String contentText = fCount + " files";
		String subText = tCount + " forum topics";
		String contentInfo = rCount + " forum replies";

		showNotification(contentTitle, contentText, subText, contentInfo, true);

	}

	private void showNotification(String contentTitle, String contentText,
			String subText, String contentInfo, Boolean autoCancel) {
		int requestID = (int) System.currentTimeMillis();
		Intent intent = new Intent(this, CourseActivity.class);
		PendingIntent pIntent = PendingIntent.getActivity(this, requestID,
				intent, PendingIntent.FLAG_UPDATE_CURRENT);

		NotificationCompat.Builder notification = new NotificationCompat.Builder(
				this).setContentTitle(contentTitle).setContentText(contentText)
				.setSmallIcon(R.drawable.code_inverted).setSubText(subText)
				.setContentInfo(contentInfo).setContentIntent(pIntent)
				.setAutoCancel(autoCancel);
		Bitmap bm = BitmapFactory.decodeResource(getResources(),
				R.drawable.ic_launcher);
		notification.setLargeIcon(bm);

		NotificationManager notificationManager = getNotificationManager();
		notificationManager.notify(1, notification.build());
	}

}
