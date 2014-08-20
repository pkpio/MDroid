package in.co.praveenkumar.mdroid.parsers;

import org.json.JSONException;
import org.json.JSONObject;

import in.co.praveenkumar.mdroid.models.MoodleSiteInfo;

public class SiteInfoParser {
	private MoodleSiteInfo siteInfo = new MoodleSiteInfo();
	private String error;

	// json tags
	private static final String TAG_ERROR = "error";
	private static final String TAG_SITENAME = "sitename";
	private static final String TAG_USERID = "userid";
	private static final String TAG_USERNAME = "username";
	private static final String TAG_FIRSTNAME = "firstname";
	private static final String TAG_LASTNAME = "lastname";
	private static final String TAG_USERPIC_URL = "userpictureurl";
	private static final String TAG_DOWNLOAD_FILES = "downloadfiles";
	private static final String TAG_UPLOAD_FILES = "uploadfiles";
	private static final String TAG_RELEASE = "release";
	private static final String TAG_VERSION = "version";

	public SiteInfoParser(String json) {
		try {
			JSONObject jObj = new JSONObject(json);

			// Check if there is an error
			if (jObj.has(TAG_ERROR))
				error = jObj.getString(TAG_ERROR);

			// look for site info
			else if (jObj.has(TAG_SITENAME))
				parseSiteInfo(jObj);

			else
				error = "Unknown error";

		} catch (JSONException e) {
			error = "json decode error";
			e.printStackTrace();
		}
	}

	private void parseSiteInfo(JSONObject infoObj) {
		try {
			siteInfo.setSitename(infoObj.getString(TAG_SITENAME));
			siteInfo.setUserid(infoObj.getString(TAG_USERID));
			siteInfo.setUsername(infoObj.getString(TAG_USERNAME));
			siteInfo.setFirstname(infoObj.getString(TAG_FIRSTNAME));
			siteInfo.setLastname(infoObj.getString(TAG_LASTNAME));
			siteInfo.setUserpictureurl(infoObj.getString(TAG_USERPIC_URL));
			siteInfo.setVersion(infoObj.getString(TAG_VERSION));
			siteInfo.setRelease(infoObj.getString(TAG_RELEASE));
			siteInfo.setDownloadfiles(1 == infoObj.getInt(TAG_DOWNLOAD_FILES));
			siteInfo.setUploadfiles(1 == infoObj.getInt(TAG_UPLOAD_FILES));
		} catch (JSONException e) {
			error = "Error while decoding siteinfo object";
			e.printStackTrace();
		}
	}

	public MoodleSiteInfo getSiteInfo() {
		return siteInfo;
	}

	public String getError() {
		return error;
	}

}
