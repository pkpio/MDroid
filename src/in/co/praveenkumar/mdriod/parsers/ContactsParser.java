package in.co.praveenkumar.mdriod.parsers;

import in.co.praveenkumar.mdroid.models.MoodleContact;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ContactsParser {
	private ArrayList<MoodleContact> contacts = new ArrayList<MoodleContact>();
	private String error;

	// json tags
	private static final String TAG_ERROR = "error";
	private static final String TAG_ID = "id";
	private static final String TAG_FULLNAME = "fullname";
	private static final String TAG_IMAGE_URL = "profileimageurlsmall";
	private static final String TAG_UNREAD = "unread";
	private static final String TAG_ONLINE = "online";
	private static final String TAG_OFFLINE = "offline";
	private static final String TAG_STRANGERS = "strangers";

	public ContactsParser(String json) {
		try {
			JSONObject jObj = new JSONObject(json);

			// Check if there is an error
			if (jObj.has(TAG_ERROR))
				error = jObj.getString(TAG_ERROR);

			// look for site info
			else if (jObj.has(TAG_ONLINE))
				parseContacts(jObj);

			else
				error = "Unknown error";

		} catch (JSONException e) {
			error = "json decode error";
			e.printStackTrace();
		}
	}

	private void parseContacts(JSONObject cObj) {
		JSONArray jArray = new JSONArray();
		try {
			// Online
			jArray = cObj.getJSONArray(TAG_ONLINE);
			parseContactsInStatus(jArray, "Online");

			// Offline
			jArray = cObj.getJSONArray(TAG_OFFLINE);
			parseContactsInStatus(jArray, "Offline");

			// Strangers
			jArray = cObj.getJSONArray(TAG_STRANGERS);
			parseContactsInStatus(jArray, "Strangers");

		} catch (JSONException e) {
			error += "Error decoding contact groups";
			e.printStackTrace();
		}
	}

	private void parseContactsInStatus(JSONArray jContactArray, String status) {
		JSONObject jContactObj;
		for (int i = 0; i < jContactArray.length(); i++) {
			try {
				jContactObj = jContactArray.getJSONObject(i);
				MoodleContact contact = new MoodleContact();

				contact.setId(jContactObj.getString(TAG_ID));
				contact.setFullname(jContactObj.getString(TAG_FULLNAME));
				contact.setImageurl(jContactObj.getString(TAG_IMAGE_URL));
				contact.setUnread(jContactObj.getInt(TAG_UNREAD));
				contact.setStatus(status);

				contacts.add(contact);
			} catch (JSONException e) {
				error += "\n Error parsing contacts";
				e.printStackTrace();
			}
		}
	}

	public ArrayList<MoodleContact> getContacts() {
		return contacts;
	}

	public String getError() {
		return error;
	}

}
