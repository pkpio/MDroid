package in.co.praveenkumar.mdroid.task;

import in.co.praveenkumar.mdroid.model.MDroidNotification;
import in.co.praveenkumar.mdroid.model.MoodleCourse;
import in.co.praveenkumar.mdroid.model.MoodleDiscussion;
import in.co.praveenkumar.mdroid.moodlerest.MoodleRestDiscussion;

import java.util.ArrayList;
import java.util.List;

public class DiscussionSyncTask {
	String mUrl;
	String token;
	long siteid;

	String error;
	Boolean notification;
	int notificationcount;

	/**
	 * 
	 * @param mUrl
	 * @param token
	 * @param siteid
	 * 
	 * @author Praveen Kumar Pendyala (praveen@praveenkumar.co.in)
	 */
	public DiscussionSyncTask(String mUrl, String token, long siteid) {
		this.mUrl = mUrl;
		this.token = token;
		this.siteid = siteid;
		this.notification = false;
		this.notificationcount = 0;
	}

	/**
	 * 
	 * @param mUrl
	 * @param token
	 * @param siteid
	 * @param notification
	 *            If true, sets notifications for new contents
	 * 
	 * @author Praveen Kumar Pendyala (praveen@praveenkumar.co.in)
	 */
	public DiscussionSyncTask(String mUrl, String token, long siteid,
			Boolean notification) {
		this.mUrl = mUrl;
		this.token = token;
		this.siteid = siteid;
		this.notification = notification;
		this.notificationcount = 0;
	}

	/**
	 * Get the notifications count. Notifications should be enabled during
	 * Object instantiation.
	 * 
	 * @return notificationcount
	 */
	public int getNotificationcount() {
		return notificationcount;
	}

	/**
	 * Sync all the discussion topics of a forum.
	 * 
	 * @return syncStatus
	 * 
	 * @author Praveen Kumar Pendyala (praveen@praveenkumar.co.in)
	 */
	public Boolean syncDiscussions(int forumid) {
		ArrayList<String> forumids = new ArrayList<String>();
		forumids.add(forumid + "");
		return syncDiscussions(forumids);
	}

	/**
	 * Sync all the discussion topics in the list of forums.
	 * 
	 * @return syncStatus
	 * 
	 * @author Praveen Kumar Pendyala (praveen@praveenkumar.co.in)
	 */
	public Boolean syncDiscussions(ArrayList<String> forumids) {
		MoodleRestDiscussion mrd = new MoodleRestDiscussion(mUrl, token);
		ArrayList<MoodleDiscussion> mTopics = mrd.getDiscussions(forumids);

		/** Error checking **/
		// Some network or encoding issue.
		if (mTopics == null) {
			error = "Network issue!";
			return false;
		}

		// Moodle exception
		if (mTopics.size() == 0) {
			error = "No data received";
			// No additional debug info as that needs context
			return false;
		}

		List<MoodleDiscussion> dbTopics;
		MoodleDiscussion topic = new MoodleDiscussion();
		for (int i = 0; i < mTopics.size(); i++) {
			topic = mTopics.get(i);
			topic.setSiteid(siteid);

			dbTopics = MoodleDiscussion.find(MoodleDiscussion.class,
					"discussionid = ? and siteid = ?", topic.getDiscussionid()
							+ "", siteid + "");
			if (dbTopics.size() > 0)
				topic.setId(dbTopics.get(0).getId());

			// set notifications if enabled
			else if (notification) {
				List<MoodleCourse> dbCourses = MoodleCourse.find(
						MoodleCourse.class, "courseid = ? and siteid = ?",
						siteid + "", topic.getCourseid() + "");
				MoodleCourse course = (dbCourses != null && dbCourses.size() > 0) ? dbCourses
						.get(0) : null;

				if (course != null) {
					new MDroidNotification(siteid,
							MDroidNotification.TYPE_FORUM_TOPIC,
							"New forum topic in " + course.getShortname(),
							topic.getName() + " started in course "
									+ course.getFullname()).save();
					notificationcount++;
				}
			}
			topic.save();
		}

		return true;
	}
}
