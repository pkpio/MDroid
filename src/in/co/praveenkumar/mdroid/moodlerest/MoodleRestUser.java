package in.co.praveenkumar.mdroid.moodlerest;

import in.co.praveenkumar.mdroid.helper.GsonExclude;
import in.co.praveenkumar.mdroid.moodlemodel.MoodleUser;

import java.io.Reader;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class MoodleRestUser {
	private final String DEBUG_TAG = "MoodleRestUser";
	private String mUrl;
	private String token;

	public MoodleRestUser(String mUrl, String token) {
		this.mUrl = mUrl;
		this.token = token;
	}

	/**
	 * Retrieve users information for a specified unique field. The search field
	 * can be 'id' or 'idnumber' or 'username' or 'email'
	 * 
	 * @return MoodleUser
	 */
	public MoodleUser getUserinfoByField(int userid) {
		ArrayList<MoodleUser> mUsers = null; // So that we know about network
		// failures
		String format = MoodleRestOption.RESPONSE_FORMAT;
		String function = MoodleRestOption.FUNCTION_GET_USERS_FROM_COURSE;

		try {
			// Adding all parameters.
			String params = "values[0]="
					+ URLEncoder.encode(userid + "", "UTF-8");

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
			mUsers = gson.fromJson(reader, new TypeToken<List<MoodleUser>>() {
			}.getType());
			reader.close();

		} catch (Exception e) {
			Log.d(DEBUG_TAG, "URL encoding failed");
			e.printStackTrace();
		}

		return mUsers.get(0);
	}

}
