package in.co.praveenkumar.mdroid.moodlerest;

import in.co.praveenkumar.mdroid.moodlemodel.MoodleToken;

import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class MoodleRestToken {
	private final String DEBUG_TAG = "MoodleToken";
	private String uname;
	private String pswd;
	private String url;
	private MoodleToken token = new MoodleToken();

	public MoodleRestToken(String uname, String pswd, String baseUrl) {
		this.uname = uname;
		this.pswd = pswd;
		this.url = baseUrl + "/login/token.php";

	}

	/**
	 * Tries 3 different web service and returns a token for the given username
	 * and password combination <br/>
	 * <br/>
	 * If there were errors, they can be checked in the error field of the token
	 * object
	 * 
	 * @return MoodleToken object
	 * 
	 * @author Praveen Kumar Pendyala (praveen@praveenkumar.co.in)
	 * 
	 */
	public MoodleToken getToken() {
		String urlParams = "";

		// set required parameters for token url
		try {
			urlParams = "username=" + URLEncoder.encode(uname, "UTF-8")
					+ "&password=" + URLEncoder.encode(pswd, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			Log.d(DEBUG_TAG, "credential encoding failed!");
			e.printStackTrace();
		}

		// Check MDroid service.
		getTokenForService(urlParams, MoodleRestOption.SERVICE_MDROID);

		// Check Moody service if above failed.
		if (token.getToken() == null)
			getTokenForService(urlParams, MoodleRestOption.SERVICE_MOODY);

		// Check Moodle mobile service if above failed.
		if (token.getToken() == null)
			getTokenForService(urlParams,
					MoodleRestOption.SERVICE_MOODLE_MOBILE);

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
			Reader reader = new InputStreamReader(con.getInputStream());

			Gson gson = new GsonBuilder().create();
			token = gson.fromJson(reader, MoodleToken.class);
			reader.close();

		} catch (Exception e) {
			token.appenedError("\n" + serviceName + " : " + e.getMessage());
		}

	}

	/**
	 * Get token for a custom service name
	 * 
	 * @param serviceName
	 * @return token object
	 * 
	 * @author Praveen Kumar Pendyala (praveen@praveenkumar.co.in)
	 */
	public MoodleToken getCustomServiceToken(String serviceName) {
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
}
