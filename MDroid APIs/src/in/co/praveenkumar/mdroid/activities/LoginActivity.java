package in.co.praveenkumar.mdroid.activities;

import in.co.praveenkumar.mdroid.apis.R;
import in.co.praveenkumar.mdroid.helpers.Database;
import in.co.praveenkumar.mdroid.moodlerest.MoodleToken;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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
		setContentView(R.layout.login_activity);
		setUpWidgets();
	}

	private void setUpWidgets() {
		switchNormal = (Button) findViewById(R.id.switch_normal);
		switchParanoid = (Button) findViewById(R.id.switch_paranoid);

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
				publishProgress("Token received.", "Login success!");
				db.setToken(token);
				isLogged = true;
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

		normalLayout.setVisibility(LinearLayout.VISIBLE);
		paranoidLayout.setVisibility(LinearLayout.GONE);
	}

	public void setToParanoid(View v) {
		Log.d(DEBUG_TAG, "Set to paranoid");

		switchNormal.setActivated(false);
		switchParanoid.setActivated(true);

		normalLayout.setVisibility(LinearLayout.GONE);
		paranoidLayout.setVisibility(LinearLayout.VISIBLE);

	}

}
