package in.co.praveenkumar.mdroid.fragment;

import in.co.praveenkumar.mdroid.activity.CourseContentActivity;
import in.co.praveenkumar.mdroid.apis.R;
import in.co.praveenkumar.mdroid.helper.SessionSetting;
import in.co.praveenkumar.mdroid.moodlemodel.MoodleCourse;
import in.co.praveenkumar.mdroid.task.CourseSyncTask;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class CourseFragment extends Fragment {
	/**
	 * List all courses in Moodle site
	 */
	public static final int TYPE_ALL_COURSES = 0;
	/**
	 * List only user courses
	 */
	public static final int TYPE_USER_COURSES = 1;
	/**
	 * List only courses favourited by user
	 */
	public static final int TYPE_FAV_COURSES = 2;

	CourseListAdapter courseListAdapter;
	SessionSetting session;
	List<MoodleCourse> mCourses;
	int Type = 0;
	LinearLayout courseEmptyLayout;

	/**
	 * All courses will be listed. You can choose something else by using
	 * Course(type) method.
	 */
	public CourseFragment() {
	}

	/**
	 * Course.TYPE_ALL_COURSES, TYPE_USER_COURSES, TYPE_FAV_COURSES are
	 * available options
	 * 
	 * @param Type
	 *            Choose what all courses have to listed.
	 */
	public CourseFragment(int Type) {
		this.Type = Type;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.frag_courses, container,
				false);

		// Get all courses of this site
		session = new SessionSetting(getActivity());

		if (Type == TYPE_USER_COURSES)
			mCourses = MoodleCourse.find(MoodleCourse.class,
					"siteid = ? and is_user_course = ?",
					session.getCurrentSiteId() + "", "1");
		else if (Type == TYPE_FAV_COURSES)
			mCourses = MoodleCourse.find(MoodleCourse.class,
					"siteid = ? and is_fav_course = ?",
					session.getCurrentSiteId() + "", "1");
		else
			mCourses = MoodleCourse.find(MoodleCourse.class, "siteid = ? and ",
					session.getCurrentSiteId() + "");

		courseEmptyLayout = (LinearLayout) rootView
				.findViewById(R.id.course_empty_layout);
		ListView courseList = (ListView) rootView
				.findViewById(R.id.content_course);
		courseListAdapter = new CourseListAdapter(getActivity());
		courseList.setAdapter(courseListAdapter);

		// We don't want to run sync in each course listing
		if (Type == TYPE_USER_COURSES)
			new courseSyncerBg().execute("");

		return rootView;
	}

	public class CourseListAdapter extends BaseAdapter {
		private final Context context;

		public CourseListAdapter(Context context) {
			this.context = context;
			if (mCourses.size() != 0)
				courseEmptyLayout.setVisibility(LinearLayout.GONE);
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
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
				viewHolder.favIcon = (ImageView) convertView
						.findViewById(R.id.list_course_fav);

				// Save the holder with the view
				convertView.setTag(viewHolder);
			} else {
				// Just use the viewHolder and avoid findviewbyid()
				viewHolder = (ViewHolder) convertView.getTag();
			}

			// Assign values
			final MoodleCourse mCourse = mCourses.get(position);
			viewHolder.shortname.setText(mCourse.getShortname());
			viewHolder.fullname.setText(mCourse.getFullname());
			if (mCourses.get(position).getIsFavCourse())
				viewHolder.favIcon.setImageResource(R.drawable.icon_favorite);
			else
				viewHolder.favIcon
						.setImageResource(R.drawable.icon_favorite_outline);

			convertView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					Intent i = new Intent(context, CourseContentActivity.class);
					i.putExtra("courseid", mCourses.get(position).getCourseid());
					i.putExtra("coursedbid", mCourses.get(position).getId());
					context.startActivity(i);
				}
			});

			viewHolder.favIcon.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					mCourse.setIsFavCourse(!mCourse.getIsFavCourse());
					mCourse.save();

					// Update listview
					mCourses.get(position).setIsFavCourse(
							mCourse.getIsFavCourse());
					courseListAdapter.notifyDataSetChanged();
				}
			});

			return convertView;
		}

		@Override
		public int getCount() {
			return mCourses.size();
		}

		@Override
		public Object getItem(int position) {
			return mCourses.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}
	}

	static class ViewHolder {
		TextView shortname;
		TextView fullname;
		ImageView favIcon;
	}

	/**
	 * Syncs the courses of the current user in background and updates list *
	 * 
	 * @author Praveen Kumar Pendyala (praveen@praveenkumar.co.in)
	 * 
	 */
	private class courseSyncerBg extends AsyncTask<String, Integer, Boolean> {
		@Override
		protected Boolean doInBackground(String... params) {
			CourseSyncTask cs = new CourseSyncTask(session.getmUrl(),
					session.getToken(), session.getCurrentSiteId());
			if (cs.syncUserCourses())
				return true;
			else
				return false;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			if (Type == TYPE_USER_COURSES)
				mCourses = MoodleCourse.find(MoodleCourse.class,
						"siteid = ? and is_user_course = ?",
						session.getCurrentSiteId() + "", "1");
			else if (Type == TYPE_FAV_COURSES)
				mCourses = MoodleCourse.find(MoodleCourse.class,
						"siteid = ? and is_fav_course = ?",
						session.getCurrentSiteId() + "", "1");
			else
				mCourses = MoodleCourse.find(MoodleCourse.class,
						"siteid = ? and ", session.getCurrentSiteId() + "");
			courseListAdapter.notifyDataSetChanged();
			if (mCourses.size() != 0)
				courseEmptyLayout.setVisibility(LinearLayout.GONE);
		}
	}

}
