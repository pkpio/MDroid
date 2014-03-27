package in.co.praveenkumar.mdroid.moodlerest;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import in.co.praveenkumar.mdroid.helpers.Database;
import android.content.Context;
import android.util.Log;

public class MoodleRestCourseContents {
	private final String DEBUG_TAG = "MoodleRestCourseContents";
	private String mUrl;
	private String token;
	private Database db;

	public MoodleRestCourseContents(Context context) {
		db = new Database(context);
		mUrl = db.getmUrl();
		token = db.getToken();
	}

	public void getCourseContent(String courseId) {
		String format = MoodleRestOptions.RESPONSE_FORMAT;
		String function = MoodleRestOptions.FUNCTION_GET_COURSE_CONTENTS;

		try {
			// Adding all parameters.
			String params = "&courseid=" + URLEncoder.encode(courseId, "UTF-8");

			// Build a REST call url to make a call.
			String restUrl = mUrl + "/webservice/rest/server.php" + "?wstoken="
					+ token + "&wsfunction=" + function
					+ "&moodlewsrestformat=" + format;

			// Fetch content now.
			MoodleRestCall mrc = new MoodleRestCall();
			System.out.println(mrc.fetchContent(restUrl, params));
		} catch (UnsupportedEncodingException e) {
			Log.d(DEBUG_TAG, "URL encoding failed");
			e.printStackTrace();
		}
	}
}
