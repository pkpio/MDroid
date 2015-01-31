package in.co.praveenkumar.mdroid.task;

import in.co.praveenkumar.mdroid.model.MDroidNotification;
import in.co.praveenkumar.mdroid.model.MoodleCourse;
import in.co.praveenkumar.mdroid.model.MoodleUser;
import in.co.praveenkumar.mdroid.moodlerest.MoodleRestUser;

import java.util.List;

public class UserSyncTask {
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
	public UserSyncTask(String mUrl, String token, long siteid) {
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
	public UserSyncTask(String mUrl, String token, long siteid,
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
	 * Sync all topics in a discussion.
	 * 
	 * @return syncStatus
	 * 
	 * @author Praveen Kumar Pendyala (praveen@praveenkumar.co.in)
	 */
	public Boolean syncUsers(int courseid) {
		MoodleRestUser mru = new MoodleRestUser(mUrl, token);
		List<MoodleUser> mUsers = mru.getUsers(courseid);

		/** Error checking **/
		// Some network or encoding issue.
		if (mUsers == null || mUsers.size() == 0) {
			error = "No users found!";
			return false;
		}

		List<MoodleUser> dbUsers;
		MoodleUser mUser = new MoodleUser();
		for (int i = 0; i < mUsers.size(); i++) {
			mUser = mUsers.get(i);
			mUser.setSiteid(siteid);
			mUser.setCourseid(courseid);

			dbUsers = MoodleUser.find(MoodleUser.class,
					"userid = ? and siteid = ? and courseid = ?",
					mUser.getUserid() + "", siteid + "", courseid + "");
			if (dbUsers.size() > 0)
				mUser.setId(dbUsers.get(0).getId());
			// set notifications if enabled
			else if (notification) {
				List<MoodleCourse> dbCourses = MoodleCourse.find(
						MoodleCourse.class, "courseid = ? and siteid = ?",
						siteid + "", courseid + "");
				MoodleCourse course = (dbCourses != null && dbCourses.size() > 0) ? dbCourses
						.get(0) : null;

				if (course != null) {
					new MDroidNotification(siteid,
							MDroidNotification.TYPE_PARTICIPANT,
							"New people joined " + course.getShortname(),
							mUser.getFullname() + " joined "
									+ course.getFullname()).save();
					notificationcount++;
				}
			}
			mUser.save();
		}

		return true;
	}
}
