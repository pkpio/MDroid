package in.co.praveenkumar;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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

public class userOfflineFolderListing extends Activity {
	String serverAddress = MDroidActivity.serverAddress;
	private ArrayList<String> courseFolder = new ArrayList<String>();
	private ArrayList<String> folderDateModified = new ArrayList<String>();
	private ArrayList<String> folderFilesNumber = new ArrayList<String>();
	AppPreferences appPrefs;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.userofflinefolderlisting);
		// For getting Preferences
		appPrefs = new AppPreferences(getApplicationContext());

		File root = new File(android.os.Environment
				.getExternalStorageDirectory().getPath() + "/MDroid/");
		ListCourses(root);
	}

	public void ListCourses(File f) {
		File[] files = f.listFiles();
		courseFolder.clear();
		folderDateModified.clear();
		folderFilesNumber.clear();
		for (File file : files) {
			if (file.isDirectory()) {
				int startIndex;
				int endIndex;
				String fileName = file.toString();
				startIndex = fileName.lastIndexOf("/") + 1;
				endIndex = fileName.length();
				fileName = fileName.substring(startIndex, endIndex);
				File coursefolderPath = new File(f + "/" + fileName);
				if (fileName != ".android_secure" && fileName != "LOST.DIR") {
					// Getting no of files in that folder...
					File filesInCourseFolder = new File(coursefolderPath + "/");
					int filesCountInCourseFolder = filesInCourseFolder
							.listFiles().length;

					// Last modified date of the folder...
					Date lastModDate = new Date(coursefolderPath.lastModified());
					SimpleDateFormat format = new SimpleDateFormat(
							"MM/dd/yyyy hh:mm a");
					String lastModDateformatted = format.format(lastModDate);

					courseFolder.add(fileName);
					folderDateModified.add(lastModDateformatted);
					folderFilesNumber.add(filesCountInCourseFolder
							+ " offline files");

				}
			}
			listFilesInListView(courseFolder, folderDateModified,
					folderFilesNumber);
		}
	}

	public void listFilesInListView(ArrayList<String> courseFolderName,
			ArrayList<String> courseFolderDateModified,
			ArrayList<String> courseFolderFilesCount) {

		ListView listView = (ListView) findViewById(R.id.myCourseFoldersOffline);

		MySimpleArrayAdapter adapter = new MySimpleArrayAdapter(this,
				courseFolderName, courseFolderDateModified,
				courseFolderFilesCount);
		// Assign adapter to ListView
		listView.setAdapter(adapter);
	}

	public class MySimpleArrayAdapter extends ArrayAdapter<String> {
		private final Context context;
		private final ArrayList<String> courseFolderName;
		private final ArrayList<String> courseFolderDateModified;
		private final ArrayList<String> courseFolderFilesCount;

		public MySimpleArrayAdapter(Context context,
				ArrayList<String> courseFolderName,
				ArrayList<String> courseFolderDateModified,
				ArrayList<String> courseFolderFilesCount) {
			super(context, R.layout.userofflinefolderlistinglistview,
					courseFolderName);
			this.context = context;
			this.courseFolderName = courseFolderName;
			this.courseFolderDateModified = courseFolderDateModified;
			this.courseFolderFilesCount = courseFolderFilesCount;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View rowView = inflater.inflate(
					R.layout.userofflinefolderlistinglistview, parent, false);

			if (position % 2 == 0)
				rowView.setBackgroundResource(R.drawable.listview_evenitem_color);

			final TextView textViewFolderName = (TextView) rowView
					.findViewById(R.id.myCourseNameOffline);
			textViewFolderName.setText(courseFolderName.get(position));

			final TextView textViewDateModified = (TextView) rowView
					.findViewById(R.id.courseModifiedDateOffline);
			textViewDateModified
					.setText(courseFolderDateModified.get(position));

			final TextView textViewNoOfFilesOffline = (TextView) rowView
					.findViewById(R.id.noOfFilesOffline);
			textViewNoOfFilesOffline.setText(courseFolderFilesCount
					.get(position));

			rowView.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					final int REQUEST_CODE = 19;
					Intent i = new Intent(context, userOfflineFileListing.class);
					i.putExtra("courseFolderName", textViewFolderName.getText()
							.toString());
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
