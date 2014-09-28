package in.co.praveenkumar.mdroid.fragment;

import in.co.praveenkumar.mdroid.apis.R;
import in.co.praveenkumar.mdroid.helper.SessionSetting;
import in.co.praveenkumar.mdroid.helper.TimeFormat;
import in.co.praveenkumar.mdroid.moodlemodel.MoodleDiscussion;
import in.co.praveenkumar.mdroid.task.DiscussionSyncTask;

import java.util.List;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class DiscussionFragment extends Fragment {
	TopicListAdapter topicListAdapter;
	SessionSetting session;
	int forumid = 30;
	List<MoodleDiscussion> mTopics;
	LinearLayout topicsEmptyLayout;

	/**
	 * This constructor lists all forums in the site. Don't use this
	 * constructor.
	 */
	public DiscussionFragment() {
	}

	/**
	 * If you want to list all forums, use courseid = 0
	 * 
	 * @param courseid
	 */
	public DiscussionFragment(int forumid) {
		this.forumid = forumid;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.frag_discussion, container,
				false);
		topicsEmptyLayout = (LinearLayout) rootView
				.findViewById(R.id.discussion_empty_layout);

		if (forumid == 0)
			return rootView;

		session = new SessionSetting(getActivity());
		mTopics = MoodleDiscussion.find(MoodleDiscussion.class,
				"siteid = ? and forumid = ?", session.getCurrentSiteId() + "",
				forumid + "");

		ListView forumList = (ListView) rootView
				.findViewById(R.id.content_discussion);
		topicListAdapter = new TopicListAdapter(getActivity());

		forumList.setAdapter(topicListAdapter);
		new AsyncTopicsSync(session.getmUrl(), session.getToken(),
				session.getCurrentSiteId()).execute("");
		return rootView;
	}

	public class TopicListAdapter extends BaseAdapter {
		private final Context context;

		public TopicListAdapter(Context context) {
			this.context = context;
			if (mTopics.size() != 0)
				topicsEmptyLayout.setVisibility(LinearLayout.GONE);
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			final ViewHolder viewHolder;

			if (convertView == null) {
				viewHolder = new ViewHolder();
				LayoutInflater inflater = (LayoutInflater) context
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

				convertView = inflater.inflate(R.layout.list_item_discussion,
						parent, false);

				viewHolder.topicname = (TextView) convertView
						.findViewById(R.id.discussion_name);
				viewHolder.topicreplies = (TextView) convertView
						.findViewById(R.id.discussion_reply_count);
				viewHolder.topicstarttime = (TextView) convertView
						.findViewById(R.id.discussion_startedtime);
				viewHolder.topicstartby = (TextView) convertView
						.findViewById(R.id.discussion_startedby);
				viewHolder.topiclasttime = (TextView) convertView
						.findViewById(R.id.discussion_lastmodifiedtime);
				viewHolder.topiclastby = (TextView) convertView
						.findViewById(R.id.discussion_lastmodifiedby);

				// Save the holder with the view
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}

			// Assign values
			viewHolder.topicname.setText(mTopics.get(position).getName());
			viewHolder.topicreplies.setText(mTopics.get(position)
					.getNumreplies());
			viewHolder.topicstarttime.setText(TimeFormat.getNiceTime(mTopics
					.get(position).getTimestart()));
			viewHolder.topicstartby.setText(mTopics.get(position)
					.getFirstuserfullname());
			viewHolder.topiclasttime.setText(TimeFormat.getNiceTime(mTopics
					.get(position).getTimemodified()));
			viewHolder.topiclastby.setText(mTopics.get(position)
					.getLastuserfullname());

			return convertView;
		}

		@Override
		public int getCount() {
			return mTopics.size();
		}

		@Override
		public Object getItem(int position) {
			return mTopics.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}
	}

	static class ViewHolder {
		TextView topicname;
		TextView topicreplies;
		TextView topicstarttime;
		TextView topicstartby;
		TextView topiclasttime;
		TextView topiclastby;
	}

	private class AsyncTopicsSync extends AsyncTask<String, Integer, Boolean> {
		Long siteid;
		DiscussionSyncTask dst;
		Boolean syncStatus;

		public AsyncTopicsSync(String mUrl, String token, Long siteid) {
			this.siteid = siteid;
			dst = new DiscussionSyncTask(mUrl, token, siteid);
		}

		@Override
		protected Boolean doInBackground(String... params) {
			syncStatus = dst.syncDiscussions(forumid);

			if (syncStatus) {
				mTopics = MoodleDiscussion
						.find(MoodleDiscussion.class,
								"siteid = ? and forumid = ?", siteid + "",
								forumid + "");
				return true;
			} else
				return false;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			topicListAdapter.notifyDataSetChanged();
			if (mTopics.size() != 0)
				topicsEmptyLayout.setVisibility(LinearLayout.GONE);
		}

	}
}
