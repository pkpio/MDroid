package in.co.praveenkumar.mdriod.parsers;

import org.json.JSONException;
import org.json.JSONObject;

public class TokenParser {
	private String token;
	private String error;

	// json tags
	private static final String TAG_TOKEN = "token";
	private static final String TAG_ERROR = "error";

	public TokenParser(String json) {
		try {
			JSONObject jObj = new JSONObject(json);

			// Check if token exists
			if (jObj.has(TAG_TOKEN))
				token = jObj.getString(TAG_TOKEN);

			// Check if error field exists
			else if (jObj.has(TAG_ERROR))
				error = jObj.getString(TAG_ERROR);

			// An unknown error
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
