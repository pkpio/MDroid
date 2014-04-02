package in.co.praveenkumar.mdroid.activities;

import in.co.praveeenkumar.mdroid.extenders.DrawerActivity;
import in.co.praveeenkumar.mdroid.extenders.StickyListViewAdapter;
import in.co.praveenkumar.mdroid.apis.R;
import in.co.praveenkumar.mdroid.models.MoodleCourseContent;
import in.co.praveenkumar.mdroid.models.MoodleCourseModule;
import in.co.praveenkumar.mdroid.moodlerest.MoodleRestCourseContents;

import java.util.ArrayList;

import se.emilsjolander.stickylistheaders.StickyListHeadersListView;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

public class CourseContentActivity extends DrawerActivity {
	private final String DEBUG_TAG = "CourseContentActivity";
	private MoodleCourseContent mCourseContent;
	private ArrayList<MoodleCourseModule> mModules = new ArrayList<MoodleCourseModule>();
	private ArrayList<String> sectionHeaders = new ArrayList<String>();

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

	}

	private class AsyncContentFetch extends AsyncTask<String, Integer, Long> {

		protected Long doInBackground(String... credentials) {
			MoodleRestCourseContents mrcc = new MoodleRestCourseContents(
					getApplicationContext());
			mCourseContent = mrcc.getCourseContent("2");
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
