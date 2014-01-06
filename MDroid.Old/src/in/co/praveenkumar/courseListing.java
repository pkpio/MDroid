package in.co.praveenkumar;

import java.io.File;
import java.util.ArrayList;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

public class courseListing extends Activity {
	String serverAddress = MDroidActivity.serverAddress;
	AppPreferences appPrefs;

	@Override
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.courselisting);
		// For getting Preferences
		appPrefs = new AppPreferences(getApplicationContext());

		int loginCount = appPrefs.getIntPrefs("logincount");
		int rated = appPrefs.getIntPrefs("rated");

		// Saving prefs
		appPrefs.saveIntPrefs("logincount", loginCount + 1);

		Bundle extras = getIntent().getExtras();
		if (extras == null) {
			return;
		}
		String htmlData = extras.getString("htmlData");
		int prevIndex = 0;
		int endIndex = 0;
		ArrayList<String> courseIDs = new ArrayList<String>();
		ArrayList<String> courseNames = new ArrayList<String>();
		String userName = "";

		prevIndex = htmlData.indexOf("You are logged in as ", prevIndex);
		prevIndex = htmlData.indexOf("\">", prevIndex) + 2;
		endIndex = htmlData.indexOf("</a>", prevIndex);
		userName = (htmlData.substring(prevIndex, endIndex));

		// Setting title of the Course Activity...
		setTitle(userName + "'s courses");

		while (true) {
			prevIndex = htmlData.indexOf(
					"<a title=\"Click to enter this course\" href=\"",
					prevIndex);
			if (prevIndex == -1)
				break;
			prevIndex += 44;
			prevIndex = htmlData.indexOf("/course/view.php?id=", prevIndex) + 20;
			endIndex = htmlData.indexOf('\"', prevIndex);

			courseIDs.add(htmlData.substring(prevIndex, endIndex));

			prevIndex = endIndex + 2;
			endIndex = htmlData.indexOf("</a>", prevIndex);

			courseNames.add(htmlData.substring(prevIndex, endIndex));
		}

		for (int i = 0; i < courseNames.size(); i++) {
			String tempCourseName = "";
			tempCourseName = courseNames.get(i).replaceAll(" : ", "-");
			tempCourseName = android.text.Html.fromHtml(tempCourseName)
					.toString();

			courseNames.set(i, tempCourseName);
			File file = new File(Environment.getExternalStorageDirectory(),
					"/MDroid/" + tempCourseName + "/");
			if (!file.exists()) {
				if (!file.mkdirs()) {
					Log.e("TravellerLog :: ", "Problem creating course folder");
					Toast.makeText(getBaseContext(),
							"failed to create folder " + file,
							Toast.LENGTH_SHORT).show();
				}
			}
		}

		// Listing Course in a ListView..
		listCourseInListView(courseNames, courseIDs);

		if (loginCount % 2 == 0 && loginCount != 0 && rated == 0) {
			showDialog(4);
		}
	}

	public Context context = this;

	public void listCourseInListView(ArrayList<String> courseNames,
			ArrayList<String> courseIDs) {

		ListView listView = (ListView) findViewById(R.id.myCourses);
		MySimpleArrayAdapter adapter = new MySimpleArrayAdapter(this,
				courseNames, courseIDs);

		// Assign adapter to ListView
		listView.setAdapter(adapter);
	}

	public class MySimpleArrayAdapter extends ArrayAdapter<String> {
		private final Context context;
		private final ArrayList<String> courseName;
		private final ArrayList<String> courseID;

		public MySimpleArrayAdapter(Context context,
				ArrayList<String> courseName, ArrayList<String> courseID) {
			super(context, R.layout.courselistviewlayout, courseName);
			this.context = context;
			this.courseName = courseName;
			this.courseID = courseID;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View rowView = inflater.inflate(R.layout.courselistviewlayout,
					parent, false);
			final TextView textView = (TextView) rowView
					.findViewById(R.id.myCoursesName);
			textView.setText(courseName.get(position));
			textView.setHint(courseID.get(position));
			if (position % 2 == 0)
				textView.setBackgroundResource(R.drawable.listview_evenitem_color);

			rowView.setClickable(true);
			rowView.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					final int REQUEST_CODE = 11;
					Intent i = new Intent(context, needSelection.class);
					i.putExtra("courseID", textView.getHint());
					i.putExtra("courseName", textView.getText());
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
					Toast.makeText(
							getBaseContext(),
							"Can't change! Go to login screen to change server",
							Toast.LENGTH_SHORT).show();
				}
			});

			resetServerValueButton.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
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
