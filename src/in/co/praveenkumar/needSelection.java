package in.co.praveenkumar;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Toast;

public class needSelection extends Activity {

	String serverAddress = MDroidActivity.serverAddress;
	String courseID;
	String courseName;
	AppPreferences appPrefs;

	@Override
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.needselection);
		// For getting Preferences
		appPrefs = new AppPreferences(getApplicationContext());

		Bundle extras = getIntent().getExtras();
		if (extras == null) {
			return;
		}
		courseID = extras.getString("courseID");
		courseName = extras.getString("courseName");

		setTitle(courseName);

		LinearLayout linearLayoutSelectionFiles = (LinearLayout) findViewById(R.id.files);
		linearLayoutSelectionFiles.setOnClickListener(fileSelectionListener);

		LinearLayout linearLayoutSelectionForum = (LinearLayout) findViewById(R.id.forums);
		linearLayoutSelectionForum.setOnClickListener(forumSelectionListener);
	}

	private OnClickListener fileSelectionListener = new OnClickListener() {
		public void onClick(View v) {
			final int REQUEST_CODE = 13;
			Intent i = new Intent(getBaseContext(), fileListing.class);
			i.putExtra("courseID", courseID);
			i.putExtra("courseName", courseName);
			startActivityForResult(i, REQUEST_CODE);

		}
	};

	private OnClickListener forumSelectionListener = new OnClickListener() {
		public void onClick(View v) {
			final int REQUEST_CODE = 14;
			Intent i = new Intent(getBaseContext(), forums.class);
			i.putExtra("courseID", courseID);
			i.putExtra("courseName", courseName);
			startActivityForResult(i, REQUEST_CODE);
		}
	};

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
