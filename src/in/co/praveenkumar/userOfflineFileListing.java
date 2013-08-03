package in.co.praveenkumar;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
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
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

public class userOfflineFileListing extends Activity {
	String serverAddress = MDroidActivity.serverAddress;
	String courseFolderName;
	private ArrayList<String> offlineFileName = new ArrayList<String>();
	private ArrayList<String> offlineFileDateModified = new ArrayList<String>();
	private ArrayList<String> offlineFileSize = new ArrayList<String>();
	AppPreferences appPrefs;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.userofflinefilelisting);
		// For getting Preferences
		appPrefs = new AppPreferences(getApplicationContext());
		Bundle extras = getIntent().getExtras();
		if (extras == null) {
			return;
		}
		courseFolderName = extras.getString("courseFolderName");

		File root = new File(android.os.Environment
				.getExternalStorageDirectory().getPath()
				+ "/MDroid/"
				+ courseFolderName + "/");
		ListFiles(root);

	}

	public void ListFiles(File f) {
		File[] files = f.listFiles();
		offlineFileName.clear();
		offlineFileDateModified.clear();
		offlineFileSize.clear();
		for (File file : files) {
			if (!(file.isDirectory())) {
				int startIndex;
				int endIndex;
				String fileName = file.toString();
				startIndex = fileName.lastIndexOf("/") + 1;
				endIndex = fileName.length();
				fileName = fileName.substring(startIndex, endIndex);
				File fileLocationPath = new File(f + "/" + fileName);
				if (fileName != ".android_secure" && fileName != "LOST.DIR") {
					// Getting the size of file
					long fileSize = fileLocationPath.length();
					String fileSizeInfo;
					if (fileSize > (1024 * 1024))
						fileSizeInfo = fileSize / (1024 * 1024) + "MB";
					else if (fileSize > 1024)
						fileSizeInfo = fileSize / 1024 + "KB";
					else
						fileSizeInfo = fileSize + "Bytes";

					// Last modified date of the folder...
					Date lastModDate = new Date(fileLocationPath.lastModified());
					SimpleDateFormat format = new SimpleDateFormat(
							"MM/dd/yyyy hh:mm a");
					String lastModDateformatted = format.format(lastModDate);

					offlineFileName.add(fileName);
					offlineFileDateModified.add(lastModDateformatted);
					offlineFileSize.add(fileSizeInfo);

				}
			}
			listFilesInListView(offlineFileName, offlineFileDateModified,
					offlineFileSize);
		}
	}

	public void listFilesInListView(ArrayList<String> offlineFileName,
			ArrayList<String> offlineFileDateModified,
			ArrayList<String> offlineFileSize) {

		ListView listView = (ListView) findViewById(R.id.myCourseFilesOffline);

		MySimpleArrayAdapter adapter = new MySimpleArrayAdapter(this,
				offlineFileName, offlineFileDateModified, offlineFileSize);
		// Assign adapter to ListView
		listView.setAdapter(adapter);
	}

	public class MySimpleArrayAdapter extends ArrayAdapter<String> {
		private final Context context;
		private final ArrayList<String> offlineFileName;
		private final ArrayList<String> offlineFileDateModified;
		private final ArrayList<String> offlineFileSize;

		public MySimpleArrayAdapter(Context context,
				ArrayList<String> offlineFileName,
				ArrayList<String> offlineFileDateModified,
				ArrayList<String> offlineFileSize) {
			super(context, R.layout.userofflinefilelistinglistview,
					offlineFileName);
			this.context = context;
			this.offlineFileName = offlineFileName;
			this.offlineFileDateModified = offlineFileDateModified;
			this.offlineFileSize = offlineFileSize;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View rowView = inflater.inflate(
					R.layout.userofflinefilelistinglistview, parent, false);

			if (position % 2 == 0)
				rowView.setBackgroundResource(R.drawable.listview_evenitem_color);

			final TextView textViewFileName = (TextView) rowView
					.findViewById(R.id.myOfflineFileName);
			textViewFileName.setText(offlineFileName.get(position));

			final TextView textViewFileDetails = (TextView) rowView
					.findViewById(R.id.myOfflineFileDetails);
			textViewFileDetails.setText(offlineFileSize.get(position) + ", "
					+ offlineFileDateModified.get(position));

			final Button fileOpenButton = (Button) rowView
					.findViewById(R.id.myOfflineFileOpenButton);

			final Button fileDeleteButton = (Button) rowView
					.findViewById(R.id.myOfflineFileDeleteButton);

			fileOpenButton.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					String fileUrl = android.os.Environment
							.getExternalStorageDirectory().getPath()
							+ "/MDroid/"
							+ courseFolderName
							+ "/"
							+ textViewFileName.getText().toString();
					int startIndex = fileUrl.lastIndexOf(".") + 1;
					int endIndex = fileUrl.length();
					String fileExtension = fileUrl.substring(startIndex,
							endIndex);
					File fileToBeOpened = new File(fileUrl);
					Intent i = new Intent();
					i.setAction(android.content.Intent.ACTION_VIEW);
					i.setDataAndType(Uri.fromFile(fileToBeOpened),
							"application/" + fileExtension);
					try {
						startActivity(i);
					} catch (ActivityNotFoundException e) {
						Toast.makeText(
								getBaseContext(),
								"No application found to open file type\n"
										+ fileExtension, Toast.LENGTH_LONG)
								.show();
					}

				}
			});

			rowView.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					String fileUrl = android.os.Environment
							.getExternalStorageDirectory().getPath()
							+ "/MDroid/"
							+ courseFolderName
							+ "/"
							+ textViewFileName.getText().toString();
					int startIndex = fileUrl.lastIndexOf(".") + 1;
					int endIndex = fileUrl.length();
					String fileExtension = fileUrl.substring(startIndex,
							endIndex);
					File fileToBeOpened = new File(fileUrl);
					Intent i = new Intent();
					i.setAction(android.content.Intent.ACTION_VIEW);
					i.setDataAndType(Uri.fromFile(fileToBeOpened),
							"application/" + fileExtension);
					try {
						startActivity(i);
					} catch (ActivityNotFoundException e) {
						Toast.makeText(
								getBaseContext(),
								"No application found to open file type\n"
										+ fileExtension, Toast.LENGTH_LONG)
								.show();
					}

				}
			});

			fileDeleteButton.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					String fileUrl = android.os.Environment
							.getExternalStorageDirectory().getPath()
							+ "/MDroid/"
							+ courseFolderName
							+ "/"
							+ textViewFileName.getText().toString();
					File fileToBeDeleted = new File(fileUrl);
					boolean deleted = fileToBeDeleted.delete();
					if (deleted)
						Toast.makeText(
								getBaseContext(),
								"File " + textViewFileName.getText().toString()
										+ " deleted!", Toast.LENGTH_SHORT)
								.show();
					offlineFileName.remove(position);
					offlineFileDateModified.remove(position);
					offlineFileSize.remove(position);
					listFilesInListView(offlineFileName,
							offlineFileDateModified, offlineFileSize);

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
