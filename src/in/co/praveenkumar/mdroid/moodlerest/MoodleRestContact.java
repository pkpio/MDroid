package in.co.praveenkumar.mdroid.moodlerest;

import in.co.praveenkumar.mdroid.helper.GsonExclude;
import in.co.praveenkumar.mdroid.model.MoodleContacts;
import in.co.praveenkumar.mdroid.model.MoodleException;
import in.co.praveenkumar.mdroid.model.MoodleForum;
import in.co.praveenkumar.mdroid.model.MoodleUser;

import java.io.Reader;
import java.net.URLEncoder;
import java.util.List;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

public class MoodleRestContact {
	private final String DEBUG_TAG = "MoodleRestContact";
	private String mUrl;
	private String token;
	private String error;

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
			mContacts = gson.fromJson(reader, MoodleContacts.class);
			reader.close();

		} catch (Exception e) {
			Log.d(DEBUG_TAG, "URL encoding failed");
			error = "Network issue!";
			e.printStackTrace();
		}

		return mContacts;
	}

	/**
	 * Adds an user as contact
	 * 
	 * @param user
	 *            MoodleUser
	 * @return Status
	 */
	public Boolean addContact(MoodleUser user) {
		String format = MoodleRestOption.RESPONSE_FORMAT;
		String function = MoodleRestOption.FUNCTION_CREATE_CONTACTS;
		Reader reader = null;

		try {
			// Adding all parameters.
			String params = "" + URLEncoder.encode("", "UTF-8");

			params += "&userids[0]="
					+ URLEncoder.encode(String.valueOf(user.getUserid()),
							"UTF-8");

			// Build a REST call url to make a call.
			String restUrl = mUrl + "/webservice/rest/server.php" + "?wstoken="
					+ token + "&wsfunction=" + function
					+ "&moodlewsrestformat=" + format;

			// Fetch content now.
			MoodleRestCall mrc = new MoodleRestCall();
			reader = mrc.fetchContent(restUrl, params);
			GsonExclude ex = new GsonExclude();
			Gson gson = new GsonBuilder()
					.addDeserializationExclusionStrategy(ex)
					.addSerializationExclusionStrategy(ex).create();
			gson.fromJson(reader, new TypeToken<List<MoodleForum>>() {
			}.getType());

			reader.close();
			return true;
		}

		// Probably Moodle returned an error
		catch (JsonSyntaxException e) {
			e.printStackTrace();

			try {
				Gson gson = new GsonBuilder().create();
				MoodleException exception = gson.fromJson(reader,
						MoodleException.class);
				Log.d(DEBUG_TAG, exception.toString());
				error = "Moodle error: " + exception.getMessage();
			}

			// Some network issue probably
			catch (Exception e1) {
				Log.d(DEBUG_TAG, "Network issue!");
				error = "Network issue!";
				e1.printStackTrace();
			}

		}

		// Unknow error
		catch (Exception e) {
			Log.d(DEBUG_TAG, "Unknown error");
			error = "Unknown error";
			e.printStackTrace();
		}

		// Close reader if open
		try {
			reader.close();
		} catch (Exception e) {

		}

		return false;
	}

	/**
	 * Removes a contact from list
	 * 
	 * @param user
	 *            MoodleUser
	 * @return Status
	 */
	public Boolean removeContact(MoodleUser user) {
		String format = MoodleRestOption.RESPONSE_FORMAT;
		String function = MoodleRestOption.FUNCTION_DELETE_CONTACTS;
		Reader reader = null;

		try {
			// Adding all parameters.
			String params = "" + URLEncoder.encode("", "UTF-8");

			params += "&userids[0]="
					+ URLEncoder.encode(String.valueOf(user.getUserid()),
							"UTF-8");

			// Build a REST call url to make a call.
			String restUrl = mUrl + "/webservice/rest/server.php" + "?wstoken="
					+ token + "&wsfunction=" + function
					+ "&moodlewsrestformat=" + format;

			// Fetch content now.
			MoodleRestCall mrc = new MoodleRestCall();
			reader = mrc.fetchContent(restUrl, params);

			/**
			 * NOTE: Moodle return a response "null" if there was no error.
			 * Else, A Json response with MoodleException.
			 */
			char[] response = new char[4];
			reader.read(response, 0, 4);
			System.out.println(String.valueOf(response));
			if (String.valueOf(response).contentEquals("null")) {
				reader.close();
				return true;
			}

			/**
			 * Unexpected response. Check if MoodleException occured.
			 */
			Gson gson = new GsonBuilder().create();
			MoodleException exception = gson.fromJson(reader,
					MoodleException.class);
			error = "Moodle error: " + exception.getMessage();

		} catch (Exception e1) {
			e1.printStackTrace();
			error = "Network issue!";
		}

		// Close reader if open
		try {
			reader.close();
		} catch (Exception e) {

		}

		return false;
	}

	/**
	 * Return error occured during last operation
	 * 
	 * @return
	 */
	public String getError() {
		return error;
	}
}
