package in.co.praveenkumar.mdroid.task;

import in.co.praveenkumar.mdroid.model.MoodleCourse;
import in.co.praveenkumar.mdroid.model.MoodleForum;
import in.co.praveenkumar.mdroid.moodlerest.MoodleRestForum;

import java.util.ArrayList;
import java.util.List;

public class ForumSyncTask {
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
	public ForumSyncTask(String mUrl, String token, long siteid) {
		this.mUrl = mUrl;
		this.token = token;
		this.siteid = siteid;
	}

	/**
	 * Sync all the events of a course. This will also sync user and site events
	 * whose scope is outside course.
	 * 
	 * @return syncStatus
	 * 
	 * @author Praveen Kumar Pendyala (praveen@praveenkumar.co.in)
	 */
	public Boolean syncForums(int courseid) {
		ArrayList<String> courseids = new ArrayList<String>();
		courseids.add(courseid + "");
		return syncForums(courseids);
	}

	/**
	 * Sync all the forums in the list of courses.
	 * 
	 * @return syncStatus
	 * 
	 * @author Praveen Kumar Pendyala (praveen@praveenkumar.co.in)
	 */
	public Boolean syncForums(ArrayList<String> courseids) {
		MoodleRestForum mrf = new MoodleRestForum(mUrl, token);
		ArrayList<MoodleForum> mForums = mrf.getForums(courseids);

		/** Error checking **/
		// Some network or encoding issue.
		if (mForums == null) {
			error = "Network issue!";
			return false;
		}

		// Moodle exception
		if (mForums.size() == 0) {
			error = "No data received";
			// No additional debug info as that needs context
			return false;
		}

		List<MoodleForum> dbForums;
		List<MoodleCourse> dbCourses;
		MoodleForum forum = new MoodleForum();
		for (int i = 0; i < mForums.size(); i++) {
			forum = mForums.get(i);
			forum.setSiteid(siteid);
			/*
			 * -TODO- Improve this search with only Sql operation
			 */
			dbForums = MoodleForum.find(MoodleForum.class,
					"forumid = ? and siteid = ?", forum.getForumid() + "",
					siteid + "");
			dbCourses = MoodleCourse.find(MoodleCourse.class,
					"courseid = ? and siteid = ?", forum.getCourseid() + "",
					siteid + "");
			if (dbCourses.size() > 0)
				forum.setCoursename(dbCourses.get(0).getShortname());
			if (dbForums.size() > 0)
				forum.setId(dbForums.get(0).getId());
			forum.save();
		}

		return true;
	}

}
