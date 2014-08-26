package in.co.praveenkumar.mdroid.moodlemodel;

import java.util.ArrayList;

public class MoodleSiteInfo {
	private String sitename;
	private String username;
	private String firstname;
	private String lastname;
	private String fullname;
	private String lang;
	private int userid;
	private String siteurl;
	private String userpictureurl;
	ArrayList<MoodleFunction> functions;
	private int downloadfiles;
	private int uploadfiles;
	private String release;
	private String version;
	private String mobilecssurl;

	public String getSitename() {
		return sitename;
	}

	public String getUsername() {
		return username;
	}

	public String getFirstname() {
		return firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public String getFullname() {
		return fullname;
	}

	/**
	 * User language
	 */
	public String getLang() {
		return lang;
	}

	public int getUserid() {
		return userid;
	}

	public String getSiteurl() {
		return siteurl;
	}

	/**
	 * User profile picture. <br/>
	 * Warning: This url is the public URL that only works when forcelogin is
	 * set to NO and guestaccess is set to YES. In order to retrieve user
	 * profile pictures independently of the Moodle config, replace
	 * "pluginfile.php" by "webservice/pluginfile.php?token=WSTOKEN&file=" Of
	 * course the user can only see profile picture depending on his/her
	 * permissions. Moreover it is recommended to use HTTPS too.
	 */
	public String getUserpictureurl() {
		return userpictureurl;
	}

	/**
	 * functions that are available
	 * 
	 * @return
	 */
	public ArrayList<MoodleFunction> getFunctions() {
		return functions;
	}

	/**
	 * 1 if users are allowed to download files, 0 if not (Optional)
	 */
	public int getDownloadfiles() {
		return downloadfiles;
	}

	/**
	 * 1 if users are allowed to upload files, 0 if not (Optional)
	 */
	public int getUploadfiles() {
		return uploadfiles;
	}

	/**
	 * Moodle release number (Optional)
	 */
	public String getRelease() {
		return release;
	}

	/**
	 * Moodle version number (Optional)
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * Mobile custom CSS theme
	 */
	public String getMobilecssurl() {
		return mobilecssurl;
	}

}
