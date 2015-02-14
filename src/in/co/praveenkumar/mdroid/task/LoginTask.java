package in.co.praveenkumar.mdroid.task;

import in.co.praveenkumar.R;
import in.co.praveenkumar.mdroid.activity.CourseActivity;
import in.co.praveenkumar.mdroid.activity.WebservicesoffActivity;
import in.co.praveenkumar.mdroid.helper.SessionSetting;
import in.co.praveenkumar.mdroid.model.MoodleSiteInfo;
import in.co.praveenkumar.mdroid.model.MoodleToken;
import in.co.praveenkumar.mdroid.moodlerest.MoodleRestSiteInfo;
import in.co.praveenkumar.mdroid.moodlerest.MoodleRestToken;
import in.co.praveenkumar.mdroid.view.LoginStatusViewHolder;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

public class LoginTask extends AsyncTask<String, String, Boolean> {
	String username;
	String password;
	String mUrl;
	String token;
	String progress = "";
	MoodleSiteInfo siteInfo = new MoodleSiteInfo();
	SessionSetting session;
	Context context;
	Boolean webservices = true;

	// Widgets
	LoginStatusViewHolder progressViews;

	/**
	 * Do a normal login with Username, Password and Moodle Url.
	 * 
	 * @param username
	 * @param password
	 * @param mUrl
	 * @param progressViews
	 *            Views to show progress of login
	 * 
	 * @param context
	 *            So that we can start course activity if all goes well
	 * 
	 * @author Praveen Kumar Pendyala (praveen@praveenkumar.co.in)
	 */
	public LoginTask(String username, String password, String mUrl,
			LoginStatusViewHolder progressViews, Context context) {
		this.username = username;
		this.password = password;
		this.mUrl = mUrl;
		this.progressViews = progressViews;
		this.context = context;
		session = new SessionSetting(context);
	}

	/**
	 * Do a Paranoid login with Token and Moodle Url.
	 * 
	 * @param token
	 * @param mUrl
	 * @param progressViews
	 *            Views to show progress of login
	 * @param context
	 *            So that we can start course activity if all goes well
	 * 
	 * @author Praveen Kumar Pendyala (praveen@praveenkumar.co.in)
	 */
	public LoginTask(String token, String mUrl,
			LoginStatusViewHolder progressViews, Context context) {
		this.token = token;
		this.mUrl = mUrl;
		this.progressViews = progressViews;
		this.context = context.getApplicationContext();
		session = new SessionSetting(context);
	}

	@Override
	protected void onPreExecute() {
		progressViews.loginButton.setText(context
				.getString(R.string.login_prog_loggingin));
		progressViews.loginButton.setEnabled(false);
		progressViews.retryButton.setVisibility(Button.GONE);
		progressViews.statusLayout.setVisibility(RelativeLayout.VISIBLE);
		progressViews.progressBar.setVisibility(ProgressBar.VISIBLE);
		progressViews.progressTitle.setText(context
				.getString(R.string.login_prog_loggingin));
	}

	@Override
	protected void onProgressUpdate(String... params) {
		if (params == null || params.length == 0)
			return;

		progress += params[0] + "\n";
		progressViews.progressText.setText(progress);
	}

	@Override
	protected Boolean doInBackground(String... params) {

		// Get token if required
		if (token == null)
			if (!getToken())
				return false;

		// Get Site info
		if (!getSiteInfo())
			return false;

		// Sync data
		publishProgress("Syncing data");
		new DownloadTask(null).download(siteInfo.getUserpictureurl(), "", "."
				+ siteInfo.getId(), false, DownloadTask.APP_DOWNLOADER);
		if (!getCourseInfo())
			return false;
		getMessagesContacts();

		publishProgress("\nSync complete!");
		return true;
	}

