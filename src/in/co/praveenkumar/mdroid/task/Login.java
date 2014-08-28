package in.co.praveenkumar.mdroid.task;

import in.co.praveenkumar.mdroid.helper.SessionSetting;
import in.co.praveenkumar.mdroid.moodlemodel.MoodleSiteInfo;
import in.co.praveenkumar.mdroid.moodlemodel.MoodleToken;
import in.co.praveenkumar.mdroid.moodlerest.MoodleRestSiteInfo;
import in.co.praveenkumar.mdroid.moodlerest.MoodleRestToken;
import android.os.AsyncTask;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

public class Login extends AsyncTask<String, Integer, Boolean> {
	String username;
	String password;
	String mUrl;
	String progress = "";
	String token;
	MoodleSiteInfo siteInfo = new MoodleSiteInfo();

	// Widgets
	Button loginButton;
	ScrollView loginProgressSV;
	TextView loginProgressTV;

	/**
	 * Do a normal login with Username, Password and Moodle Url.
	 * 
	 * @param username
	 * @param password
	 * @param mUrl
	 * @param loginButton
	 *            Login button widget
	 * @param loginProgressLL
	 *            Login progress linearlayout
	 * @param loginProgressTV
	 *            Login progress message showing TextView widget
	 */
	public Login(String username, String password, String mUrl,
			Button loginButton, ScrollView loginProgressSV,
			TextView loginProgressTV) {
		this.username = username;
		this.password = password;
		this.mUrl = mUrl;
		this.loginButton = loginButton;
		this.loginProgressSV = loginProgressSV;
		this.loginProgressTV = loginProgressTV;
	}

	/**
	 * Do a Paranoid login with Token and Moodle Url.
	 * 
	 * @param token
	 * @param mUrl
	 * @param loginButton
	 *            Login button widget
	 * @param loginProgressLL
	 *            Login progress linearlayout
	 * @param loginProgressTV
	 *            Login progress message showing TextView widget
	 */
	public Login(String token, String mUrl, Button loginButton,
			ScrollView loginProgressSV, TextView loginProgressTV) {
		this.token = token;
		this.mUrl = mUrl;
		this.loginButton = loginButton;
		this.loginProgressSV = loginProgressSV;
		this.loginProgressTV = loginProgressTV;
	}

	@Override
	protected void onPreExecute() {
		loginButton.setText("Logging in..");
		loginButton.setEnabled(false);
		loginProgressSV.setVisibility(ScrollView.VISIBLE);
	}

	@Override
	protected void onProgressUpdate(Integer... params) {
		loginProgressTV.setText(progress);
	}

	@Override
	protected Boolean doInBackground(String... params) {

		// Get token if required
		if (token == null)
			if (!getToken())
				return null;

		// Get Site info
		if (!getSiteInfo())
			return null;

		// Sync data
		updateProgress("Syncing data\n");
		new Download(null).download(siteInfo.getUserpictureurl(), "."
				+ siteInfo.getId(), false, Download.APP_DOWNLOADER);
		if (!getCourseInfo())
			return null;

		updateProgress("\nSync complete!");
		return null;
	}

	private void updateProgress(String status) {
		progress += status + "\n";
		publishProgress(0);
	}

	/**
	 * Get a token from username, password and mUrl. Token found in this.token
	 * 
	 * @return True: If token fetched. False: otherwise.
	 */
	private Boolean getToken() {
		updateProgress("Fetching token");
		MoodleRestToken mrt = new MoodleRestToken(username, password, mUrl);
		MoodleToken mt = mrt.getToken();

		if (mt.getToken() == null) {
			updateProgress("Token fetch failed!");
			updateProgress("\nError: \n" + mt.getError());

			if (SessionSetting.DebugMode) {
				updateProgress("Moodle url: " + mt.getReproductionlink());
				updateProgress("Stacktrace: " + mt.getStacktrace());
				updateProgress("Debug info: " + mt.getDebuginfo());
			}

			return false;
		}
		this.token = mt.getToken();

		return true;
	}

	/**
	 * Get siteinfo from this.token and this.mUrl
	 * 
	 * @return True: If info fetched. False: otherwise.
	 */
	private Boolean getSiteInfo() {
		updateProgress("Fetching site info");
		MoodleRestSiteInfo mrsi = new MoodleRestSiteInfo(mUrl, token);
		siteInfo = mrsi.getSiteInfo();
		if (siteInfo.getFullname() == null) {
			updateProgress("Siteinfo fetch failed!");
			updateProgress("\nError code: \n" + siteInfo.getErrorcode());

			if (SessionSetting.DebugMode) {
				updateProgress("Exception: " + siteInfo.getException());
				updateProgress("Message: " + siteInfo.getMessage());
				updateProgress("Debug info: " + siteInfo.getDebuginfo());
			}

			return false;
		}
		siteInfo.setToken(token);
		siteInfo.save();
		SessionSetting.currentSiteId = siteInfo.getId();

		updateProgress("\nWelcome " + siteInfo.getFullname() + "!\n");

		return true;
	}

	/**
	 * Sync all Moodle courses and user's courses
	 * 
	 * @return User course sync status
	 */
	private Boolean getCourseInfo() {
		CourseSync cs = new CourseSync(mUrl, token, siteInfo.getId());

		updateProgress("Syncing all Moodle courses");
		Boolean allCourseSyncStatus = cs.syncAllCourses();
		if (!allCourseSyncStatus)
			updateProgress(cs.getError());

		updateProgress("Syncing user's courses");
		Boolean usrCourseSyncStatus = cs.syncUserCourses();
		if (!usrCourseSyncStatus) {
			updateProgress(cs.getError());
			updateProgress("\nSync failed!");
		}

		// Success on user's course sync is what matters
		return usrCourseSyncStatus;
	}

}
