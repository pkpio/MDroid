package in.co.praveenkumar.mdriod.parsers;

import in.co.praveenkumar.mdroid.models.MoodleCourse;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CourseParser {
	private ArrayList<MoodleCourse> mCourses = new ArrayList<MoodleCourse>();
	private String error;

	// json tags
	private static final String TAG_ERROR = "error";
	private static final String TAG_ID = "id";
	private static final String TAG_SHORTNAME = "shortname";
	private static final String TAG_FULLNAME = "fullname";
	private static final String TAG_SUMMARY = "summary";
	private static final String TAG_START_DATE = "startdate";
	private static final String TAG_TIME_CREATED = "timecreated";
	private static final String TAG_TIME_MODIFIED = "timemodified";

	public CourseParser(String json) {

		// The response will be a JSONArray while error gives a JSONObject
		// So, we first see if there are any courses, else we shall look for
		// error by taking it as a JSONObject.
		try {
			JSONArray jArray = new JSONArray(json);

			// Check if it has courses
			if (jArray.length() > 0)
				parseCourses(jArray);

			// No courses
			else if (jArray.length() == 0)
				error += "\n No courses found";

			// An unknown error
			else
				error += "\n Unknown error";

		} catch (JSONException e) {
			e.printStackTrace();

			// Course reading failed. Check if there was an error
			// thrown by Moodle.
			try {
				JSONObject jObj = new JSONObject(json);

				// Error thrown by Moodle
				if (jObj.has(TAG_ERROR))
					error += "\n" + jObj.getString(TAG_ERROR);

				// Unknown error.
				else
					error += "\n Unknown error";

			} catch (JSONException e1) {
				// Decode failed.
				error += "\n json decode error";

				e1.printStackTrace();
			}

		}
	}

	private void parseCourses(JSONArray jCourseArray) {
		JSONObject jCourseObj;
		for (int i = 0; i < jCourseArray.length(); i++) {
			try {
				jCourseObj = jCourseArray.getJSONObject(i);
				MoodleCourse course = new MoodleCourse();

				course.setId(jCourseObj.getString(TAG_ID));
				course.setShortname(jCourseObj.getString(TAG_SHORTNAME));
				course.setFullname(jCourseObj.getString(TAG_FULLNAME));
				course.setDatecreated(jCourseObj.getLong(TAG_TIME_CREATED));
				course.setDatemodified(jCourseObj.getLong(TAG_TIME_MODIFIED));
				course.setStartdate(jCourseObj.getLong(TAG_START_DATE));
				course.setSummary(jCourseObj.getString(TAG_SUMMARY));

				mCourses.add(course);
			} catch (JSONException e) {
				error += "\n error parsing courses";
				e.printStackTrace();
			}
		}
	}

	public ArrayList<MoodleCourse> getCourses() {
		return mCourses;
	}

	public String getError() {
		return error;
	}
}
