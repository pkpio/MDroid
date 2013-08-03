package in.co.praveenkumar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class IdentifierStrings {

	private static final String TAG_IDENTIFIERS = "identifiers";

	// Identifiers JSONArray
	JSONArray identifiers = null;

	/************************** DEFINING TAGS **************************/
	/* Login */
	private static final String TAG_LOGIN = "login";
	private static final String TAG_LOGIN_LENGTH = "login_length";

	/* Course listing */
	private static final String TAG_COURSE_ID_PREV_INDEX1 = "course_id_prev_index1";
	private static final String TAG_COURSE_ID_PREV_INDEX1_LENGTH = "course_id_prev_index1_length";
	private static final String TAG_COURSE_ID_PREV_INDEX2 = "course_id_prev_index2";
	private static final String TAG_COURSE_ID_PREV_INDEX2_LENGTH = "course_id_prev_index2_length";
	private static final String TAG_COURSE_ID_END_INDEX2 = "course_id_end_index2";
	private static final String TAG_COURSE_ID_END_INDEX2_LENGTH = "course_id_end_index2_length";
	private static final String TAG_COURSE_NAME_END_INDEX = "course_name_end_index";

	/************************ DEFINING NAMES ***********************/

	String course_id_prev_index1;
	Integer course_id_prev_index1_length;
	String course_id_prev_index2;
	Integer course_id_prev_index2_length;
	String course_id_end_index2;
	Integer course_id_end_index2_length;
	String course_name_end_index;

	public IdentifierStrings() {
		// Creating JSON Parser instance
		JSONParser jParser = new JSONParser();

		// getting JSON string from URL
		JSONObject json = jParser.getJSONFromFile();

		try {
			// Getting Array of Contacts
			identifiers = json.getJSONArray(TAG_IDENTIFIERS);

			// looping through All Contacts
			JSONObject c = identifiers.getJSONObject(0);

			// Storing each json item in variable
			/* Login */
			final String login = c.getString(TAG_LOGIN);
			final Integer loginLength = c.getInt(TAG_LOGIN_LENGTH);

			/* Course listing */
			String course_id_prev_index1 = c
					.getString(TAG_COURSE_ID_PREV_INDEX1);
			Integer course_id_prev_index1_length = c
					.getInt(TAG_COURSE_ID_PREV_INDEX1_LENGTH);
			String course_id_prev_index2 = c
					.getString(TAG_COURSE_ID_PREV_INDEX2);
			Integer course_id_prev_index2_length = c
					.getInt(TAG_COURSE_ID_PREV_INDEX2_LENGTH);
			String course_id_end_index2 = c.getString(TAG_COURSE_ID_END_INDEX2);
			Integer course_id_end_index2_length = c
					.getInt(TAG_COURSE_ID_END_INDEX2_LENGTH);
			String course_name_end_index = c
					.getString(TAG_COURSE_NAME_END_INDEX);

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public String getString(String name) {
		if (name == "")
			return name;
		return name;
	}

}
