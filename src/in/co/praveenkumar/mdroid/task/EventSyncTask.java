package in.co.praveenkumar.mdroid.task;

import in.co.praveenkumar.mdroid.model.MDroidNotification;
import in.co.praveenkumar.mdroid.model.MoodleCourse;
import in.co.praveenkumar.mdroid.model.MoodleEvent;
import in.co.praveenkumar.mdroid.model.MoodleEvents;
import in.co.praveenkumar.mdroid.moodlerest.MoodleRestEvent;

import java.util.ArrayList;
import java.util.List;

public class EventSyncTask {
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
	public EventSyncTask(String mUrl, String token, long siteid) {
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
	public EventSyncTask(String mUrl, String token, long siteid,
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
	 * Sync all the events of a course. This will also sync user and site events
	 * whose scope is outside course.
	 * 
	 * @return syncStatus
	 * 
	 * @author Praveen Kumar Pendyala (praveen@praveenkumar.co.in)
	 */
	public Boolean syncEvents(int courseid) {
		ArrayList<String> courseids = new ArrayList<String>();
		courseids.add(courseid + "");
		return syncEvents(courseids);
	}

	/**
	 * Sync all the events in the list of courses. This will also sync user and
	 * site events whose scope is outside courses.
	 * 
	 * @return syncStatus
	 * 
	 * @author Praveen Kumar Pendyala (praveen@praveenkumar.co.in)
	 */
	public Boolean syncEvents(ArrayList<String> courseids) {
		MoodleRestEvent mre = new MoodleRestEvent(mUrl, token);
		MoodleEvents mEvents = mre.getEventsForIds(courseids,
				MoodleRestEvent.ID_TYPE_COURSE, true, true);

		/** Error checking **/
		// Some network or encoding issue.
		if (mEvents == null) {
			error = "Network issue!";
			return false;
		}

		// Moodle exception
		if (mEvents.getErrorcode() != null) {
			error = mEvents.getErrorcode();
			// No additional debug info as that needs context
			return false;
		}

		ArrayList<MoodleEvent> events = mEvents.getEvents();
		// Warnings are not being handled
		List<MoodleEvent> dbEvents;
		List<MoodleCourse> dbCourses;
		MoodleEvent event = new MoodleEvent();

		if (events != null)
			for (int i = 0; i < events.size(); i++) {
				event = events.get(i);
				event.setSiteid(siteid);

				dbEvents = MoodleEvent.find(MoodleEvent.class,
						"eventid = ? and siteid = ?", event.getEventid() + "",
						siteid + "");
				dbCourses = MoodleCourse.find(MoodleCourse.class,
						"courseid = ? and siteid = ?",
						event.getCourseid() + "", siteid + "");
				if (dbCourses.size() > 0)
					event.setCoursename(dbCourses.get(0).getShortname());
				if (dbEvents.size() > 0)
					event.setId(dbEvents.get(0).getId());

				// set notifications if enabled
				else if (notification) {
					new MDroidNotification(siteid,
							MDroidNotification.TYPE_EVENT, "New events in "
									+ event.getCoursename(),
							"New event titled " + event.getName()).save();
					notificationcount++;
				}

				event.save();
			}

		return true;
	}

}
