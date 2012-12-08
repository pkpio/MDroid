package in.co.praveenkumar;
//Importing required libraries

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

//(re)-defining our Activity's class
public class MDroidActivity extends Activity {

	/** Called when the activity is first created. */
	private static final int REQUEST_CODE = 10;
	String htmldataString = "";
	ProgressDialog loginDialog;
	Dialog dataWarningDialog;
	SchemeRegistry schreg = new SchemeRegistry();
	String firstLogin;
	public static String serverAddress;
	AppPreferences appPrefs;

	public static DefaultHttpClient httpclient;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		// For getting Preferences
		appPrefs = new AppPreferences(getApplicationContext());

		HttpParams httpParams = new BasicHttpParams();
		// set timeout in milliseconds until a connection is established
		int timeoutConnection = 30000;
		HttpConnectionParams.setSoTimeout(httpParams, timeoutConnection);

		schreg.register(new Scheme("http", PlainSocketFactory
				.getSocketFactory(), 80));
		schreg.register(new Scheme("https", PlainSocketFactory
				.getSocketFactory(), 80));
		ClientConnectionManager cm = new ThreadSafeClientConnManager(
				httpParams, schreg);
		httpclient = new DefaultHttpClient(cm, httpParams);

		// Setting up dialogs
		loginDialog = new ProgressDialog(MDroidActivity.this);
		loginDialog.setMessage("logging in..please wait....!");
		loginDialog.setIndeterminate(true);

		// Setting up onClickListeners....
		Button loginButton = (Button) findViewById(R.id.loginButton);
		loginButton.setOnClickListener(loginButtonListener);

		Button userOfflineButton = (Button) findViewById(R.id.userOfflineButton);
		userOfflineButton.setOnClickListener(userOfflineButtonListener);

		// Filling fields with values from preferences...
		// getting preferences
		SharedPreferences prefsGet = getPreferences(Context.MODE_PRIVATE);
		String userNamePref = prefsGet.getString("username", "");
		String passwordPref = prefsGet.getString("password", "");
		firstLogin = prefsGet.getString("firstLogin", "1");
		serverAddress = prefsGet.getString("serverAddress",
				"http://moodle.iitb.ac.in");
		Boolean dataWarningDialogStatus = prefsGet.getBoolean(
				"dataWarningDialogStatus", true);

		if ((userNamePref.compareTo("") == 0)
				|| (passwordPref.compareTo("") == 0))
			dataWarningPopup(dataWarningDialogStatus);

		// filling them in the fields
		EditText usernameRaw = (EditText) findViewById(R.id.usernameEditText);
		EditText passwordRaw = (EditText) findViewById(R.id.passwordEditText);
		usernameRaw.setText(userNamePref);
		passwordRaw.setText(passwordPref);

