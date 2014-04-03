package in.co.praveenkumar.mdroid.activities;

import in.co.praveeenkumar.mdroid.extenders.DrawerActivity;
import in.co.praveeenkumar.mdroid.extenders.StickyListViewAdapter;
import in.co.praveenkumar.mdroid.apis.R;
import in.co.praveenkumar.mdroid.models.MoodleCourseContent;
import in.co.praveenkumar.mdroid.models.MoodleCourseModule;
import in.co.praveenkumar.mdroid.models.MoodleCourseSection;
import in.co.praveenkumar.mdroid.moodlerest.MoodleRestCourseContents;

import java.util.ArrayList;

import se.emilsjolander.stickylistheaders.StickyListHeadersListView;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

public class CourseContentActivity extends DrawerActivity {
	private final String DEBUG_TAG = "CourseContentActivity";
	private MoodleCourseContent mCourseContent;
	ArrayList<MoodleCourseSection> mSections = new ArrayList<MoodleCourseSection>();
	ArrayList<MoodleCourseModule> mModules = new ArrayList<MoodleCourseModule>();
	private contentListViewAdapter myListAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_course_content);
		super.onCreate(savedInstanceState);
		new AsyncContentFetch().execute("");
	}

	private void listContentInListView() {
		StickyListHeadersListView listview;
		listview = (StickyListHeadersListView) findViewById(R.id.course_content_list);
		listview.setAreHeadersSticky(true);

		// Build a string array of module section names
		ArrayList<String> mSectionNames = new ArrayList<String>();
		for (int i = 0; i < mModules.size(); i++)
			mSectionNames.add(mModules.get(i).getSectionname());

		// StickyHeader List view
		myListAdapter = new contentListViewAdapter(this, mSectionNames);
		listview.setAdapter(myListAdapter);

	}

	private class AsyncContentFetch extends AsyncTask<String, Integer, Long> {

		protected Long doInBackground(String... credentials) {
			MoodleRestCourseContents mrcc = new MoodleRestCourseContents(
					getApplicationContext());
			mCourseContent = mrcc.getCourseContent("2");

			Log.d(DEBUG_TAG, "Course json parsing complete.");

			// Getting modules in all sections
			for (int i = 0; i < mCourseContent.getSections().size(); i++) {
				mModules.addAll(mCourseContent.getSection(i).getModules());
			}
			return null;

		}

		protected void onPostExecute(Long result) {
			listContentInListView();
		}
	}

	private class contentListViewAdapter extends StickyListViewAdapter {

		public contentListViewAdapter(Context context, ArrayList<String> dataSet) {
			super(context, dataSet);
			// TODO Auto-generated constructor stub
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View rowView = mInflater.inflate(R.layout.course_content_list_item,
					parent, false);

			return rowView;
		}
	}

}
