/**************************************************************
 * Author: 	Praveen Kumar Pendyala
 * Project: MDroid
 * Created: 29th May, 2014.
 * License: http://creativecommons.org/licenses/by-nc-sa/3.0/
 **************************************************************/

package in.co.praveenkumar.mdroid.activities;

import in.co.praveeenkumar.mdroid.extenders.HomeDrawerActivity;
import in.co.praveeenkumar.mdroid.extenders.StickyListViewAdapter;
import in.co.praveenkumar.mdroid.apis.R;
import in.co.praveenkumar.mdroid.helpers.Database;
import in.co.praveenkumar.mdroid.helpers.ImageDecoder;
import in.co.praveenkumar.mdroid.models.MoodleCourse;
import in.co.praveenkumar.mdroid.moodlerest.MoodleRestCourses;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import se.emilsjolander.stickylistheaders.StickyListHeadersListView;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class MoodleHomeActivity extends HomeDrawerActivity {
	private final String DEBUG_TAG = "MoodleHomeActivity";
	private ArrayList<MoodleCourse> mCourses;
	private courseListViewAdapter myListAdapter;
	private String[] menuItems = new String[] { "My Courses", "All Courses",
			"Favourites" };
	private Database db;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_moodle_home);
		super.onCreate(savedInstanceState);

		db = new Database(this);
		setUpNavMenu();
		new AsyncCourseFetch().execute("");
	}

	private void listCoursesInListView() {
		StickyListHeadersListView listview;
		listview = (StickyListHeadersListView) findViewById(R.id.moodle_home_list);
		listview.setAreHeadersSticky(true);

		// Sort course objects
		Collections.sort(mCourses, new CustomComparator());

		// Build a string array of course names
		ArrayList<String> mCourseNames = new ArrayList<String>();
		for (int i = 0; i < mCourses.size(); i++)
			mCourseNames.add(mCourses.get(i).getShortname());

		// StickyHeader List view
		myListAdapter = new courseListViewAdapter(this, mCourseNames);
		listview.setAdapter(myListAdapter);
	}

	private class AsyncCourseFetch extends AsyncTask<String, Integer, Long> {

		protected Long doInBackground(String... credentials) {
			MoodleRestCourses mrc = new MoodleRestCourses(
					getApplicationContext());
			mCourses = mrc.getEnrolledCourses(db.getUserid());
			return null;
		}

		protected void onPostExecute(Long result) {
			if (mCourses != null && mCourses.size() > 0)
				listCoursesInListView();
			else {
				// Do error reporting code here
			}
		}
	}

	private class courseListViewAdapter extends StickyListViewAdapter {

		public courseListViewAdapter(Context context, ArrayList<String> dataSet) {
			super(context, dataSet);
			// TODO Auto-generated constructor stub
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View rowView = mInflater.inflate(R.layout.list_item_course, parent,
					false);

			// Get course views
			final TextView cShortName = (TextView) rowView
					.findViewById(R.id.course_shortname);
			final TextView cLongName = (TextView) rowView
					.findViewById(R.id.course_longname);

			// Set values
			cShortName.setText(mCourses.get(position).getShortname());
			cLongName.setText(mCourses.get(position).getFullname());

			// set a listener on view
			rowView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					openContentIntent();
				}
			});

			return rowView;
		}
	}

	public void openContentIntent() {
		Intent i = new Intent(this, CourseContentActivity.class);
		startActivityForResult(i, 3);
	}

	public class CustomComparator implements Comparator<MoodleCourse> {

		@Override
		public int compare(MoodleCourse arg0, MoodleCourse arg1) {
			return arg0.getShortname().compareTo(arg1.getShortname());
		}
	}

	private void setUpNavMenu() {
		Log.d(DEBUG_TAG, "Navigation menu setup");

		ImageView userImageView = (ImageView) findViewById(R.id.user_image);
		TextView userNameView = (TextView) findViewById(R.id.user_fullname);

		Bitmap userImage = ImageDecoder.decodeImage(new File(Environment
				.getExternalStorageDirectory() + "/MDroid/user.jpg"));
		if (userImage != null)
			userImageView.setImageBitmap(userImage);
		else
			Log.d(DEBUG_TAG, "Received null for userImage");
		userNameView.setText(db.getUserFullname());

		setUpMenu(menuItems, 0);
	}

}
