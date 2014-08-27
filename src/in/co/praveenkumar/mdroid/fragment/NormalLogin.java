package in.co.praveenkumar.mdroid.fragment;

import in.co.praveenkumar.mdroid.apis.R;
import in.co.praveenkumar.mdroid.moodlemodel.MoodleSiteInfo;
import in.co.praveenkumar.mdroid.moodlemodel.MoodleToken;
import in.co.praveenkumar.mdroid.moodlerest.MoodleRestToken;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

public class NormalLogin extends Fragment {
	Boolean DEBUGGING_MODE = false;
	EditText usernameET;
	EditText passwordET;
	EditText murlET;
	Button loginButton;
	ScrollView loginProgressLL;
	TextView loginProgressTV;
	Context ctx;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.normal_login, container,
				false);
		setUpWidgets(rootView);
		ctx = getActivity();

		loginButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				doNormalLogin();
			}
		});

		return rootView;
	}

	private void setUpWidgets(View rootView) {
		usernameET = (EditText) rootView
				.findViewById(R.id.login_normal_username);
		passwordET = (EditText) rootView
				.findViewById(R.id.login_normal_password);
		murlET = (EditText) rootView.findViewById(R.id.login_normal_url);
		loginButton = (Button) rootView.findViewById(R.id.login_normal_login);
		loginProgressLL = (ScrollView) rootView
				.findViewById(R.id.login_progress_layout);
		loginProgressTV = (TextView) rootView
				.findViewById(R.id.login_progress_message);
	}

	private void doNormalLogin() {
		String username = usernameET.getText().toString();
		String password = passwordET.getText().toString();
		String mUrl = murlET.getText().toString();

		MoodleSiteInfo msf = new MoodleSiteInfo("Praveen");
		msf.save();

		long i = 2;
		MoodleSiteInfo site = MoodleSiteInfo.findById(MoodleSiteInfo.class, i);
		System.out.println("Site info: " + site.getFirstname());

		new asyncNormalLogin(username, password, mUrl).execute("");
	}

	private class asyncNormalLogin extends AsyncTask<String, Integer, Boolean> {
		String uname;
		String pwd;
		String mUrl;
		String progress = "";

		public asyncNormalLogin(String uname, String pwd, String mUrl) {
			this.uname = uname;
			this.pwd = pwd;
			this.mUrl = mUrl;
		}

		@Override
		protected void onPreExecute() {
			loginButton.setText("Logging in..");
			loginButton.setEnabled(false);
			loginProgressLL.setVisibility(ScrollView.VISIBLE);
		}

		@Override
		protected void onProgressUpdate(Integer... params) {
			loginProgressTV.setText(progress);
		}

		@Override
		protected Boolean doInBackground(String... params) {
			updateProgress("Fetching token");
			MoodleRestToken mrt = new MoodleRestToken(uname, pwd, mUrl);
			MoodleToken mt = mrt.getToken();

			if (mt.getToken() == null) {
				updateProgress("Token fetching failed!");

				// Print the errors from moodle
				if (mt.getError() != null) {
					updateProgress("\nError: " + mt.getError());
					updateProgress("Moodle url: " + mt.getReproductionlink());
					/*
					 * -TODO- Set this debugging mode flag as app setting
					 */
					if (DEBUGGING_MODE) {
						updateProgress("Stacktrace: " + mt.getStacktrace());
						updateProgress("Debug info: " + mt.getDebuginfo());
					}
				}

				// Print non-moodle errors
				else {
					updateProgress("\nError:\n" + mrt.getErrorsString());
				}

				return null;
			}

			// Fetch site info
			updateProgress("Token obtained\n Fetching site info\n");

			return null;
		}

		private void updateProgress(String status) {
			progress += status + "\n";
			publishProgress(0);
		}

	}

}
