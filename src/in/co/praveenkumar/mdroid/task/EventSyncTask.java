package in.co.praveenkumar.mdroid.task;

import in.co.praveenkumar.mdroid.moodlemodel.MoodleEvent;
import in.co.praveenkumar.mdroid.moodlemodel.MoodleEvents;
import in.co.praveenkumar.mdroid.moodlerest.MoodleRestEvent;

import java.util.ArrayList;
import java.util.List;

public class EventSyncTask {
	String mUrl;
	String token;
	long siteid;
	String error;

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
	}

	/**
	 * Sync all the events of a course.
	 * 
	 * @return syncStatus
	 * 
	 * @author Praveen Kumar Pendyala (praveen@praveenkumar.co.in)
	 */
	public Boolean syncCourseEvents(int courseid) {
		ArrayList<String> courseids = new ArrayList<String>();
		courseids.add(courseid + "");
		return syncCourseEvents(courseids);
	}

	/**
	 * Sync all the events in the list of courses.
	 * 
	 * @return syncStatus
	 * 
	 * @author Praveen Kumar Pendyala (praveen@praveenkumar.co.in)
	 */
	public Boolean syncCourseEvents(ArrayList<String> courseids) {
		MoodleRestEvent mre = new MoodleRestEvent(mUrl, token);
		MoodleEvents mEvents = mre.getEventsForIds(courseids,
				MoodleRestEvent.ID_TYPE_COURSE);

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
		MoodleEvent event = new MoodleEvent();

		if (events != null)
			for (int i = 0; i < events.size(); i++) {
				event = events.get(i);
				event.setSiteid(siteid);
				/*
				 * -TODO- Improve this search with only Sql operation
				 */
				dbEvents = MoodleEvent.find(MoodleEvent.class,
						"eventid = ? and siteid = ?", event.getEventid() + "",
						siteid + "");
				if (dbEvents.size() > 0)
					event.setId(dbEvents.get(0).getId());
				event.save();
			}

		return true;
	}

}
