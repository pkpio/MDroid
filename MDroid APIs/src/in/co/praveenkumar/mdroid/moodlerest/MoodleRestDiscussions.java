/**************************************************************
 * Author: 	Praveen Kumar Pendyala
 * Project: MDroid
 * Created: 29th May, 2014.
 * License: http://creativecommons.org/licenses/by-nc-sa/3.0/
 **************************************************************/

package in.co.praveenkumar.mdroid.moodlerest;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import in.co.praveenkumar.mdroid.helpers.Database;
import android.content.Context;
import android.util.Log;

public class MoodleRestDiscussions {
	private final String DEBUG_TAG = "MoodleRestDiscussions";
	private String mUrl;
	private String token;
	private Database db;

	public MoodleRestDiscussions(Context context) {
		db = new Database(context);
		mUrl = db.getmUrl();
		token = db.getToken();
	}

	public void getDiscussions(ArrayList<String> forumIds) {
		String format = MoodleRestOptions.RESPONSE_FORMAT;
		String function = MoodleRestOptions.FUNCTION_GET_DISCUSSIONS;

		try {
			// Adding all parameters.
			String params = "";
			for (int i = 0; i < forumIds.size(); i++) {
				params += "&forumids[" + i + "]="
						+ URLEncoder.encode(forumIds.get(i), "UTF-8");
			}

			// Build a REST call url to make a call.
			String restUrl = mUrl + "/webservice/rest/server.php" + "?wstoken="
					+ token + "&wsfunction=" + function
					+ "&moodlewsrestformat=" + format;

			// Fetch content now.
			MoodleRestCall mrc = new MoodleRestCall();
			System.out.println(mrc.fetchContent(restUrl, params));
		} catch (UnsupportedEncodingException e) {
			Log.d(DEBUG_TAG, "URL encoding failed");
			e.printStackTrace();
		}
	}
}
