package in.co.praveenkumar.mdroid.moodlerest;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.util.Log;

public class MoodleRestCall {
	private final String DEBUG_TAG = "MoodleRestCall";

	/**
	 * Executes the Moodle Rest call with the set parameters
	 * 
	 * @param restUrl
	 *            Moodle url to make rest calls
	 * @param params
	 *            Params for the rest call
	 * @return InputStreamReader to read the content from
	 * 
	 * @author Praveen Kumar Pendyala (praveen@praveenkumar.co.in)
	 */
	public InputStreamReader fetchContent(String restUrl, String params) {

		Log.d(DEBUG_TAG, restUrl + params);
		HttpURLConnection con;
		try {
			con = (HttpURLConnection) new URL(restUrl + params)
					.openConnection();
			con.setRequestMethod("POST");
			con.setRequestProperty("Accept", "application/xml");
			con.setRequestProperty("Content-Language", "en-US");
			con.setDoOutput(true);

			OutputStreamWriter writer = new OutputStreamWriter(
					con.getOutputStream());
			writer.write("");
			writer.flush();
			writer.close();

			return new InputStreamReader(con.getInputStream());
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

}