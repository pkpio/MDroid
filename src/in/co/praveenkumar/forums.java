package in.co.praveenkumar;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

public class forums extends Activity {

	String serverAddress = MDroidActivity.serverAddress;
	String courseName = "";
	String courseID = "";
	String discussionCount = "0";
	ArrayList<String> DiscussionIDs = new ArrayList<String>();
	ArrayList<String> DiscussionSubject = new ArrayList<String>();
	ArrayList<String> DiscussionAuthor = new ArrayList<String>();
	ArrayList<String> DiscussionRepliesCount = new ArrayList<String>();
	ArrayList<String> DiscussionLastPostTime = new ArrayList<String>();
	AppPreferences appPrefs;

	@Override
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.forums);
		// For getting Preferences
		appPrefs = new AppPreferences(getApplicationContext());

		Bundle extras = getIntent().getExtras();
		if (extras == null) {
			return;
		}
		courseID = extras.getString("courseID");
		courseName = extras.getString("courseName");

		// Setting title of FileListing activity..
		setTitle("Forums (" + discussionCount + "): " + courseName);

		new getForumsPageContent().execute(courseID);
	}

	/* AsycTask Thread */

	private class getForumsPageContent extends AsyncTask<String, Integer, Long> {
		protected Long doInBackground(String... courseID) {

			try {
				getPageContentForumsOne(courseID[0]);
			} catch (ClientProtocolException e) {

			} catch (IOException e) {

			}

			return null;
		}

		protected void onProgressUpdate(Integer... progress) {
		}

		protected void onPostExecute(Long result) {
			// Changing title of FileListing activity..
			setTitle("Forums (" + discussionCount + "): " + courseName);

			listFilesInListView(DiscussionIDs, DiscussionSubject,
					DiscussionAuthor, DiscussionRepliesCount,
					DiscussionLastPostTime);
			hideLoadingMessageLayout();

		}

	}

	public void getPageContentForumsOne(String courseID)
			throws ClientProtocolException, IOException {

		DefaultHttpClient httpclient = MDroidActivity.httpclient;

		HttpGet httpgetCourse = new HttpGet(serverAddress
				+ "/mod/forum/index.php?id=" + courseID);

		HttpResponse responseCourse = httpclient.execute(httpgetCourse);
		HttpEntity entityCourse = responseCourse.getEntity();

		try {
			inputStreamToStringForumsOne(responseCourse.getEntity()
					.getContent());
		} catch (IOException e) {

		}

		if (entityCourse != null) {
			entityCourse.consumeContent();
		}
	}

	private void inputStreamToStringForumsOne(InputStream is)
			throws IOException {
		String line = "";
		StringBuilder total = new StringBuilder();

		// Wrap a BufferedReader around the InputStream
		BufferedReader rd = new BufferedReader(new InputStreamReader(is));

		// Read response until the end
		while ((line = rd.readLine()) != null) {
			total.append(line);
		}

		extractFileDetailsForForumsOne(total.toString());
	}

	public void extractFileDetailsForForumsOne(String htmlDataString) {

		int prevIndex = 0;
		int endIndex = 0;
		String forumViewID = "";

		while (true) {
			prevIndex = htmlDataString.indexOf("<a href=\"view.php?f=",
					prevIndex);

			if (prevIndex == -1)
				break;

			prevIndex += 20;
			endIndex = htmlDataString.indexOf("\"", prevIndex);
			forumViewID = (htmlDataString.substring(prevIndex, endIndex));
		}

		try {
			getPageContentForumsTwo(forumViewID);
		} catch (ClientProtocolException e) {

		} catch (IOException e) {

		}

	}

	public void getPageContentForumsTwo(String forumViewID)
			throws ClientProtocolException, IOException {

		DefaultHttpClient httpclient = MDroidActivity.httpclient;

		HttpGet httpgetCourse = new HttpGet(serverAddress
				+ "/mod/forum/view.php?f=" + forumViewID);

		HttpResponse responseCourse = httpclient.execute(httpgetCourse);
		HttpEntity entityCourse = responseCourse.getEntity();

		try {
			inputStreamToStringForumsTwo(responseCourse.getEntity()
					.getContent());
		} catch (IOException e) {

		}

		if (entityCourse != null) {
			entityCourse.consumeContent();
		}
	}

	private void inputStreamToStringForumsTwo(InputStream is)
			throws IOException {
		String line = "";
		StringBuilder total = new StringBuilder();

		// Wrap a BufferedReader around the InputStream
		BufferedReader rd = new BufferedReader(new InputStreamReader(is));

		// Read response until the end
		while ((line = rd.readLine()) != null) {
			total.append(line);
		}

		extractFileDetailsForForumsTwo(total.toString());
	}

	// Call to ListFilesInListView for Forum files made here..

	public void extractFileDetailsForForumsTwo(String htmlDataString) {

		int prevIndex = 0;
		int endIndex = 0;

		while (true) {
			prevIndex = htmlDataString.indexOf(
					"class=\"topic starter\"><a href=\"", prevIndex);

			if (prevIndex == -1)
				break;

			// for Post ID
			prevIndex += 31;
			endIndex = htmlDataString.indexOf("\"", prevIndex);
			DiscussionIDs.add(htmlDataString.substring(prevIndex, endIndex));

			// for post subject
			prevIndex = endIndex + 2;
			endIndex = htmlDataString.indexOf("</a>", prevIndex);
			String textConvertedhtmlDataString = htmlDataString.substring(
					prevIndex, endIndex);
			textConvertedhtmlDataString = android.text.Html.fromHtml(
					textConvertedhtmlDataString).toString();
			DiscussionSubject.add(textConvertedhtmlDataString);

			// for post Author
			prevIndex = endIndex;
			prevIndex = htmlDataString.indexOf(
					"<td class=\"author\"><a href=\"", prevIndex) + 28;
			prevIndex = htmlDataString.indexOf("\">", prevIndex) + 2;
			endIndex = htmlDataString.indexOf("</a>", prevIndex);
			DiscussionAuthor.add(htmlDataString.substring(prevIndex, endIndex));

			// for post replies count
			prevIndex = endIndex;
			prevIndex = htmlDataString.indexOf(
					"<td class=\"replies\"><a href=\"", prevIndex) + 29;
			prevIndex = htmlDataString.indexOf("\">", prevIndex) + 2;
			endIndex = htmlDataString.indexOf("</a>", prevIndex);
			DiscussionRepliesCount.add(htmlDataString.substring(prevIndex,
					endIndex));

			// for post last reply time
			prevIndex = endIndex;
			prevIndex = htmlDataString.indexOf(
					"<td class=\"lastpost\"><a href=\"", prevIndex) + 30;
			prevIndex = htmlDataString.indexOf("\">", prevIndex) + 2;
			prevIndex = htmlDataString.indexOf("\">", prevIndex) + 2;
			prevIndex = htmlDataString.indexOf(",", prevIndex) + 2;
			endIndex = htmlDataString.indexOf("</a>", prevIndex);
			DiscussionLastPostTime.add(htmlDataString.substring(prevIndex,
					endIndex));

		}

		discussionCount = DiscussionIDs.size() + "";

	}

	public void hideLoadingMessageLayout() {
		LinearLayout mainLayout = (LinearLayout) this
				.findViewById(R.id.linearLayoutLoadingForumsList);
		mainLayout.setVisibility(LinearLayout.GONE);
	}

	public void listFilesInListView(ArrayList<String> DiscussionIDs,
			ArrayList<String> DiscussionSubject,
			ArrayList<String> DiscussionAuthor,
			ArrayList<String> DiscussionRepliesCount,
			ArrayList<String> DiscussionLastPostTime) {
		ListView forumsListView = (ListView) findViewById(R.id.myForums);

		MySimpleArrayAdapter adapter = new MySimpleArrayAdapter(this,
				DiscussionIDs, DiscussionSubject, DiscussionAuthor,
				DiscussionRepliesCount, DiscussionLastPostTime);
		// Assign adapter to ListView
		forumsListView.setAdapter(adapter);
	}

	public class MySimpleArrayAdapter extends ArrayAdapter<String> {
		private final Context context;
		private final ArrayList<String> DiscussionIDListView;
		private final ArrayList<String> DiscussionSubjectListView;
		private final ArrayList<String> DiscussionAuthorListView;
		private final ArrayList<String> DiscussionRepliesCountListView;
		private final ArrayList<String> DiscussionLastPostTimeListView;

		public MySimpleArrayAdapter(Context context,
				ArrayList<String> DiscussionIDListView,
				ArrayList<String> DiscussionSubjectListView,
				ArrayList<String> DiscussionAuthorListView,
				ArrayList<String> DiscussionRepliesCountListView,
				ArrayList<String> DiscussionLastPostTimeListView) {
			super(context, R.layout.forumslistviewlayout,
					DiscussionSubjectListView);
			this.context = context;
			this.DiscussionIDListView = DiscussionIDListView;
			this.DiscussionSubjectListView = DiscussionSubjectListView;
			this.DiscussionAuthorListView = DiscussionAuthorListView;
			this.DiscussionRepliesCountListView = DiscussionRepliesCountListView;
			this.DiscussionLastPostTimeListView = DiscussionLastPostTimeListView;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View rowView = inflater.inflate(R.layout.forumslistviewlayout,
					parent, false);

			final TextView subjectTextView = (TextView) rowView
					.findViewById(R.id.forumThreadSubject);
			subjectTextView.setText(DiscussionSubjectListView.get(position));
			subjectTextView.setHint(DiscussionIDListView.get(position));

			final TextView repliesCountTextView = (TextView) rowView
					.findViewById(R.id.forumThreadRepliesCount);
			repliesCountTextView.setText("("
					+ DiscussionRepliesCountListView.get(position) + ")");

			final TextView authorTextView = (TextView) rowView
					.findViewById(R.id.forumThreadAuthor);
			authorTextView.setText(DiscussionAuthorListView.get(position));

			final TextView timeTextView = (TextView) rowView
					.findViewById(R.id.forumThreadStartTime);
			timeTextView.setText(DiscussionLastPostTimeListView.get(position));

			if (position % 2 == 0) {
				rowView.setBackgroundResource(R.drawable.listview_evenitem_color);				
			}
			
			rowView.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					final int REQUEST_CODE = 16;
					Intent i = new Intent(context, forumDiscussThread.class);
					i.putExtra("discussID", subjectTextView.getHint()
							+ "&mode=1");
					i.putExtra("discussSubject", subjectTextView.getText());
					startActivityForResult(i, REQUEST_CODE);
				}
			});

			return rowView;
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
		case R.id.ChangeLog:
			showDialog(5);

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

		case 5:
			dialog.setContentView(R.layout.change_log);
			dialog.setTitle("Change log");
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
			
			Button submitRatingLaterButton = (Button) dialog
					.findViewById(R.id.submitRatingLater);

			submitRatingLaterButton.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
						dialog.dismiss();
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
					// String newServervalue =
					// changeServerEdittextValue.getText()
					// .toString();
					// // Saving in preferences..
					// SharedPreferences prefs =
					// getPreferences(Context.MODE_PRIVATE);
					// SharedPreferences.Editor editor = prefs.edit();
					// editor.putString("serverAddress", newServervalue);
					// editor.commit();
					// serverAddress = newServervalue;
					// dialog.dismiss();
					//
					// Toast.makeText(getBaseContext(),
					// "Server prefernce saved to \n" + serverAddress,
					// Toast.LENGTH_SHORT).show();
					Toast.makeText(
							getBaseContext(),
							"Can't change! Go to login screen to change server",
							Toast.LENGTH_SHORT).show();
				}
			});

			resetServerValueButton.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					// changeServerEdittextValue
					// .setText("http://moodle.iitb.ac.in");
					// // Saving in preferences..
					// SharedPreferences prefs =
					// getPreferences(Context.MODE_PRIVATE);
					// SharedPreferences.Editor editor = prefs.edit();
					// editor.putString("serverAddress",
					// "http://moodle.iitb.ac.in");
					// editor.commit();
					// serverAddress = "http://moodle.iitb.ac.in";
					// dialog.dismiss();

					Toast.makeText(
							getBaseContext(),
							"Can't change! Go to login screen to change server",
							Toast.LENGTH_SHORT).show();
				}
			});

			break;
		}
		return dialog;
	}

}
