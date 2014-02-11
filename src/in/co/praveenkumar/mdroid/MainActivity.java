/*
 * Author: 	Praveen Kumar Pendyala
 * Project: MDroid
 * Created:	28-12-2013
 * 
 * © 2013, Praveen Kumar Pendyala. 
 * Licensed under the Creative Commons Attribution-NonCommercial-ShareAlike 
 * 3.0 Unported license, http://creativecommons.org/licenses/by-nc-sa/3.0/ 
 * 
 * This is a part of the MDroid project. You may use the contents of this file
 * or the project only if you comply with license of the project, available at the 
 * Github repo: https://github.com/praveendath92/MDroid/blob/master/README.md
 * 
 */

package in.co.praveenkumar.mdroid;

import in.co.praveenkumar.R;
import in.co.praveenkumar.mdroid.helpers.AppsHttpClient;
import in.co.praveenkumar.mdroid.helpers.BaseActivity;
import in.co.praveenkumar.mdroid.helpers.Database;
import in.co.praveenkumar.mdroid.helpers.Toaster;
import in.co.praveenkumar.mdroid.networking.DoLogin;
import in.co.praveenkumar.mdroid.services.MDroidService;
import in.co.praveenkumar.mdroid.sqlite.databases.SqliteTbCourses;

import org.apache.http.impl.client.DefaultHttpClient;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends BaseActivity {
	private final String DEBUG_TAG = "MDroid Main activity";
	public static String mURL;
	public static DefaultHttpClient httpclient;
	public static Toaster toaster;
	private EditText uNameET;
	private EditText pswdET;
	private Database db;
	private ProgressDialog loginDialog;
	private DoLogin l;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.loginpage);

		// Testing
		SqliteTbCourses stc = new SqliteTbCourses(this);
		stc.favCourse(2603 + "");

		// Setup a http client for all network tasks, URL
		AppsHttpClient ahc = new AppsHttpClient();
		httpclient = ahc.getHttpClient();
		db = new Database(getApplicationContext());
		mURL = db.getURL();

		// Start service
		Log.d(DEBUG_TAG, "Registering service");
		// Intent intent = new Intent(this, MDroidService.class);
		// startService(intent);

		// Setup other helpers
		LayoutInflater inflater = getLayoutInflater();
		View layout = inflater.inflate(R.layout.toast_layout,
				(ViewGroup) findViewById(R.id.toast_layout_root));
		toaster = new Toaster(getApplicationContext(), layout);
		l = new DoLogin();

		// Setup widgets and their values
		uNameET = (EditText) findViewById(R.id.username);
		pswdET = (EditText) findViewById(R.id.pswd);
		uNameET.setText(db.getLDAP());
		pswdET.setText(db.getPswd());

		Button offlineBTN = (Button) findViewById(R.id.offline);
		Button loginBTN = (Button) findViewById(R.id.login);

		// setup OnClickListeners
		offlineBTN.setOnClickListener(OfflineBtnListen);
		loginBTN.setOnClickListener(LoginBtnListen);

		// Setup login dialog
		loginDialog = new ProgressDialog(MainActivity.this);
		loginDialog.setMessage("Logging in..Please wait..!");
		loginDialog.setIndeterminate(true);
		loginDialog.setCancelable(false);

		// Check if auto login enabled and take action
		if (db.getAutoLoginState())
			if (!uNameET.getText().toString().contentEquals("")
					&& !pswdET.getText().toString().contentEquals(""))
				new tryAsyncLogin().execute(uNameET.getText().toString(),
						pswdET.getText().toString());
			else
				MainActivity.toaster.showToast("Fill all values.");
	}

	private OnClickListener OfflineBtnListen = new OnClickListener() {
		public void onClick(View v) {
			openOffline();
		}
	};

	private void openOffline() {
		Intent i = new Intent(this, OfflineCoursesActivity.class);
		startActivityForResult(i, 1);
	}

	private OnClickListener LoginBtnListen = new OnClickListener() {
		public void onClick(View v) {
			// Save values according to AutoSave state
			// 0 - only uname; 1 - both; 2 - none
			int AUS = db.getAutoSaveState();
			switch (AUS) {
			case 0:
				db.setLDAP(uNameET.getText().toString());
				db.setPswd("");
				break;
			case 1:
				db.setLDAP(uNameET.getText().toString());
				db.setPswd(pswdET.getText().toString());
				break;
			case 2:
				db.setLDAP("");
				db.setPswd("");
				break;
			}

			if (!uNameET.getText().toString().contentEquals("")
					&& !pswdET.getText().toString().contentEquals(""))
				new tryAsyncLogin().execute(uNameET.getText().toString(),
						pswdET.getText().toString());
			else
				MainActivity.toaster.showToast("Fill all values.");
		}
	};

	private class tryAsyncLogin extends AsyncTask<String, Integer, Long> {
		private int respCode;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			loginDialog.show();
		}

		protected Long doInBackground(String... credentials) {
			respCode = l.doLogin(credentials[0], credentials[1]);
			return null;
		}

		protected void onPostExecute(Long result) {
			try {
				loginDialog.dismiss();
			} catch (IllegalArgumentException e) {

			}
			checkLogin(respCode);
		}
	}

	private void checkLogin(int respCode) {
		if (l.isLoggedIn()) {
			// Intent with courses
			Intent i = new Intent(this, CoursesActivity.class);
			i.putExtra("html", l.getContent());
			startActivityForResult(i, 1);
		} else {
			switch (respCode) {
			case 0:
				if (l.getLoginError() == 3)
					toaster.showToast("Incorrect credentials");
				else {
					// Intent with error code.
					toaster.showToast("Login failed. Error code: "
							+ l.getLoginError());
					Intent i = new Intent(this, ErrorActivity.class);
					i.putExtra("errCode", l.getLoginError());
					startActivityForResult(i, 1);
				}

				break;
			case 1:
				// malformed url
				toaster.showToast("Malformed Moodle address.");
				break;
			case 2:
				// Connection issue
				toaster.showToast("No connection.");
				break;
			}
		}
	}

}
