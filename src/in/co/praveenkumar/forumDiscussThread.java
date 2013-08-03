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

public class forumDiscussThread extends Activity {
	String serverAddress = MDroidActivity.serverAddress;
	String discussID;
	String discussSubject;
	ArrayList<String> discussThreadReplySubject = new ArrayList<String>();
	ArrayList<String> discussThreadReplyTime = new ArrayList<String>();
	ArrayList<String> discussThreadReplyPerson = new ArrayList<String>();
	ArrayList<String> discussThreadReplyContent = new ArrayList<String>();
	AppPreferences appPrefs;

	@Override
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.forumdiscussthread);
		// For getting Preferences
		appPrefs = new AppPreferences(getApplicationContext());

		Bundle extras = getIntent().getExtras();
		if (extras == null) {
			return;
		}
		discussID = extras.getString("discussID");
		discussSubject = extras.getString("discussSubject");

		// Setting title of discussThread activity..
		setTitle(discussSubject);

		new getForumsPageContent().execute(discussID);
	}

	/* AsycTask Thread */

	private class getForumsPageContent extends AsyncTask<String, Integer, Long> {
		protected Long doInBackground(String... discussID) {

			try {
				getPageContentForumsOne(discussID[0]);
			} catch (ClientProtocolException e) {

			} catch (IOException e) {

			}

			return null;
		}

		protected void onProgressUpdate(Integer... progress) {
		}

		protected void onPostExecute(Long result) {

			listFilesInListView(discussThreadReplySubject,
					discussThreadReplyPerson, discussThreadReplyTime,
					discussThreadReplyContent);
			hideLoadingMessageLayout();

		}

	}

	public void getPageContentForumsOne(String courseID)
			throws ClientProtocolException, IOException {

		DefaultHttpClient httpclient = MDroidActivity.httpclient;

		HttpGet httpgetCourse = new HttpGet(discussID);

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

		while (true) {
			prevIndex = htmlDataString
					.indexOf(" class=\"subject\">", prevIndex);

			if (prevIndex == -1)
				break;

			// for reply subject
			prevIndex += 17;
			endIndex = htmlDataString.indexOf("</div>", prevIndex);
			discussThreadReplySubject.add(htmlDataString.substring(prevIndex,
					endIndex));

			// for reply person name
			prevIndex = endIndex;
			prevIndex = htmlDataString.indexOf("<div class=\"author\">",
					prevIndex) + 22;
			prevIndex = htmlDataString.indexOf("\">", prevIndex) + 2;
			endIndex = htmlDataString.indexOf("</a>", prevIndex);
			discussThreadReplyPerson.add(htmlDataString.substring(prevIndex,
					endIndex));

			// for reply time
			prevIndex = endIndex;
			prevIndex = htmlDataString.indexOf(", ", prevIndex) + 3;
			endIndex = htmlDataString.indexOf("</div>", prevIndex);
			discussThreadReplyTime.add(htmlDataString.substring(prevIndex,
					endIndex));

			// for reply content
			prevIndex = endIndex;
			prevIndex = htmlDataString.indexOf("<div class=\"posting",
					prevIndex) + 19;
			prevIndex = htmlDataString.indexOf("\">",prevIndex)+2;
			endIndex = htmlDataString.indexOf("</div><div class=\"", prevIndex);
			String tempdiscussThreadReplyContent = (htmlDataString.substring(
					prevIndex, endIndex));
			tempdiscussThreadReplyContent = android.text.Html.fromHtml(
					tempdiscussThreadReplyContent).toString();
			discussThreadReplyContent.add(tempdiscussThreadReplyContent);

		}

	}

	public void hideLoadingMessageLayout() {
		LinearLayout mainLayout = (LinearLayout) this
				.findViewById(R.id.linearLayoutLoadingDiscussThread);
		mainLayout.setVisibility(LinearLayout.GONE);
	}

	public void listFilesInListView(
			ArrayList<String> discussThreadReplySubject,
			ArrayList<String> discussThreadReplyPerson,
			ArrayList<String> discussThreadReplyTime,
			ArrayList<String> discussThreadReplyContent) {
		ListView forumsListView = (ListView) findViewById(R.id.forumsDiscussThread);

		MySimpleArrayAdapter adapter = new MySimpleArrayAdapter(this,
				discussThreadReplySubject, discussThreadReplyPerson,
				discussThreadReplyTime, discussThreadReplyContent);
		// Assign adapter to ListView
		forumsListView.setAdapter(adapter);
	}

	public class MySimpleArrayAdapter extends ArrayAdapter<String> {
		private final Context context;
		private final ArrayList<String> discussThreadReplySubjectListView;
		private final ArrayList<String> discussThreadReplyPersonListView;
		private final ArrayList<String> discussThreadReplyTimeListView;
		private final ArrayList<String> discussThreadReplyContentListView;

		public MySimpleArrayAdapter(Context context,
				ArrayList<String> discussThreadReplySubjectListView,
				ArrayList<String> discussThreadReplyPersonListView,
				ArrayList<String> discussThreadReplyTimeListView,
				ArrayList<String> discussThreadReplyContentListView) {
			super(context, R.layout.forumslistviewlayout,
					discussThreadReplySubjectListView);
			this.context = context;
			this.discussThreadReplySubjectListView = discussThreadReplySubjectListView;
			this.discussThreadReplyPersonListView = discussThreadReplyPersonListView;
			this.discussThreadReplyTimeListView = discussThreadReplyTimeListView;
			this.discussThreadReplyContentListView = discussThreadReplyContentListView;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View rowView = inflater.inflate(
					R.layout.forumdiscussthreadlistviewlayout, parent, false);

			final TextView subjectTextView = (TextView) rowView
					.findViewById(R.id.forumDiscussThreadSubject);
			subjectTextView.setText(discussThreadReplySubjectListView
					.get(position));

			final TextView authorTextView = (TextView) rowView
					.findViewById(R.id.forumDiscussThreadAuthor);
			authorTextView.setText(discussThreadReplyPersonListView
					.get(position));

			final TextView timeTextView = (TextView) rowView
					.findViewById(R.id.forumDiscussThreadReplyTime);
			timeTextView.setText(discussThreadReplyTimeListView.get(position));

			final TextView contentTextView = (TextView) rowView
					.findViewById(R.id.forumDiscussThreadContent);
			contentTextView.setText(discussThreadReplyContentListView
					.get(position));

			rowView.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {

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
