package in.co.praveenkumar.mdroid.moodlerest;

import in.co.praveenkumar.mdroid.moodlemodel.MoodleCourse;

import java.io.Reader;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class MoodleRestCourse {
	private final String DEBUG_TAG = "MoodleRestCourses";
	private String mUrl;
	private String token;

	public MoodleRestCourse(String mUrl, String token) {
		this.mUrl = token;
		this.token = mUrl;
	}

	/**
	 * Get all the courses in the Moodle site.<br/>
	 * User may not have permission to do this. In such cases, only one course
	 * entry is added to the list with only error fields filled. If no entries
	 * are found, then it could mean a network or encoding issue.
	 * 
	 * @return ArrayList<MoodleCourse>
	 */
	public ArrayList<MoodleCourse> getAllCourses() {
		ArrayList<MoodleCourse> mCourses = new ArrayList<MoodleCourse>();
		String format = MoodleRestOption.RESPONSE_FORMAT;
		String function = MoodleRestOption.FUNCTION_GET_ALL_COURSES;

		try {
			// Adding all parameters.
			String params = "" + URLEncoder.encode("", "UTF-8");

			// Build a REST call url to make a call.
			String restUrl = mUrl + "/webservice/rest/server.php" + "?wstoken="
					+ token + "&wsfunction=" + function
					+ "&moodlewsrestformat=" + format;

			// Fetch content now.
			MoodleRestCall mrc = new MoodleRestCall();
			Reader reader = mrc.fetchContent(restUrl, params);
			Gson gson = new GsonBuilder().create();
			mCourses = gson.fromJson(reader,
					new TypeToken<List<MoodleCourse>>() {
					}.getType());
			reader.close();

		} catch (Exception e) {
			Log.d(DEBUG_TAG, "URL encoding failed");
			e.printStackTrace();
		}

		return mCourses;
	}

	/**
	 * Get all the courses that a user is enrolled in<br/>
	 * In case of errors, only one course entry is added to the list with only
	 * error fields filled. If no entries are found, then it could mean a
	 * network or encoding issue.
	 * 
	 * 
	 * @param userId
	 *            userId of the user whose courses are needed
	 * @return ArrayList<MoodleCourse>
	 */
	public ArrayList<MoodleCourse> getEnrolledCourses(String userId) {
		ArrayList<MoodleCourse> mCourses = new ArrayList<MoodleCourse>();
		String format = MoodleRestOption.RESPONSE_FORMAT;
		String function = MoodleRestOption.FUNCTION_GET_ENROLLED_COURSES;

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
			Reader reader = mrc.fetchContent(restUrl, params);
			Gson gson = new GsonBuilder().create();
			mCourses = gson.fromJson(reader,
					new TypeToken<List<MoodleCourse>>() {
					}.getType());
			reader.close();

		} catch (Exception e) {
			Log.d(DEBUG_TAG, "URL encoding failed");
			e.printStackTrace();
		}

		return mCourses;
	}

}
