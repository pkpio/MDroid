package in.co.praveenkumar.mdroid.fragment;

import in.co.praveenkumar.mdroid.apis.R;
import in.co.praveenkumar.mdroid.helper.SessionSetting;
import in.co.praveenkumar.mdroid.helper.TimeFormat;
import in.co.praveenkumar.mdroid.moodlemodel.MoodleCourse;
import in.co.praveenkumar.mdroid.moodlemodel.MoodleForum;
import in.co.praveenkumar.mdroid.task.ForumSyncTask;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class ForumFragment extends Fragment {
	ForumListAdapter forumListAdapter;
	ListView forumList;
	SessionSetting session;
	int courseid = 0;
	List<MoodleForum> mForums;
	LinearLayout forumEmptyLayout;

	/**
	 * This constructor lists all forums in the site. Don't use this
	 * constructor.
	 */
	public ForumFragment() {
	}

	/**
	 * If you want to list all forums, use courseid = 0
	 * 
	 * @param courseid
	 */
	public ForumFragment(int courseid) {
		this.courseid = courseid;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.frag_forum, container, false);
		forumEmptyLayout = (LinearLayout) rootView
				.findViewById(R.id.forum_empty_layout);

		// Get all courses of this site or course
		session = new SessionSetting(getActivity());
		if (courseid == 0)
			mForums = MoodleForum.find(MoodleForum.class, "siteid = ?",
					session.getCurrentSiteId() + "");
		else
			mForums = MoodleForum.find(MoodleForum.class,
					"siteid = ? and courseid = ?", session.getCurrentSiteId()
							+ "", courseid + "");

		forumList = (ListView) rootView.findViewById(R.id.content_forum);
		forumListAdapter = new ForumListAdapter(getActivity());

		forumList.setAdapter(forumListAdapter);
		new AsyncForumSync(session.getmUrl(), session.getToken(),
				session.getCurrentSiteId()).execute("");
		return rootView;
	}

	public class ForumListAdapter extends BaseAdapter {
		private final Context context;

		public ForumListAdapter(Context context) {
			this.context = context;
			if (mForums.size() != 0)
				forumEmptyLayout.setVisibility(LinearLayout.GONE);
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			final ViewHolder viewHolder;

			if (convertView == null) {
				viewHolder = new ViewHolder();
				LayoutInflater inflater = (LayoutInflater) context
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

				convertView = inflater.inflate(R.layout.list_item_forum,
						parent, false);

				viewHolder.forumname = (TextView) convertView
						.findViewById(R.id.forum_name);
				viewHolder.forumcourse = (TextView) convertView
						.findViewById(R.id.forum_coursename);
				viewHolder.forumtime = (TextView) convertView
						.findViewById(R.id.forum_time);
				viewHolder.forumdesc = (TextView) convertView
						.findViewById(R.id.forum_desc);

				// Save the holder with the view
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}

			// Assign values
			viewHolder.forumname.setText(mForums.get(position).getName());
			viewHolder.forumcourse.setText(mForums.get(position)
					.getCoursename());
			viewHolder.forumtime.setText(TimeFormat.getRelTime(mForums.get(
					position).getTimemodified()));
			String intro = mForums.get(position).getIntro();
			if (intro == null)
				intro = "";
			else
				intro = Html.fromHtml(intro).toString().trim();
			viewHolder.forumdesc.setText(intro);

			return convertView;
		}

		@Override
		public int getCount() {
			return mForums.size();
		}

		@Override
		public Object getItem(int position) {
			return mForums.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}
	}

	static class ViewHolder {
		TextView forumname;
		TextView forumcourse;
		TextView forumtime;
		TextView forumdesc;
	}

	private class AsyncForumSync extends AsyncTask<String, Integer, Boolean> {
		Long siteid;
		ForumSyncTask fst;
		Boolean syncStatus;

		public AsyncForumSync(String mUrl, String token, Long siteid) {
			this.siteid = siteid;
			fst = new ForumSyncTask(mUrl, token, siteid);
		}

		@Override
		protected Boolean doInBackground(String... params) {
			// Get course ids
			List<MoodleCourse> mCourses = MoodleCourse.find(MoodleCourse.class,
					"siteid = ?", siteid + "");
			ArrayList<String> courseIds = new ArrayList<String>();
			for (int i = 0; i < mCourses.size(); i++)
				courseIds.add(mCourses.get(i).getCourseid() + "");
			syncStatus = fst.syncForums(courseIds);

			if (syncStatus) {
				mForums = MoodleForum.find(MoodleForum.class, "siteid = ?",
						session.getCurrentSiteId() + "");
				return true;
			} else
				return false;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			// forumListAdapter = new ForumListAdapter(getActivity());
			// forumList.setAdapter(forumListAdapter);
			System.out.println("Forums size:" + mForums.size());
			forumListAdapter.notifyDataSetChanged();
			if (mForums.size() != 0)
				forumEmptyLayout.setVisibility(LinearLayout.GONE);
		}

	}

}
