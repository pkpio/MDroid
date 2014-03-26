package in.co.praveenkumar.mdroid.moodlerest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import android.util.Log;

public class MoodleToken {
	private final String DEBUG_TAG = "MoodleToken";
	private String uname;
	private String pswd;
	private String url;
	private String token;

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

		// If above returns no token, try Moody service
		if (token == null)
			getTokenForService(urlParams, MoodleRestOptions.SERVICE_MOODY);

		// If above returns no token, try Moodle mobile service
		if (token == null)
			getTokenForService(urlParams,
					MoodleRestOptions.SERVICE_MOODLE_MOBILE);

		return token;
	}

	private String getTokenForService(String urlParams, String serviceName) {
		String error = "";

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
			if (!buffer.toString().contains("Web service is not available"))
				token = buffer.toString();
			System.out.println(buffer.toString());
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return error;

	}
}
