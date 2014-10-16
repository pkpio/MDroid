package in.co.praveenkumar.mdroid.task;

import in.co.praveenkumar.mdroid.moodlemodel.MoodleCourse;
import in.co.praveenkumar.mdroid.moodlemodel.MoodleSiteInfo;
import in.co.praveenkumar.mdroid.moodlerest.MoodleRestCourse;

import java.util.ArrayList;
import java.util.List;

public class CourseSyncTask {
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
	public CourseSyncTask(String mUrl, String token, long siteid) {
		this.mUrl = mUrl;
		this.token = token;
		this.siteid = siteid;
	}

	/**
	 * Sync all the courses in the current site.
	 * 
	 * @return syncStatus
	 * 
	 * @author Praveen Kumar Pendyala (praveen@praveenkumar.co.in)
	 */
	public Boolean syncAllCourses() {
		MoodleRestCourse mrc = new MoodleRestCourse(mUrl, token);
		ArrayList<MoodleCourse> mCourses = mrc.getAllCourses();

		/** Error checking **/
		// Some network or encoding issue.
		if (mCourses.size() == 0) {
			error = "Network issue!";
			return false;
		}

		// Moodle exception
		if (mCourses.size() == 1 && mCourses.get(0).getCourseid() == 0) {
			error = "Moodle Exception: User don't have permissions!";
			return false;
		}

		// Add siteid to all courses and update
		MoodleCourse course = new MoodleCourse();
		List<MoodleCourse> dbCourses;
		for (int i = 0; i < mCourses.size(); i++) {
			course = mCourses.get(i);
			course.setSiteid(siteid);

			// Update or save in database
			dbCourses = MoodleCourse.find(MoodleCourse.class,
					"courseid = ? and siteid = ?", course.getCourseid() + "",
					course.getSiteid() + "");
			if (dbCourses.size() > 0) {
				// Set app specific fields explicitly
				course.setId(dbCourses.get(0).getId());
				course.setIsUserCourse(dbCourses.get(0).getIsUserCourse());
				course.setIsFavCourse(dbCourses.get(0).getIsFavCourse());
			}
			course.save();
		}

		return true;
	}

	/**
	 * Sync all courses of logged in user in the current site.
	 * 
	 * @return syncStatus
	 * 
	 * @author Praveen Kumar Pendyala (praveen@praveenkumar.co.in)
	 */
	public Boolean syncUserCourses() {
		// Get userid
		MoodleSiteInfo site = MoodleSiteInfo.findById(MoodleSiteInfo.class,
				siteid);
		
		if (site == null)
			return false;

		int userid = site.getUserid();

		MoodleRestCourse mrc = new MoodleRestCourse(mUrl, token);
		ArrayList<MoodleCourse> mCourses = mrc.getEnrolledCourses(userid + "");

		/** Error checking **/
		// Some network or encoding issue.
		if (mCourses.size() == 0)
			return false;

		// Moodle exception
		if (mCourses.size() == 1 && mCourses.get(0).getCourseid() == 0)
			return false;

		// Add siteid and isUserCourse to all courses and update
		MoodleCourse course = new MoodleCourse();
		List<MoodleCourse> dbCourses;
		for (int i = 0; i < mCourses.size(); i++) {
			course = mCourses.get(i);
			course.setSiteid(siteid);
			course.setIsUserCourse(true);

			// Update or save in database
			dbCourses = MoodleCourse.find(MoodleCourse.class,
					"courseid = ? and siteid = ?", course.getCourseid() + "",
					course.getSiteid() + "");
			if (dbCourses.size() > 0) {
				// Set app specific fields explicitly
				course.setId(dbCourses.get(0).getId());
				course.setIsFavCourse(dbCourses.get(0).getIsFavCourse());
			}
			course.save();
		}

		return true;
	}

	/**
	 * Error message from the last failed sync operation.
	 * 
	 * @return error
	 * 
	 * @author Praveen Kumar Pendyala (praveen@praveenkumar.co.in)
	 */
	public String getError() {
		return error;
	}
}
