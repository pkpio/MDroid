package in.co.praveenkumar.mdroid.networking;

import in.co.praveenkumar.mdroid.MainActivity;
import in.co.praveenkumar.mdroid.parser.ForumParser;

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

public class FetchForum {
	private final String DEBUG_TAG = "NETWORKING_FORUM";
	private String mURL = MainActivity.mURL;
	private ArrayList<String> forumThreadIds = new ArrayList<String>();
	private ArrayList<String> forumThreadSubs = new ArrayList<String>();
	private ArrayList<String> forumThreadReplyCounts = new ArrayList<String>();
	private ArrayList<String> forumThreadAuthors = new ArrayList<String>();
	private ArrayList<String> forumThreadDates = new ArrayList<String>();
	private int nForumThreads = 0;
	private ForumParser forumparser = new ForumParser();

	public void fetchForum(String cId) {
		String fId = getForumId(cId);
		fetchForumContent(fId);
	}

	private String getForumId(String cID) {
		String fId = "";
		try {
			// Get default app client. Only that has the cookie data
			DefaultHttpClient httpclient = MainActivity.httpclient;

			// Fetch course page to parse out forum id
			HttpGet httpgetCourse = new HttpGet(mURL
					+ "/mod/forum/index.php?id=" + cID);
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
			String html = total.toString();

			// Parse and extract forum id from content
			fId = forumparser.getForumId(html);

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
		return fId;
	}

	private void fetchForumContent(String fId) {
		try {
			// Get default app client
			DefaultHttpClient httpclient = MainActivity.httpclient;

			// Fetch forum content
			HttpGet httpgetForum = new HttpGet(mURL + "/mod/forum/view.php?f="
					+ fId);
			HttpResponse response = httpclient.execute(httpgetForum);
			InputStream is = response.getEntity().getContent();

			// Read content from stream
			String line = "";
			StringBuilder total = new StringBuilder();
			BufferedReader rd = new BufferedReader(new InputStreamReader(is));
			while ((line = rd.readLine()) != null) {
				total.append(line);
			}
			String html = total.toString();

			// Fill ArrayLists by parsing values
			forumparser.ParseForumForData(html);
			forumThreadIds = forumparser.getThreadIds();
			forumThreadSubs = forumparser.getThreadSubjects();
			forumThreadAuthors = forumparser.getThreadAuthors();
			forumThreadDates = forumparser.getThreadTimes();
			forumThreadReplyCounts = forumparser.getThreadReplyCounts();
			nForumThreads = forumparser.getThreadsCount();

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
	}

	public ArrayList<String> getThreadSubjects() {
		return forumThreadSubs;
	}

	public ArrayList<String> getThreadAuthors() {
		return forumThreadAuthors;
	}

	public ArrayList<String> getThreadIds() {
		return forumThreadIds;
	}

	public ArrayList<String> getThreadReplyCounts() {
		return forumThreadReplyCounts;
	}

	public ArrayList<String> getThreadTimes() {
		return forumThreadDates;
	}

	public int getThreadsCount() {
		return nForumThreads;
	}
}
