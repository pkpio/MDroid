/**************************************************************
 * Author: 	Praveen Kumar Pendyala
 * Project: MDroid
 * Created: 8th April, 2014.
 * License: http://creativecommons.org/licenses/by-nc-sa/3.0/
 **************************************************************/

package in.co.praveenkumar.mdroid.moodlerest;

import in.co.praveenkumar.mdriod.parsers.SiteInfoParser;
import in.co.praveenkumar.mdroid.helpers.Database;
import in.co.praveenkumar.mdroid.models.MoodleSiteInfo;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import android.content.Context;
import android.util.Log;

public class MoodleRestSiteInfo {
	private final String DEBUG_TAG = "MoodleRestCourses";
	private String mUrl;
	private String token;
	private Database db;
	private String error;

	public MoodleRestSiteInfo(Context context) {
		db = new Database(context);
		mUrl = db.getmUrl();
		token = db.getToken();
	}

	public MoodleSiteInfo getSiteInfo() {
		MoodleSiteInfo siteInfo = new MoodleSiteInfo();
		String format = MoodleRestOptions.RESPONSE_FORMAT;
		String function = MoodleRestOptions.FUNCTION_GET_SITE_INFO;

		try {
			// Adding all parameters.
			String params = "" + URLEncoder.encode("", "UTF-8");

			// Build a REST call url to make a call.
			String restUrl = mUrl + "/webservice/rest/server.php" + "?wstoken="
					+ token + "&wsfunction=" + function
					+ "&moodlewsrestformat=" + format;

			// Fetch content now.
			MoodleRestCall mrc = new MoodleRestCall();
			SiteInfoParser sip = new SiteInfoParser(mrc.fetchContent(restUrl,
					params));

			siteInfo = sip.getSiteInfo();
			error = sip.getError();

		} catch (UnsupportedEncodingException e) {
			Log.d(DEBUG_TAG, "URL encoding failed");
			e.printStackTrace();
		}

		return siteInfo;
	}

	public String getError() {
		return error;
	}

}
