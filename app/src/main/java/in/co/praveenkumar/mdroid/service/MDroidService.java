package in.co.praveenkumar.mdroid.service;

import in.co.praveenkumar.R;
import in.co.praveenkumar.mdroid.activity.NotificationActivity;
import in.co.praveenkumar.mdroid.helper.ImageDecoder;
import in.co.praveenkumar.mdroid.model.MoodleCourse;
import in.co.praveenkumar.mdroid.model.MoodleDiscussion;
import in.co.praveenkumar.mdroid.model.MoodleForum;
import in.co.praveenkumar.mdroid.model.MoodleSiteInfo;
import in.co.praveenkumar.mdroid.task.ContactSyncTask;
import in.co.praveenkumar.mdroid.task.CourseContentSyncTask;
import in.co.praveenkumar.mdroid.task.DiscussionSyncTask;
import in.co.praveenkumar.mdroid.task.EventSyncTask;
import in.co.praveenkumar.mdroid.task.ForumSyncTask;
import in.co.praveenkumar.mdroid.task.MessageSyncTask;
import in.co.praveenkumar.mdroid.task.PostSyncTask;
import in.co.praveenkumar.mdroid.task.UserSyncTask;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class MDroidService extends Service {
	final String DEBUG_TAG = "MDroid Services";

	/**
	 * Service Params and extras
	 */
	Boolean forceCheck = false;
	Boolean notifications = true;
	long siteid = -1;
	int courseid = -1;
	protected int startId;

	SharedPreferences settings;

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d(DEBUG_TAG, "Started service!");
		this.startId = startId;
		settings = PreferenceManager.getDefaultSharedPreferences(this);

		// Check if the service started from NotificationActivity
		Bundle extras = intent.getExtras();
		if (extras != null) {
			forceCheck = extras.getBoolean("forceCheck", false);
			notifications = extras.getBoolean("notifications", true);
			siteid = extras.getLong("siteid", -1);
			courseid = extras.getInt("courseid", -1);
		}

		// Check for new contents
		new ContentCheckerBg().execute("");

		return Service.START_NOT_STICKY;
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	/**
	 * Does the actual content checking. Loops through all the available
	 * accounts and checks for new contents in each account.
	 * 
	 * @author praveen
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

			List<MoodleSiteInfo> mSites = new ArrayList<>();
			MoodleSiteInfo site;

			// Get list of accounts in app if no siteid param is passed
			if (siteid == -1)
				mSites = MoodleSiteInfo.listAll(MoodleSiteInfo.class);
			else {
				site = MoodleSiteInfo.findById(MoodleSiteInfo.class, siteid);
				mSites.add(site);
			}

			if (mSites == null || mSites.isEmpty())
				return false;

			// Loop through all sites for checking contents
			for (int i = 0; i < mSites.size(); i++) {
				site = mSites.get(i);

				List<MoodleCourse> mCourses = new ArrayList<>();

				// Get list of favourites courses if no courseid is passed
				if (courseid == -1)
					mCourses = MoodleCourse.find(MoodleCourse.class,
							"siteid = ? and is_fav_course = ?", String.valueOf(site.getId())
									, "1");
				else
					mCourses = MoodleCourse.find(MoodleCourse.class,
							"siteid = ? and courseid = ?", String.valueOf(site.getId()),
							String.valueOf(courseid));

				// Contents sync
				if (settings.getBoolean("notify_coursecontents", true))
					contentCount = syncCourseContents(site, mCourses);

				// Forums sync
				if (settings.getBoolean("notify_forums", true))
					forumCount = syncForums(site, mCourses);

				// Discussion sync
				if (settings.getBoolean("notify_forumtopics", true))
					discussionCount = syncDiscussions(site, mCourses);

				// Forum posts (replies) sync
				if (settings.getBoolean("notify_forumposts", true))
					postCount = syncPosts(site, mCourses);

				// Events sync
				if (settings.getBoolean("notify_events", true))
					eventCount = syncEvents(site, mCourses);

				// Participants sync
				if (settings.getBoolean("notify_participants", false))
					participantCount = syncParticipants(site, mCourses);

				/**
				 * The below two sync ops have additional constraints. Don't
				 * sync if the service is passed with a courseid. That means
				 * only a course data sync.
				 */

				// Contact sync
				if (settings.getBoolean("notify_contacts", false)
						&& courseid == -1)
					contactCount = syncContacts(site);

				// Message sync.
				if (settings.getBoolean("notify_messages", true)
						&& courseid == -1)
					messageCount = syncMessages(site);

				setNotificationWithCounts(site, contentCount, forumCount,
						discussionCount, postCount, contactCount,
						participantCount, messageCount, eventCount);
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
		 * @return Notification count
		 */
		private int syncCourseContents(MoodleSiteInfo site,
				List<MoodleCourse> mCourses) {
			if (mCourses == null || mCourses.isEmpty())
				return 0;

			CourseContentSyncTask ccst = new CourseContentSyncTask(
					site.getSiteurl(), site.getToken(), site.getId(),
					notifications);
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
		 * @return Notification count
		 */
		private int syncForums(MoodleSiteInfo site, List<MoodleCourse> mCourses) {
			if (mCourses == null || mCourses.isEmpty())
				return 0;

			ArrayList<String> courseids = new ArrayList<>();
			ForumSyncTask fst = new ForumSyncTask(site.getSiteurl(),
					site.getToken(), site.getId(), notifications);
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
		 * @return Notification count
		 */
		private int syncDiscussions(MoodleSiteInfo site,
				List<MoodleCourse> mCourses) {
			if (mCourses == null || mCourses.isEmpty())
				return 0;

			DiscussionSyncTask dst = new DiscussionSyncTask(site.getSiteurl(),
					site.getToken(), site.getId(), notifications);
			List<MoodleForum> forums = new ArrayList<>();

			// Get list of discussions to sync
			for (int i = 0; i < mCourses.size(); i++)
				forums.addAll(MoodleForum.find(MoodleForum.class,
						"courseid = ? and siteid = ?", mCourses.get(i)
								.getCourseid() + "", site.getId() + ""));

			// Make an Arraylist of ids for above discussions
			ArrayList<String> forumids = new ArrayList<>();
			for (int i = 0; i < forums.size(); i++)
				forumids.add(forums.get(i).getForumid() + "");

			dst.syncDiscussions(forumids);
			return dst.getNotificationcount();
		}

		/**
		 * Sync posts in a site for given courses - all forum discussions.
		 * 
		 * @param site
		 *            MoodleSite
		 * @param mCourses
		 *            MoodleCourses whose forums need to be synced
		 * @return Notification count
		 */
		private int syncPosts(MoodleSiteInfo site, List<MoodleCourse> mCourses) {
			if (mCourses == null || mCourses.isEmpty())
				return 0;

			PostSyncTask pst = new PostSyncTask(site.getSiteurl(),
					site.getToken(), site.getId(), notifications);
			List<MoodleDiscussion> discussions = new ArrayList<>();

			// Get list of discussions to sync
			for (int i = 0; i < mCourses.size(); i++)
				discussions.addAll(MoodleDiscussion.find(
						MoodleDiscussion.class, "courseid = ? and siteid = ?",
						mCourses.get(i).getCourseid() + "", site.getId() + ""));

			// Make an Arraylist of ids for above discussions
			ArrayList<Integer> discussionids = new ArrayList<>();
			for (int i = 0; i < discussions.size(); i++)
				discussionids.add(discussions.get(i).getDiscussionid());

			pst.syncPosts(discussionids);
			return pst.getNotificationcount();
		}

		/**
		 * Sync messages of user
		 * 
		 * @param site
		 *            MoodleSite
		 * @return Notification count
		 */
		private int syncMessages(MoodleSiteInfo site) {
			MessageSyncTask mst = new MessageSyncTask(site.getSiteurl(),
					site.getToken(), site.getId(), notifications);
			mst.syncMessages(site.getUserid());
			return mst.getNotificationcount();
		}

		/**
		 * Sync events in the site for given courses. Global (not specific to
		 * any course) events will be included too.
		 * 
		 * @param site
		 *            MoodleSite
		 * @param mCourses
		 *            MoodleCourses whose events need to be synced
		 * @return Notification count
		 */
		private int syncEvents(MoodleSiteInfo site, List<MoodleCourse> mCourses) {
			if (mCourses == null || mCourses.isEmpty())
				return 0;

			ArrayList<String> courseids = new ArrayList<>();
			EventSyncTask est = new EventSyncTask(site.getSiteurl(),
					site.getToken(), site.getId(), notifications);
			for (int i = 0; i < mCourses.size(); i++)
				courseids.add(mCourses.get(i).getCourseid() + "");
			est.syncEvents(courseids);

			return est.getNotificationcount();
		}

		/**
		 * Sync participants in the site for given courses.
		 * 
		 * @param site
		 *            MoodleSite
		 * @param mCourses
		 *            MoodleCourses whose events need to be synced
		 * @return Notification count
		 */
		private int syncParticipants(MoodleSiteInfo site,
				List<MoodleCourse> mCourses) {
			if (mCourses == null || mCourses.isEmpty())
				return 0;

			UserSyncTask ust = new UserSyncTask(site.getSiteurl(),
					site.getToken(), site.getId(), notifications);
			for (int i = 0; i < mCourses.size(); i++)
				ust.syncUsers(mCourses.get(i).getCourseid());

			return ust.getNotificationcount();
		}

		/**
		 * Sync contacts of user in the given site
		 * 
		 * @param site
		 *            MoodleSite
		 * @return Notification count
		 */
		private int syncContacts(MoodleSiteInfo site) {
			ContactSyncTask cst = new ContactSyncTask(site.getSiteurl(),
					site.getToken(), site.getId(), notifications);
			cst.syncAllContacts();
			return cst.getNotificationcount();
		}

		@Override
		protected void onPostExecute(Boolean result) {
			Log.d(DEBUG_TAG, "MDroidservice exiting itself.");
			// Log the time of checking
			SharedPreferences.Editor editor = settings.edit();
			editor.putLong("notifications_lastchecked",
					System.currentTimeMillis());
			editor.commit();
			stopSelf(startId);
		}
	}

	/**
	 * Set a notification for the given counts in the given site
	 * 
	 * @param site
	 *            MoodleSite
	 * @param contentCount
	 * @param forumCount
	 * @param discussionCount
	 * @param postCount
	 * @param contactCount
	 * @param participantCount
	 * @param messageCount
	 * @param eventCount
	 */
	private void setNotificationWithCounts(MoodleSiteInfo site,
			int contentCount, int forumCount, int discussionCount,
			int postCount, int contactCount, int participantCount,
			int messageCount, int eventCount) {
		int total = contentCount + forumCount + discussionCount + postCount
				+ contactCount + participantCount + messageCount + eventCount;
		int totalForums = postCount + forumCount + discussionCount;
		int totalOthers = contactCount + eventCount + participantCount;

		final String spaces = "     ";
		String contentTitle = total + " updates for " + site.getFirstname();
		String contentText = "Contents : " + contentCount + spaces
				+ " Messages : " + messageCount;
		String subText = "Forums : " + totalForums + spaces + " Others : "
				+ totalOthers;
		String contentInfo = site.getSitename();
		Bitmap largeIcon = ImageDecoder.decodeImage(new File(Environment
				.getExternalStorageDirectory() + "/MDroid/." + site.getId()));

		if (total != 0)
			showNotification(contentTitle, contentText, subText, contentInfo,
					true, largeIcon, site.getId());
		else if (forceCheck)
			showNotification("No updated found", "Did you star your courses ?",
					"Open courses section to star a course", "", true,
					largeIcon, site.getId());

	}

	/**
	 * 
	 * @param contentTitle
	 * @param contentText
	 * @param subText
	 * @param contentInfo
	 * @param autoCancel
	 *            If true, notification is cancels itself on click. Not to be
	 *            confused with Notification persistancy.
	 * @param largeIcon
	 *            Largeicon bitmap for the notification. Pass null if you want
	 *            to use the app icon instead.
	 * @param siteid
	 *            Id of the account to which this notification corresponds to.
	 *            Pass -1 if not applicable.
	 */
	private void showNotification(String contentTitle, String contentText,
			String subText, String contentInfo, Boolean autoCancel,
			Bitmap largeIcon, long siteid) {
		int requestID = (int) System.currentTimeMillis();

		// Sent intent and extras as needed
		Intent intent = new Intent(this, NotificationActivity.class);
		if (siteid != -1)
			intent.putExtra("siteid", siteid);
		PendingIntent pIntent = PendingIntent.getActivity(this, requestID,
				intent, PendingIntent.FLAG_UPDATE_CURRENT);

		// Define sound URI
		Uri soundUri = RingtoneManager
				.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

		NotificationCompat.Builder notification = new NotificationCompat.Builder(
				this).setContentTitle(contentTitle).setContentText(contentText)
				.setSmallIcon(R.drawable.ic_actionbar_icon).setSubText(subText)
				.setContentInfo(contentInfo).setContentIntent(pIntent)
				.setAutoCancel(autoCancel).setSound(soundUri);

		if (largeIcon == null)
			largeIcon = BitmapFactory.decodeResource(getResources(),
					R.drawable.ic_launcher);
		notification.setLargeIcon(largeIcon);

		NotificationManager notificationManager = getNotificationManager();
		notificationManager.notify(requestID, notification.build());
	}

	// Building notifications
	private NotificationManager getNotificationManager() {
		return (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
	}

}
