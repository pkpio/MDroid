/**************************************************************
 * Author: 	Praveen Kumar Pendyala
 * Project: MDroid
 * Created: 28th May, 2014.
 * License: http://creativecommons.org/licenses/by-nc-sa/3.0/
 **************************************************************/

package in.co.praveenkumar.mdroid.moodlerest;

import in.co.praveenkumar.mdriod.parsers.CourseParser;
import in.co.praveenkumar.mdroid.helpers.Database;
import in.co.praveenkumar.mdroid.models.MoodleCourse;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import android.content.Context;
import android.util.Log;

public class MoodleRestCourses {
	private final String DEBUG_TAG = "MoodleRestCourses";
	private String mUrl;
	private String token;
	private Database db;
	private String error;

	public MoodleRestCourses(Context context) {
		db = new Database(context);
		mUrl = db.getmUrl();
		token = db.getToken();
	}

	public ArrayList<MoodleCourse> getAllCourses() {
		ArrayList<MoodleCourse> mCourses = new ArrayList<MoodleCourse>();
		String format = MoodleRestOptions.RESPONSE_FORMAT;
		String function = MoodleRestOptions.FUNCTION_GET_ALL_COURSES;

		try {
			// Adding all parameters.
			String params = "" + URLEncoder.encode("", "UTF-8");

			// Build a REST call url to make a call.
			String restUrl = mUrl + "/webservice/rest/server.php" + "?wstoken="
					+ token + "&wsfunction=" + function
					+ "&moodlewsrestformat=" + format;

			// Fetch content now.
			MoodleRestCall mrc = new MoodleRestCall();
			CourseParser cp = new CourseParser(
					mrc.fetchContent(restUrl, params));
			mCourses = cp.getCourses();
			error = cp.getError();

		} catch (UnsupportedEncodingException e) {
			Log.d(DEBUG_TAG, "URL encoding failed");
			e.printStackTrace();
		}

		return mCourses;
	}

	public ArrayList<MoodleCourse> getEnrolledCourses(String userId) {
		ArrayList<MoodleCourse> mCourses = new ArrayList<MoodleCourse>();
		String format = MoodleRestOptions.RESPONSE_FORMAT;
		String function = MoodleRestOptions.FUNCTION_GET_ENROLLED_COURSES;

		try {
			// Adding all parameters.
			String params = "&" + URLEncoder.encode("userid", "UTF-8") + "="
					+ userId;

			// Build a REST call url to make a call.
			String restUrl = mUrl + "/webservice/rest/server.php" + "?wstoken="
					+ token + "&wsfunction=" + function
					+ "&moodlewsrestformat=" + format;

			// Fetch content now.
			MoodleRestCall mrc = new MoodleRestCall();
			CourseParser cp = new CourseParser(
					mrc.fetchContent(restUrl, params));
			mCourses = cp.getCourses();
			error = cp.getError();

		} catch (UnsupportedEncodingException e) {
			Log.d(DEBUG_TAG, "URL encoding failed");
			e.printStackTrace();
		}

		return mCourses;
	}

	public String getError() {
		return error;
	}

}
