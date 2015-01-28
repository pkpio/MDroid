package in.co.praveenkumar.mdroid.moodlerest;

import in.co.praveenkumar.mdroid.helper.GsonExclude;
import in.co.praveenkumar.mdroid.model.MoodleForum;

import java.io.Reader;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class MoodleRestForum {
	private final String DEBUG_TAG = "MoodleRestForum";
	private String mUrl;
	private String token;

	public MoodleRestForum(String mUrl, String token) {
		this.mUrl = mUrl;
		this.token = token;
	}

	/**
	 * Get all the forums of given course in the Moodle site.<br/>
	 * 
	 * @return ArrayList of MoodleForums
	 * 
	 * @author Praveen Kumar Pendyala (praveen@praveenkumar.co.in)
	 */
	public ArrayList<MoodleForum> getForums(String courseid) {
		ArrayList<String> courseids = new ArrayList<String>();
		courseids.add(courseid);
		return getForums(courseids);
	}

	/**
	 * Get all the forums of given courses in the Moodle site.<br/>
	 * 
	 * @return ArrayList of MoodleForums
	 * 
	 * @author Praveen Kumar Pendyala (praveen@praveenkumar.co.in)
	 */
	public ArrayList<MoodleForum> getForums(ArrayList<String> courseids) {
		ArrayList<MoodleForum> mForums = new ArrayList<MoodleForum>();
		String format = MoodleRestOption.RESPONSE_FORMAT;
		String function = MoodleRestOption.FUNCTION_GET_FORUMS;

		try {
			// Adding all parameters.
			String params = "";
			for (int i = 0; i < courseids.size(); i++) {
				params += "&courseids[" + i + "]="
						+ URLEncoder.encode(courseids.get(i), "UTF-8");
			}

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
			mForums = gson.fromJson(reader, new TypeToken<List<MoodleForum>>() {
			}.getType());
			reader.close();

		} catch (Exception e) {
			Log.d(DEBUG_TAG, "URL encoding failed");
			e.printStackTrace();
		}

		return mForums;
	}
}
