package in.co.praveenkumar.mdroid.activities;

import in.co.praveeenkumar.mdroid.extenders.HomeDrawerActivity;
import in.co.praveeenkumar.mdroid.extenders.StickyListViewAdapter;
import in.co.praveenkumar.mdroid.apis.R;
import in.co.praveenkumar.mdroid.helpers.Database;
import in.co.praveenkumar.mdroid.models.MoodleCourse;
import in.co.praveenkumar.mdroid.moodlerest.MoodleRestCourses;
import in.co.praveenkumar.mdroid.moodlerest.MoodleToken;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import se.emilsjolander.stickylistheaders.StickyListHeadersListView;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

public class MoodleHomeActivity extends HomeDrawerActivity {
	private final String DEBUG_TAG = "MoodleHomeActivity";
	private ArrayList<MoodleCourse> mCourses;
	private courseListViewAdapter myListAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_moodle_home);
		super.onCreate(savedInstanceState);

		// Testing
		Database db = new Database(this);
		db.setToken("9bd96dc343e76a041729aa3e602de8c4");
		db.setmUrl("http://moodle.praveenkumar.co.in/");
		new asyncCourseFetch().execute("");

	}

	private void listCoursesInListView() {
		StickyListHeadersListView listview;
		listview = (StickyListHeadersListView) findViewById(R.id.list);
		listview.setAreHeadersSticky(true);
		
		// Sort course objects
		Collections.sort(mCourses, new CustomComparator());

		// Build a string array of course names
		ArrayList<String> mCourseNames = new ArrayList<String>();
		for (int i = 0; i < mCourses.size(); i++)
			mCourseNames.add(mCourses.get(i).getFullname());

		// StickyHeader List view
		myListAdapter = new courseListViewAdapter(this, mCourseNames);
		listview.setAdapter(myListAdapter);
	}

	private class asyncCourseFetch extends AsyncTask<String, Integer, Long> {

		protected Long doInBackground(String... credentials) {
			MoodleToken mt = new MoodleToken("praveendath92", "praveen92",
					"http://moodle.praveenkumar.co.in");
			Log.d(DEBUG_TAG, mt.getToken());
			for (int i = 0; i < mt.getErrors().size(); i++)
				Log.d(DEBUG_TAG, mt.getErrors().get(i));

			MoodleRestCourses mrc = new MoodleRestCourses(
					getApplicationContext());
			mCourses = mrc.getCourses();

			return null;

		}

		protected void onPostExecute(Long result) {
			if (mCourses != null)
				listCoursesInListView();
		}
	}

	private class courseListViewAdapter extends StickyListViewAdapter {

		public courseListViewAdapter(Context context, ArrayList<String> dataSet) {
			super(context, dataSet);
			// TODO Auto-generated constructor stub
		}

	}

	public class CustomComparator implements Comparator<MoodleCourse> {

		@Override
		public int compare(MoodleCourse arg0, MoodleCourse arg1) {
			return arg0.getFullname().compareTo(arg1.getFullname());
		}
	}

}
