package in.co.praveenkumar.mdroid.moodlerest;

import in.co.praveenkumar.mdroid.moodlemodel.MoodleSiteInfo;

import java.io.Reader;
import java.net.URLEncoder;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class MoodleRestSiteInfo {
	private final String DEBUG_TAG = "MoodleRestCourses";
	private String mUrl;
	private String token;
	MoodleSiteInfo siteInfo = new MoodleSiteInfo();

	public MoodleRestSiteInfo(String mUrl, String token) {
		this.mUrl = mUrl;
		this.token = token;
	}

	public MoodleSiteInfo getSiteInfo() {
		String format = MoodleRestOption.RESPONSE_FORMAT;
		String function = MoodleRestOption.FUNCTION_GET_SITE_INFO;

		try {
			// Adding all parameters.
			String params = "" + URLEncoder.encode("", "UTF-8");

			// Build a REST call url to make a call.
			String restUrl = mUrl + "/webservice/rest/server.php" + "?wstoken="
					+ token + "&wsfunction=" + function
					+ "&moodlewsrestformat=" + format;

			// Fetch content now
			MoodleRestCall mrc = new MoodleRestCall();
			Reader reader = mrc.fetchContent(restUrl, params);
			Gson gson = new GsonBuilder().create();
			siteInfo = gson.fromJson(reader, MoodleSiteInfo.class);
			reader.close();

		} catch (Exception e) {
			Log.d(DEBUG_TAG, "URL encoding failed");
			e.printStackTrace();
		}

		return siteInfo;
	}

}