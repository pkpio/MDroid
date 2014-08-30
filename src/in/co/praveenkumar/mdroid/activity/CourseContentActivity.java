package in.co.praveenkumar.mdroid.activity;

import in.co.praveenkumar.mdroid.adapter.NavigationDrawer;
import in.co.praveenkumar.mdroid.apis.R;
import in.co.praveenkumar.mdroid.helper.SessionSetting;
import in.co.praveenkumar.mdroid.moodlemodel.MoodleCourse;
import in.co.praveenkumar.mdroid.view.StickyListView.PinnedSectionListAdapter;

import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class CourseContentActivity extends NavigationDrawer {
	CourseListAdapter courseListAdapter;
	SessionSetting session;
	List<MoodleCourse> mCourses;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_course_contents);
		setUpDrawer();

		// Get all courses of this site
		session = new SessionSetting(this);
		mCourses = MoodleCourse.find(MoodleCourse.class, "siteid = ?",
				session.getCurrentSiteId() + "");

		ListView courseList = (ListView) findViewById(R.id.list_course_content);
		courseListAdapter = new CourseListAdapter(this);
		courseList.setAdapter(courseListAdapter);

	}

	public class CourseListAdapter extends ArrayAdapter<String> implements
			PinnedSectionListAdapter {
		private static final int TYPE_ACCOUNT = 0;
		private static final int TYPE_MENUITEM = 1;
		private static final int TYPE_COUNT = 2;
		private final Context context;

		public CourseListAdapter(Context context) {
			super(context, R.layout.list_item_account, new String[mCourses
					.size()]);
			this.context = context;
		}

		@Override
		public int getViewTypeCount() {
			return TYPE_COUNT;
		}

		@Override
		public int getItemViewType(int position) {
			return (position % 2 == 0) ? TYPE_MENUITEM : TYPE_ACCOUNT;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final ViewHolder viewHolder;

			if (convertView == null) {
				viewHolder = new ViewHolder();
				LayoutInflater inflater = (LayoutInflater) context
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

				convertView = inflater.inflate(R.layout.list_item_course,
						parent, false);

				viewHolder.shortname = (TextView) convertView
						.findViewById(R.id.list_course_shortname);
				viewHolder.fullname = (TextView) convertView
						.findViewById(R.id.list_course_fullname);

				// Save the holder with the view
				convertView.setTag(viewHolder);
			} else {
				// Just use the viewHolder and avoid findviewbyid()
				viewHolder = (ViewHolder) convertView.getTag();
			}

			// Assign values
			viewHolder.shortname.setText(mCourses.get(position).getShortname());
			viewHolder.fullname.setText(mCourses.get(position).getFullname());

			return convertView;
		}

		@Override
		public boolean isItemViewTypePinned(int viewType) {
			if (viewType == TYPE_ACCOUNT)
				return false;
			else
				return true;
		}
	}

	static class ViewHolder {
		TextView shortname;
		TextView fullname;
	}
}
