package in.co.praveenkumar.mdroid.service;

import in.co.praveenkumar.R;
import in.co.praveenkumar.mdroid.activity.CourseActivity;
import in.co.praveenkumar.mdroid.model.MoodleCourse;
import in.co.praveenkumar.mdroid.model.MoodleDiscussion;
import in.co.praveenkumar.mdroid.model.MoodleSiteInfo;
import in.co.praveenkumar.mdroid.task.CourseContentSyncTask;
import in.co.praveenkumar.mdroid.task.DiscussionSyncTask;
import in.co.praveenkumar.mdroid.task.ForumSyncTask;

import java.util.ArrayList;
import java.util.List;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class MDroidService extends Service {
	final String DEBUG_TAG = "MDroid Services";
	Boolean forceCheck = false;

	protected int startId;

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d(DEBUG_TAG, "Started service!");
		this.startId = startId;

		// Check if the service started from NotificationActivity
		Bundle extras = intent.getExtras();
		if (extras != null)
			if (extras.getBoolean("forceCheck", false)) {
				forceCheck = true;
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
		new ContentCheckerBg().execute("", "");
	}

	/**
	 * Does the actual content checking. Loops through all the available
	 * accounts and checks for new contents in each account.
	 * 
	 * @author praveen
	 * 
	 */
	private class ContentCheckerBg extends AsyncTask<String, Integer, Boolean> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		protected Boolean doInBackground(String... credentials) {
			int contentCount = 0;
			int forumCount = 0;
			int discussionCount = 0;
			int postCount = 0;
			int contactCount = 0;
			int participantCount = 0;
			int messageCount = 0;
			int eventCount = 0;

			// Get list of accounts in app
			List<MoodleSiteInfo> mSites = MoodleSiteInfo
					.listAll(MoodleSiteInfo.class);
			MoodleSiteInfo site;
			if (mSites == null || mSites.size() == 0)
				return false;

			// Loop through all sites for checking contents
			for (int i = 0; i < mSites.size(); i++) {
				site = mSites.get(i);

				// Get list of favourites courses
				List<MoodleCourse> mCourses = MoodleCourse.find(
						MoodleCourse.class, "siteid = ? and is_fav_course = ?",
						site.getId() + "", "1");

				// Contents sync
				contentCount = syncCourseContents(site, mCourses);

				// Forums sync
				forumCount = syncForums(site, mCourses);

				// Discussion sync
				discussionCount = syncDiscussions(site, mCourses);

			}

			return true;
		}

		/**
		 * Sync course contents in a site for given courses.
		 * 
		 * @param site
		 *            MoodleSite
		 * @param mCourses
		 *            MoodleCourses whose contents need to be synced
		 * @return
		 */
		private int syncCourseContents(MoodleSiteInfo site,
				List<MoodleCourse> mCourses) {
			if (mCourses == null || mCourses.size() == 0)
				return 0;

			CourseContentSyncTask ccst = new CourseContentSyncTask(
					site.getSiteurl(), site.getToken(), site.getId(), true);
			for (int i = 0; i < mCourses.size(); i++)
				ccst.syncCourseContents(mCourses.get(i).getCourseid());

			return ccst.getNotificationcount();
		}

		/**
		 * Sync forums in a site for given courses.
		 * 
		 * @param site
		 *            MoodleSite
		 * @param mCourses
		 *            MoodleCourses whose forums need to be synced
		 * @return
		 */
		private int syncForums(MoodleSiteInfo site, List<MoodleCourse> mCourses) {
			if (mCourses == null || mCourses.size() == 0)
				return 0;

			ArrayList<String> courseids = new ArrayList<String>();
			ForumSyncTask fst = new ForumSyncTask(site.getSiteurl(),
					site.getToken(), site.getId(), true);
			for (int i = 0; i < mCourses.size(); i++)
				courseids.add(mCourses.get(i).getCourseid() + "");
			fst.syncForums(courseids);

			return fst.getNotificationcount();
		}

		/**
		 * Sync discussions in a site for given courses.
		 * 
		 * @param site
		 *            MoodleSite
		 * @param mCourses
		 *            MoodleCourses whose forums need to be synced
		 * @return
		 */
		private int syncDiscussions(MoodleSiteInfo site,
				List<MoodleCourse> mCourses) {
			if (mCourses == null || mCourses.size() == 0)
				return 0;

			DiscussionSyncTask dst = new DiscussionSyncTask(site.getSiteurl(),
					site.getToken(), site.getId(), true);
			List<MoodleDiscussion> discussions = new ArrayList<MoodleDiscussion>();

			// Get list of discussions to sync
			for (int i = 0; i < mCourses.size(); i++)
				discussions.addAll(MoodleDiscussion.find(
						MoodleDiscussion.class, "courseid = ? and siteid = ?",
						mCourses.get(i).getCourseid() + "", site.getId() + ""));

			// Make an Arraylist of ids for above discussions
			ArrayList<String> discussionids = new ArrayList<String>();
			for (int i = 0; i < discussions.size(); i++)
				discussionids.add(discussions.get(i).getDiscussionid() + "");

			dst.syncDiscussions(discussionids);
			return dst.getNotificationcount();
		}

		@Override
		protected void onPostExecute(Boolean result) {
			Log.d(DEBUG_TAG, "Service completed. Notifying.");

			int test = 2;
			if (test > 0)
				setNotificationWithCounts(2, 1, 1, 0);
			else if (forceCheck)
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
		// Define sound URI
		Uri soundUri = RingtoneManager
				.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

		NotificationCompat.Builder notification = new NotificationCompat.Builder(
				this).setContentTitle(contentTitle).setContentText(contentText)
				.setSmallIcon(R.drawable.mdroid_logo_inverted)
				.setSubText(subText).setContentInfo(contentInfo)
				.setContentIntent(pIntent).setAutoCancel(autoCancel)
				.setSound(soundUri);
		Bitmap bm = BitmapFactory.decodeResource(getResources(),
				R.drawable.ic_launcher);
		notification.setLargeIcon(bm);

		NotificationManager notificationManager = getNotificationManager();
		notificationManager.notify(1, notification.build());
	}

}
