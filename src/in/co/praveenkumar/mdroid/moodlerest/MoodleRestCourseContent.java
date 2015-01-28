package in.co.praveenkumar.mdroid.moodlerest;

import in.co.praveenkumar.mdroid.helper.GsonExclude;
import in.co.praveenkumar.mdroid.model.MoodleSection;

import java.io.Reader;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class MoodleRestCourseContent {
	private final String DEBUG_TAG = "MoodleRestCourseContents";
	private String mUrl;
	private String token;

	public MoodleRestCourseContent(String mUrl, String token) {
		this.mUrl = mUrl;
		this.token = token;
	}

	/**
	 * Get all the sections in the Course.<br/>
	 * User may not have permission to do this. In such cases, only one course
	 * entry is added to the list with only error fields filled. If no entries
	 * are found, then it could mean a network or encoding issue.
	 * 
	 * @return ArrayList<MoodleCourse>
	 * 
	 * @author Praveen Kumar Pendyala (praveen@praveenkumar.co.in)
	 */
	public ArrayList<MoodleSection> getCourseContent(String courseid) {
		ArrayList<MoodleSection> sections = null;
		String format = MoodleRestOption.RESPONSE_FORMAT;
		String function = MoodleRestOption.FUNCTION_GET_COURSE_CONTENTS;

		try {
			// Adding all parameters.
			String params = "&courseid=" + URLEncoder.encode(courseid, "UTF-8");

			// Build a REST call url to make a call.
			String restUrl = mUrl + "/webservice/rest/server.php" + "?wstoken="
					+ token + "&wsfunction=" + function
					+ "&moodlewsrestformat=" + format;

			// Fetch content now.
			MoodleRestCall mrc = new MoodleRestCall();
			Reader reader = mrc.fetchContent(restUrl, params);
			GsonExclude ex = new GsonExclude();
			Gson gson = new GsonBuilder()
					.addDeserializationExclusionStrategy(ex)
					.addSerializationExclusionStrategy(ex).create();
			sections = gson.fromJson(reader,
					new TypeToken<List<MoodleSection>>() {
					}.getType());
			reader.close();

		} catch (Exception e) {
			Log.d(DEBUG_TAG, "URL encoding failed");
			e.printStackTrace();
		}

		return sections;
	}

}
