package in.co.praveenkumar.mdroid.networking;

import in.co.praveenkumar.mdroid.MainActivity;
import in.co.praveenkumar.mdroid.parser.FilesInResoucesParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.util.Log;

public class FetchResourceFiles {
	private final String DEBUG_TAG = "NETWORKING_FETCH_RESOURCE_FILES";
	private String mURL = MainActivity.mURL;
	private ArrayList<String> rFileIDs = new ArrayList<String>();
	private ArrayList<String> rFileNames = new ArrayList<String>();
	private int nFiles;

	public void fetchFiles(String cId) {
		String html = fetchFilesHtml(cId);
		FilesInResoucesParser FRP = new FilesInResoucesParser(html);
		rFileIDs = FRP.getFileIds();
		rFileNames = FRP.getFileNames();
		nFiles = FRP.getFilesCount();
	}

	private String fetchFilesHtml(String cId) {
		String html = "";
		try {
			// Get default app client. Only that has the cookie data
			DefaultHttpClient httpclient = MainActivity.httpclient;

			// Fetch thread page
			HttpGet httpgetCourse = new HttpGet(mURL + "/course/view.php?id="
					+ cId);
			HttpResponse response = httpclient.execute(httpgetCourse);
			InputStream is = response.getEntity().getContent();

			// Read content from stream
			String line = "";
			StringBuilder total = new StringBuilder();
			BufferedReader rd = new BufferedReader(new InputStreamReader(is));

			// Read response until the end
			while ((line = rd.readLine()) != null) {
				total.append(line);
			}
			html = total.toString();

		} catch (MalformedURLException e) {
			// URL malformed
			e.printStackTrace();
			Log.d(DEBUG_TAG, "Malformed URL");
			MainActivity.toaster.showToast("Malformed Moodle url.");
		} catch (IOException e) {
			// Error while making connection
			e.printStackTrace();
			Log.d(DEBUG_TAG, "IOException ! Net problem ?");
			MainActivity.toaster.showToast("No connection.");
		}
		return html;

	}

	public ArrayList<String> getFileIds() {
		return rFileIDs;
	}

	public ArrayList<String> getFileNames() {
		return rFileNames;
	}

	public int getFilesCount() {
		return nFiles;
	}

}
