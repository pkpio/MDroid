/*
 * Author: 	Praveen Kumar Pendyala
 * Project: MDroid
 * Created:	14-01-2014
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


package in.co.praveenkumar.mdroid.networking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.util.Log;
import in.co.praveenkumar.mdroid.MainActivity;
import in.co.praveenkumar.mdroid.parser.ForumThreadReplyParser;

public class ForumThreadReplier {
	private final String DEBUG_TAG = "NETWORKING_FETCH_RESOURCE_FILES";
	private String mURL = MainActivity.mURL;
	private String threadReplyId = "";

	public ForumThreadReplier(String threadReplyId) {
		this.threadReplyId = threadReplyId;
	}

	public void postReply() {
		String html = fetchReplyPage();
		ForumThreadReplyParser FTRP = new ForumThreadReplyParser(html);
	}

	private String fetchReplyPage() {
		String html = "";
		try {
			// Get default app client. Only that has the cookie data
			DefaultHttpClient httpclient = MainActivity.httpclient;

			// Fetch thread page
			HttpGet httpgetCourse = new HttpGet(mURL
					+ "/mod/forum/post.php?reply=" + threadReplyId);
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

}
