package in.co.praveenkumar.mdroid.networking;

import in.co.praveenkumar.mdroid.MainActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.util.Log;

public class DoLogin {
	private final String DEBUG_TAG = "NETWORKING_LOGIN";
	private static DefaultHttpClient httpclient;
	private String htmlData = "";
	private CookieStore cookieStore;
	private String getCookie;
	private String postCookie;

	public DoLogin() {
		httpclient = MainActivity.httpclient;
		cookieStore = httpclient.getCookieStore();
	}

	public Boolean isLoggedIn() {
		Boolean status = false;
		if (htmlData.contains("You are logged in"))
			status = true;
		return status;
	}

	public int doLogin(String uName, String pswd) {
		int respCode = 0; // used only when login failed.
		Log.d(DEBUG_TAG, "Started");
		// Get server URL
		String mURL = MainActivity.mURL;
		Log.d(DEBUG_TAG, "URL:" + mURL);

		HttpGet httpget = new HttpGet(mURL + "/login/index.php");
		HttpPost httppost = new HttpPost(mURL + "/login/index.php");

		try {
			// Do a HttpGet first. This is to pass cookies check test.
			httpclient.execute(httpget);
			Log.d(DEBUG_TAG, "GET done");

			// Check for value of MoodleSession cookie.
			// This changes on successful login.
			if (!cookieStore.getCookies().isEmpty())
				getCookie = cookieStore.getCookies().get(0).getValue();

			// Now POST request to do login
			// NameValuePairs for POST data holding
			List<NameValuePair> dataForUrl = new ArrayList<NameValuePair>();
			dataForUrl.add(new BasicNameValuePair("username", uName));
			dataForUrl.add(new BasicNameValuePair("password", pswd));

			// Add the NVPs to HTTP post request
			httppost.setEntity(new UrlEncodedFormEntity(dataForUrl));

			// Execute HTTP Post Request
			HttpResponse response = httpclient.execute(httppost);
			InputStream is = response.getEntity().getContent();

			// Read content from stream
			String line = "";
			StringBuilder total = new StringBuilder();
			BufferedReader rd = new BufferedReader(new InputStreamReader(is));

			// Read response until the end
			while ((line = rd.readLine()) != null) {
				total.append(line);
			}
			htmlData = total.toString();
			Log.d(DEBUG_TAG, "POST done");

			// POST method cookie.
			if (!cookieStore.getCookies().isEmpty())
				postCookie = cookieStore.getCookies().get(0).getValue();

		} catch (MalformedURLException e) {
			// URL malformed
			e.printStackTrace();
			Log.d(DEBUG_TAG, "Malformed URL");
			respCode = 1;
			MainActivity.toaster.showToast("Please check Moodle address.");
		} catch (IOException e) {
			// Error while making connection
			e.printStackTrace();
			Log.d(DEBUG_TAG, "IOException ! Net problem ?");
			respCode = 2;
			MainActivity.toaster.showToast("No connection.");
		} catch (IllegalStateException e) {
			// URL has error.
			e.printStackTrace();
			Log.d(DEBUG_TAG, "Illegal state exception");
			MainActivity.toaster.showToast("Please check Moodle address.");
			respCode = 1;
		} catch (IllegalArgumentException e) {
			// URL has error.
			e.printStackTrace();
			Log.d(DEBUG_TAG, "Illegal argument exception");
			MainActivity.toaster
					.showToast("URL error. Any extra spaces in your address ?");
			respCode = 1;
		}

		return respCode;
	}

	public String getContent() {
		return htmlData;
	}

	public int getLoginError() {
		int errVal = 0;

		if (getCookie != null && postCookie != null)
			if (getCookie.contentEquals(postCookie))
				// Incorrect credentials. Not MDroid problem.
				errVal = 3;
			else
				// Correct credentials. MDroid issue
				errVal = 1;

		if (htmlData.contains("You are logged in"))
			// No courses detected case
			errVal = 2;

		return errVal;
	}

}