	/**
	 * Get a token from username, password and mUrl. Token found in this.token
	 * 
	 * @return True: If token fetched. False: otherwise.
	 * 
	 * @author Praveen Kumar Pendyala (praveen@praveenkumar.co.in)
	 */
	private Boolean getToken() {
		publishProgress("Fetching token");
		MoodleRestToken mrt = new MoodleRestToken(username, password, mUrl);
		MoodleToken mt = mrt.getToken();

		if (mt == null || mt.getToken() == null) {
			publishProgress("Token fetch failed!");
			publishProgress("\nError\n" + mt.getError());

			// Check webservice here. We are using a short text because, the
			// Moodle could be setup with a non-english language.
			if (mt.getError().contains("Web services"))
				webservices = false;
			else
				webservices = true;

			if (session.getDebugMode()) {
				publishProgress("Moodle url: " + mt.getReproductionlink());
				publishProgress("Stacktrace: " + mt.getStacktrace());
				publishProgress("Debug info: " + mt.getDebuginfo());
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
	 * 
	 * @author Praveen Kumar Pendyala (praveen@praveenkumar.co.in)
	 */
	private Boolean getSiteInfo() {
		publishProgress("Fetching site info");
		MoodleRestSiteInfo mrsi = new MoodleRestSiteInfo(mUrl, token);
		siteInfo = mrsi.getSiteInfo();
		if (siteInfo.getFullname() == null) {
			publishProgress("Siteinfo fetch failed!");
			publishProgress("\nError code: \n" + siteInfo.getErrorcode());

			if (session.getDebugMode()) {
				publishProgress("Exception: " + siteInfo.getException());
				publishProgress("Message: " + siteInfo.getMessage());
				publishProgress("Debug info: " + siteInfo.getDebuginfo());
			}

			return false;
		}
		siteInfo.setToken(token);
		siteInfo.save();
		session.setCurrentSiteId(siteInfo.getId());

		publishProgress("\nWelcome " + siteInfo.getFullname() + "!\n");

		return true;
	}

	/**
	 * Sync all Moodle courses and user's courses
	 * 
	 * @return User course sync status
	 * 
	 * @author Praveen Kumar Pendyala (praveen@praveenkumar.co.in)
	 */
	private Boolean getCourseInfo() {
		CourseSyncTask cs = new CourseSyncTask(mUrl, token, siteInfo.getId());

		publishProgress("Syncing courses");
		Boolean usrCourseSyncStatus = cs.syncUserCourses();
		if (!usrCourseSyncStatus) {
			publishProgress(cs.getError());
			publishProgress("\nSync failed!");
		}

		// Success on user's course sync is what matters
		return usrCourseSyncStatus;
	}

	/**
	 * Sync all Moodle Messages and Contacts of the user. This is required for
	 * proper Notification working.<br/>
	 * Note: The status of this task shouldn't affect the login task in a bad
	 * way. This is just a convienence task.
	 * 
	 * @return sync status
	 * 
	 * @author Praveen Kumar Pendyala (praveen@praveenkumar.co.in)
	 */
	private Boolean getMessagesContacts() {
		MessageSyncTask mst = new MessageSyncTask(mUrl, token, siteInfo.getId());
		ContactSyncTask cst = new ContactSyncTask(mUrl, token, siteInfo.getId());

		publishProgress("Syncing messages");
		Boolean messageSync = mst.syncMessages(siteInfo.getUserid());

		publishProgress("Syncing contacts");
		Boolean contactSync = cst.syncAllContacts();

		// Success on user's course sync is what matters
		return messageSync && contactSync;
	}

	@Override
	protected void onPostExecute(Boolean status) {
		if (status) {
			Intent i = new Intent(context, CourseActivity.class);
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
					| Intent.FLAG_ACTIVITY_CLEAR_TASK);
			context.startActivity(i);
			return;
		} else {
			progressViews.progressTitle.setText("Login failed");
			progressViews.progressBar.setVisibility(ProgressBar.GONE);
			progressViews.retryButton.setVisibility(Button.VISIBLE);
		}

		if (!webservices) {
			Intent i = new Intent(context, WebservicesoffActivity.class);
			context.startActivity(i);
		}
	}

}