		if ((userNamePref.compareTo("") != 0)
				&& (passwordPref.compareTo("") != 0)) {
			Toast.makeText(getBaseContext(),
					"Taking autologin with saved credentials",
					Toast.LENGTH_SHORT).show();
			new tryAsyncLogin().execute(userNamePref, passwordPref);
		}

	}

	public void dataWarningPopup(Boolean dataWarningDialogStatus) {

		if (dataWarningDialogStatus) {
			dataWarningDialog = new Dialog(MDroidActivity.this);
			dataWarningDialog.setContentView(R.layout.datawarningpopup);
			dataWarningDialog.setTitle("Notice!");

			final CheckBox dataWarningStatusCheckBox = (CheckBox) dataWarningDialog
					.findViewById(R.id.dataWarningDialogStatus);
			Button dismissButton = (Button) dataWarningDialog
					.findViewById(R.id.dataWarningDialogDismiss);

			dismissButton.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					Boolean dataWarningStatusCheckBoxStatus = dataWarningStatusCheckBox
							.isChecked();
					if (dataWarningStatusCheckBoxStatus) {
						// Saving preference...
						SharedPreferences prefs = getPreferences(Context.MODE_PRIVATE);
						SharedPreferences.Editor editor = prefs.edit();
						editor.putBoolean("dataWarningDialogStatus", false);
						editor.commit();
					}
					dataWarningDialog.dismiss();
				}
			});
			dataWarningDialog.show();

		}

	}

	private OnClickListener loginButtonListener = new OnClickListener() {
		public void onClick(View v) {
			// calling tryAsyncLogin function
			// Getting userInputs into Java variables
			EditText usernameRaw = (EditText) findViewById(R.id.usernameEditText);
			EditText passwordRaw = (EditText) findViewById(R.id.passwordEditText);
			CheckBox passwordPref = (CheckBox) findViewById(R.id.passwordPrefcheckBox);

			// Converting this into strings as the above ones are just raw
			// values (I'm calling it so :P)
			String username = usernameRaw.getText().toString();
			String password = passwordRaw.getText().toString();
			boolean passwordSaving = passwordPref.isChecked();

			// Saving username password preferences...
			SharedPreferences prefs = getPreferences(Context.MODE_PRIVATE);
			SharedPreferences.Editor editor = prefs.edit();
			editor.putString("username", username);

			if (passwordSaving)
				editor.putString("password", password);

			editor.commit();

			if ((username.compareTo("") != 0) && (password.compareTo("") != 0)) {
				new tryAsyncLogin().execute(username, password);
			} else {
				Toast.makeText(getBaseContext(), "Incomplete fields!",
						Toast.LENGTH_SHORT).show();
			}
		}
	};

	private OnClickListener userOfflineButtonListener = new OnClickListener() {
		public void onClick(View v) {
			// check if file exists!
			String file = android.os.Environment.getExternalStorageDirectory()
					.getPath() + "/MDroid";
			File f = new File(file);
			if (f.exists()) {
				userOfflineIntentOpen();
			} else {
				Toast.makeText(getBaseContext(),
						"You must login atleast once to use this feature!",
						Toast.LENGTH_LONG).show();
			}
		}
	};

	/* AsycTask Thread */

	private class tryAsyncLogin extends AsyncTask<String, Integer, Long> {
		protected Long doInBackground(String... credentials) {

			try {
				postData(credentials[0], credentials[1]);
			} catch (ClientProtocolException e) {

			} catch (IOException e) {

			}
			return null;
		}

		protected void onProgressUpdate(Integer... progress) {

		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			loginDialog.show();
		}

		protected void onPostExecute(Long result) {
			// Call new intent function here..
			try {
				loginDialog.dismiss();
			} catch (Exception e) {
				// nothing
			}
			changeLoginandChangeIntent();
		}

	}

	public void userOfflineIntentOpen() {
		Intent j = new Intent(this, userOfflineFolderListing.class);
		startActivityForResult(j, 11);
	}

	public void postData(String username, String password)
			throws ClientProtocolException, IOException {

		HttpGet httpget = new HttpGet(
				"http://home.iitb.ac.in/~praveendath92/MDroidStats/increaseCount.php?firstTime="
						+ firstLogin);

		HttpResponse response = httpclient.execute(httpget);
		HttpEntity entity = response.getEntity();

		if (entity != null) {
			entity.consumeContent();
		}

		HttpGet httpgetStats = new HttpGet(serverAddress + "/login/index.php");

		HttpResponse responseStats = httpclient.execute(httpgetStats);
		HttpEntity entityStats = responseStats.getEntity();

		if (entityStats != null) {
			entityStats.consumeContent();
		}

		HttpPost httpost = new HttpPost(serverAddress + "/login/index.php");

		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("username", username));
		nvps.add(new BasicNameValuePair("password", password));

		httpost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));

		response = httpclient.execute(httpost);
		entity = response.getEntity();

		try {
			inputStreamToString(response.getEntity().getContent());
		} catch (IOException e) {
		}

		if (entity != null) {
			entity.consumeContent();
		}
	}

	private void inputStreamToString(InputStream is) throws IOException {
		String line = "";
		StringBuilder total = new StringBuilder();

		// Wrap a BufferedReader around the InputStream
		BufferedReader rd = new BufferedReader(new InputStreamReader(is));

		// Read response until the end
		while ((line = rd.readLine()) != null) {
			total.append(line);
		}

		htmldataString = total.toString();
	}

	public void changeLoginandChangeIntent() {

		// Checking if login is successful...
		int prevIndex = 0;

		if (htmldataString != null) {

			prevIndex = htmldataString.indexOf(
					"<div class=\"logininfo\">You are logged in as", prevIndex);
			if (prevIndex == -1) {
				// Login failed!
				Toast.makeText(getBaseContext(), "login failed!",
						Toast.LENGTH_LONG).show();
				// make 1st login 0
				SharedPreferences prefs = getPreferences(Context.MODE_PRIVATE);
				SharedPreferences.Editor editor = prefs.edit();
				editor.putString("firstLogin", "0");
			} else {
				// make 1st login 0
				SharedPreferences prefs = getPreferences(Context.MODE_PRIVATE);
				SharedPreferences.Editor editor = prefs.edit();
				editor.putString("firstLogin", "0");
				Intent i = new Intent(this, courseListing.class);
				i.putExtra("htmlData", htmldataString);
				startActivityForResult(i, REQUEST_CODE);
			}
		} else {
			Toast.makeText(getBaseContext(),
					"Please check your internet connectivity",
					Toast.LENGTH_LONG).show();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.AboutMDroid:
			showDialog(0);

			break;
		case R.id.Credits:
			showDialog(1);

			break;
		case R.id.HelpMenu:
			showDialog(2);

			break;
		case R.id.ChangeServer:
			showDialog(3);

			break;
		case R.id.Rating:
			showDialog(4);

			break;
		}
		return true;
	}

	public Dialog onCreateDialog(int id) {
		final Dialog dialog = new Dialog(this);

		switch (id) {
		case 0:
			dialog.setContentView(R.layout.aboutmdroid);
			dialog.setTitle("About MDroid");
			break;

		case 1:
			dialog.setContentView(R.layout.credits);
			dialog.setTitle("Credits");
			break;

		case 2:
			dialog.setContentView(R.layout.mdroidhelp);
			dialog.setTitle("Help");
			break;

		case 4:
			dialog.setContentView(R.layout.rating);
			dialog.setTitle("Rate MDroid");

			Button submitRatingButton = (Button) dialog
					.findViewById(R.id.submitRating);

			submitRatingButton.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					RatingBar mBar = (RatingBar) dialog
							.findViewById(R.id.ratingBar);
					int[] i = new int[] { (int) mBar.getRating() };

					// Saving prefs
					appPrefs.saveIntPrefs("rated", 1);

					if (i[0] <= 3) {
						Toast.makeText(getBaseContext(),
								"submiting " + i[0] + " star rating...",
								Toast.LENGTH_SHORT).show();
						dialog.dismiss();
					} else {
						dialog.dismiss();
						Intent intent = new Intent(Intent.ACTION_VIEW);
						intent.setData(Uri
								.parse("market://details?id=in.co.praveenkumar"));
						startActivity(intent);
					}

					// statusMessage.setText("value is " + i[0]);
				}
			});
			break;

		case 3:
			dialog.setContentView(R.layout.changeserver);
			dialog.setTitle("change server");

			final EditText changeServerEdittextValue = (EditText) dialog
					.findViewById(R.id.changeServerEditText);
			changeServerEdittextValue.setText(serverAddress);

			Button changeServerValueButton = (Button) dialog
					.findViewById(R.id.changeServerValue);
			Button resetServerValueButton = (Button) dialog
					.findViewById(R.id.resetServerValue);

			changeServerValueButton.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					String newServervalue = changeServerEdittextValue.getText()
							.toString();
					// Saving in preferences..
					SharedPreferences prefs = getPreferences(Context.MODE_PRIVATE);
					SharedPreferences.Editor editor = prefs.edit();
					editor.putString("serverAddress", newServervalue);
					editor.commit();
					serverAddress = newServervalue;
					dialog.dismiss();

					Toast.makeText(getBaseContext(),
							"Server prefernce saved to \n" + serverAddress,
							Toast.LENGTH_SHORT).show();
				}
			});

			resetServerValueButton.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					changeServerEdittextValue
							.setText("http://moodle.iitb.ac.in");
					// Saving in preferences..
					SharedPreferences prefs = getPreferences(Context.MODE_PRIVATE);
					SharedPreferences.Editor editor = prefs.edit();
					editor.putString("serverAddress",
							"http://moodle.iitb.ac.in");
					editor.commit();
					serverAddress = "http://moodle.iitb.ac.in";
					dialog.dismiss();

					Toast.makeText(
							getBaseContext(),
							"Server preferences reseted to \nhttp://moodle.iitb.ac.in",
							Toast.LENGTH_SHORT).show();
				}
			});

			break;
		}
		return dialog;
	}
}