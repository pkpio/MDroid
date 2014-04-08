/**************************************************************
 * Author: 	Praveen Kumar Pendyala
 * Project: MDroid
 * Created: 27th May, 2014.
 * License: http://creativecommons.org/licenses/by-nc-sa/3.0/
 **************************************************************/

package in.co.praveenkumar.mdroid.moodlerest;

import in.co.praveenkumar.mdriod.parsers.TokenParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import android.util.Log;

public class MoodleToken {
	private final String DEBUG_TAG = "MoodleToken";
	private String uname;
	private String pswd;
	private String url;
	private String token;
	private ArrayList<String> errors = new ArrayList<String>();

	public MoodleToken(String uname, String pswd, String baseUrl) {
		this.uname = uname;
		this.pswd = pswd;
		this.url = baseUrl + "/login/token.php";

	}

	public String getToken() {
		String urlParams = "";

		// set required parameters for token url
		try {
			urlParams = "username=" + URLEncoder.encode(uname, "UTF-8")
					+ "&password=" + URLEncoder.encode(pswd, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			Log.d(DEBUG_TAG, "credential encoding failed!");
			e.printStackTrace();
		}

		// Check if MDroid service in enabled.
		getTokenForService(urlParams, MoodleRestOptions.SERVICE_MDROID);

		// Before going for below services check authentication didn't fail
		// as it would fail in all cases if it failed above.
		if (token == null && !errors.get(0).contains("Authentication failed")) {
			// If above returns no token, try Moody service
			if (token == null)
				getTokenForService(urlParams, MoodleRestOptions.SERVICE_MOODY);

			// If above returns no token, try Moodle mobile service
			if (token == null)
				getTokenForService(urlParams,
						MoodleRestOptions.SERVICE_MOODLE_MOBILE);
		}

		return token;
	}

	public String getCustomServiceToken(String serviceName) {
		String urlParams = "";

		// set required parameters for token url
		try {
			urlParams = "username=" + URLEncoder.encode(uname, "UTF-8")
					+ "&password=" + URLEncoder.encode(pswd, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			Log.d(DEBUG_TAG, "credential encoding failed!");
			e.printStackTrace();
		}

		// Request for custom service token.
		getTokenForService(urlParams, serviceName);

		return token;
	}

	private void getTokenForService(String urlParams, String serviceName) {

		HttpURLConnection con;
		try {
			con = (HttpURLConnection) new URL(url + "?" + urlParams
					+ "&service=" + serviceName).openConnection();
			con.setRequestProperty("Accept", "application/xml");
			con.setRequestProperty("Content-Language", "en-US");
			con.setDoOutput(true);

			OutputStreamWriter writer = new OutputStreamWriter(
					con.getOutputStream());
			writer.write("");
			writer.flush();
			writer.close();

			// Get Response
			StringBuilder buffer = new StringBuilder();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					con.getInputStream()));
			String line;
			while ((line = reader.readLine()) != null) {
				buffer.append(line);
				buffer.append('\r');
			}
			reader.close();

			System.out.println(buffer.toString());

			// Parse for token
			TokenParser tp = new TokenParser(buffer.toString());
			if (tp.getToken() != null)
				token = tp.getToken();
			// Write to errors list an error for this service
			else {
				String error = tp.getError();
				if (error.contains(MoodleRestOptions.RESPONSE_AUTH_FAILED))
					errors.add("Authentication failed. Username and password didn't match.");
				else
					errors.add(serviceName + " : " + tp.getError());

			}

		} catch (Exception e) {
			errors.add(serviceName + " : " + e.getMessage());
		}

	}

	public ArrayList<String> getErrors() {
		return errors;
	}

	public String getErrorsString() {
		String error = "";
		for (int i = 0; i < errors.size(); i++) {
			error += errors.get(i) + "\n\n";
		}

		return error;
	}
}
