/**************************************************************
 * Author: 	Praveen Kumar Pendyala
 * Project: MDroid
 * Created: 9th May, 2014.
 * License: http://creativecommons.org/licenses/by-nc-sa/3.0/
 **************************************************************/

package in.co.praveenkumar.mdroid.moodlerest;

import in.co.praveenkumar.mdriod.parsers.ContactsParser;
import in.co.praveenkumar.mdroid.helpers.Database;
import in.co.praveenkumar.mdroid.models.MoodleContact;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import android.content.Context;
import android.util.Log;

public class MoodleRestContacts {
	private final String DEBUG_TAG = "MoodleRestCourses";
	private String mUrl;
	private String token;
	private Database db;
	private String error;

	public MoodleRestContacts(Context context) {
		db = new Database(context);
		mUrl = db.getmUrl();
		token = db.getToken();
	}

	public ArrayList<MoodleContact> getContacts() {
		ArrayList<MoodleContact> contacts = new ArrayList<MoodleContact>();
		String format = MoodleRestOptions.RESPONSE_FORMAT;
		String function = MoodleRestOptions.FUNCTION_GET_CONTACTS;

		try {
			// Adding all parameters.
			String params = "" + URLEncoder.encode("", "UTF-8");

			// Build a REST call url to make a call.
			String restUrl = mUrl + "/webservice/rest/server.php" + "?wstoken="
					+ token + "&wsfunction=" + function
					+ "&moodlewsrestformat=" + format;

			// Fetch content now.
			MoodleRestCall mrc = new MoodleRestCall();
			ContactsParser cp = new ContactsParser(mrc.fetchContent(restUrl,
					params));

			contacts = cp.getContacts();
			error = cp.getError();

		} catch (UnsupportedEncodingException e) {
			Log.d(DEBUG_TAG, "URL encoding failed");
			e.printStackTrace();
		}
		return contacts;
	}

	public String getError() {
		return error;
	}

}
