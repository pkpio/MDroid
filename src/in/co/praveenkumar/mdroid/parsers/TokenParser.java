/**************************************************************
 * Author: 	Praveen Kumar Pendyala
 * Project: MDroid
 * Created: 27th May, 2014.
 * License: http://creativecommons.org/licenses/by-nc-sa/3.0/
 **************************************************************/

package in.co.praveenkumar.mdroid.parsers;

import org.json.JSONException;
import org.json.JSONObject;

public class TokenParser {
	private String token;
	private String error;

	// json tags
	private static final String TAG_ERROR = "error";
	private static final String TAG_TOKEN = "token";

	public TokenParser(String json) {
		try {
			JSONObject jObj = new JSONObject(json);

			// Check if there is an error
			if (jObj.has(TAG_ERROR))
				error = jObj.getString(TAG_ERROR);

			// look for token field
			else if (jObj.has(TAG_TOKEN))
				token = jObj.getString(TAG_TOKEN);

			else
				error = "Unknown error";
			

		} catch (JSONException e) {
			error = "json decode error";
			e.printStackTrace();
		}
	}

	public String getToken() {
		return token;
	}

	public String getError() {
		return error;
	}

}
