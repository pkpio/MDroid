/**************************************************************
 * Author: 	Praveen Kumar Pendyala
 * Project: MDroid
 * Created: 1st April, 2014.
 * License: http://creativecommons.org/licenses/by-nc-sa/3.0/
 **************************************************************/

package in.co.praveenkumar.mdroid.activities;

import in.co.praveenkumar.mdroid.apis.R;
import in.co.praveenkumar.mdroid.helpers.Database;
import in.co.praveenkumar.mdroid.helpers.MDroidDownloader;
import in.co.praveenkumar.mdroid.models.MoodleSiteInfo;
import in.co.praveenkumar.mdroid.moodlerest.MoodleRestSiteInfo;
import in.co.praveenkumar.mdroid.moodlerest.MoodleToken;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.andreabaccega.widget.FormEditText;

public class LoginActivity extends Activity {
	final String DEBUG_TAG = "LoginActivity";
	Button switchNormal;
	Button switchParanoid;
	Button buttonRetry;

	LinearLayout normalLayout;
	LinearLayout paranoidLayout;
	LinearLayout switchLayout;
	LinearLayout progressLayout;

	FormEditText mNormUrl;
	FormEditText mParaUrl;
	FormEditText unameView;
	FormEditText pswdView;

	TextView progressTitle;
	TextView progressText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		setUpWidgets();
		InitialSetup();
	}

	private void setUpWidgets() {
		switchNormal = (Button) findViewById(R.id.switch_normal);
		switchParanoid = (Button) findViewById(R.id.switch_paranoid);
		buttonRetry = (Button) findViewById(R.id.retry_button);

		normalLayout = (LinearLayout) findViewById(R.id.normal_layout);
		paranoidLayout = (LinearLayout) findViewById(R.id.paranoid_layout);
		switchLayout = (LinearLayout) findViewById(R.id.switch_layout);
		progressLayout = (LinearLayout) findViewById(R.id.progress_layout);

		mNormUrl = (FormEditText) findViewById(R.id.norm_url);
		mParaUrl = (FormEditText) findViewById(R.id.para_url);
		unameView = (FormEditText) findViewById(R.id.username);
		pswdView = (FormEditText) findViewById(R.id.password);

		progressTitle = (TextView) findViewById(R.id.progress_title);
		progressText = (TextView) findViewById(R.id.progress_text);

		View v = null;
		setToNormal(v);
	}

	public void doNormLogin(View v) {
		FormEditText[] allFields = { mNormUrl, unameView, pswdView };

		boolean allValid = true;
		for (FormEditText field : allFields) {
			allValid = field.testValidity() && allValid;
		}

		if (allValid) {
			String username = unameView.getText().toString();
			String password = pswdView.getText().toString();
			String mUrl = mNormUrl.getText().toString();
			new AsyncLogin().execute(username, password, mUrl);
		}
	}

	private class AsyncLogin extends AsyncTask<String, String, String> {
		Boolean isLogged = false;

		@Override
		protected void onPreExecute() {
			normalLayout.setVisibility(LinearLayout.GONE);
			paranoidLayout.setVisibility(LinearLayout.GONE);
			switchLayout.setVisibility(LinearLayout.GONE);
			progressLayout.setVisibility(LinearLayout.VISIBLE);
			buttonRetry.setVisibility(Button.GONE);

			progressTitle.setText("Logging in...");
			progressText.setText("Looking for webservices");
		}

		@Override
		protected String doInBackground(String... mParams) {
			// Save mUrl in database
			Database db = new Database(getApplicationContext());
			db.setmUrl(mParams[2]);

			// Get a token
			MoodleToken mt = new MoodleToken(mParams[0], mParams[1], mParams[2]);
			String token = mt.getToken();
			if (token != null) {
				publishProgress("Token received.", "Logged in");
				db.setToken(token);
				isLogged = true;

				// Get site info
				publishProgress("Token received.\n Fetching site info");
				MoodleSiteInfo siteInfo = new MoodleSiteInfo();
				MoodleRestSiteInfo mrsi = new MoodleRestSiteInfo(
						getApplicationContext());
				siteInfo = mrsi.getSiteInfo();

				// Parse info
				publishProgress("Token received.\n Site info received.\n Parsing info");
				MDroidDownloader md = new MDroidDownloader(
						getApplicationContext());
				md.download(siteInfo.getUserpictureurl(), "user.jpg", false,
						MDroidDownloader.APP_DOWNLOADER);
				db.setUserFullname(siteInfo.getFirstname() + " "
						+ siteInfo.getLastname());
				db.setUserid(siteInfo.getUserid());

			} else
				publishProgress(mt.getErrorsString(), "Login failed");

			return null;
		}

		@Override
		protected void onProgressUpdate(String... progress) {
			if (progress.length >= 1)
				progressText.setText(progress[0]);
			if (progress.length >= 2)
				progressTitle.setText(progress[1]);
		}

		@Override
		protected void onPostExecute(String result) {
			if (isLogged)
				openMoodleHome();
			else
				buttonRetry.setVisibility(Button.VISIBLE);
		}
	}

	private void openMoodleHome() {
		Intent i = new Intent(this, MoodleHomeActivity.class);
		startActivityForResult(i, 1);
	}

	public void setToNormal(View v) {
		Log.d(DEBUG_TAG, "Set to normal");

		switchNormal.setActivated(true);
		switchParanoid.setActivated(false);

		switchLayout.setVisibility(LinearLayout.VISIBLE);
		normalLayout.setVisibility(LinearLayout.VISIBLE);
		paranoidLayout.setVisibility(LinearLayout.GONE);
		progressLayout.setVisibility(LinearLayout.GONE);
	}

	public void setToParanoid(View v) {
		Log.d(DEBUG_TAG, "Set to paranoid");

		switchNormal.setActivated(false);
		switchParanoid.setActivated(true);

		switchLayout.setVisibility(LinearLayout.VISIBLE);
		normalLayout.setVisibility(LinearLayout.GONE);
		paranoidLayout.setVisibility(LinearLayout.VISIBLE);
		progressLayout.setVisibility(LinearLayout.GONE);

	}

	public void InitialSetup() {
		File f = new File(Environment.getExternalStorageDirectory() + "/MDroid");
		if (!f.exists())
			f.mkdir();
	}
}
