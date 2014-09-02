package in.co.praveenkumar.mdroid.moodlerest;

import in.co.praveenkumar.mdroid.helper.GsonExclude;
import in.co.praveenkumar.mdroid.moodlemodel.MoodleContacts;

import java.io.Reader;
import java.net.URLEncoder;
import java.util.List;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class MoodleRestContact {
	private final String DEBUG_TAG = "MoodleRestContact";
	private String mUrl;
	private String token;

	public MoodleRestContact(String mUrl, String token) {
		this.mUrl = mUrl;
		this.token = token;
	}

	/**
	 * Get a list of all contacts types. This has 3 types of contacts, online,
	 * offline and strangers. The contacts in each type can be extracted using
	 * getOnline() or similar method for other types too.
	 * 
	 * @return
	 */
	public MoodleContacts getAllContacts() {
		MoodleContacts mContacts = null; // So that we know about network
											// failures
		String format = MoodleRestOption.RESPONSE_FORMAT;
		String function = MoodleRestOption.FUNCTION_GET_CONTACTS;

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
			GsonExclude ex = new GsonExclude();
			Gson gson = new GsonBuilder()
					.addDeserializationExclusionStrategy(ex)
					.addSerializationExclusionStrategy(ex).create();
			mContacts = gson.fromJson(reader,
					new TypeToken<List<MoodleContacts>>() {
					}.getType());
			reader.close();

		} catch (Exception e) {
			Log.d(DEBUG_TAG, "URL encoding failed");
			e.printStackTrace();
		}

		return mContacts;
	}

}
