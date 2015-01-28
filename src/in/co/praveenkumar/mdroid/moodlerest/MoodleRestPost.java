package in.co.praveenkumar.mdroid.moodlerest;

import in.co.praveenkumar.mdroid.helper.GsonExclude;
import in.co.praveenkumar.mdroid.model.MoodlePosts;

import java.io.Reader;
import java.net.URLEncoder;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class MoodleRestPost {
	private final String DEBUG_TAG = "MoodleRestPost";
	private String mUrl;
	private String token;

	public MoodleRestPost(String mUrl, String token) {
		this.mUrl = mUrl;
		this.token = token;
	}

	/**
	 * Get all posts for a discussion.
	 * 
	 * @param discussionid
	 * 
	 * @return MoodlePosts
	 * 
	 * @author Praveen Kumar Pendyala (praveen@praveenkumar.co.in)
	 */
	public MoodlePosts getPosts(int discussionid) {
		MoodlePosts mPosts = null;
		String format = MoodleRestOption.RESPONSE_FORMAT;
		String function = MoodleRestOption.FUNCTION_GET_POSTS;

		try {
			// Adding all parameters.
			String params = "";

			params += "&discussionid="
					+ URLEncoder.encode(discussionid + "", "UTF-8");

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
			mPosts = gson.fromJson(reader, MoodlePosts.class);
		} catch (Exception e) {
			Log.d(DEBUG_TAG, "URL encoding failed");
			e.printStackTrace();
		}

		return mPosts;
	}

}
