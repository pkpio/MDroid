package in.co.praveenkumar.mdroid.fragment;

import in.co.praveenkumar.R;
import in.co.praveenkumar.mdroid.helper.AppInterface.DiscussionIdInterface;
import in.co.praveenkumar.mdroid.helper.LetterColor;
import in.co.praveenkumar.mdroid.helper.SessionSetting;
import in.co.praveenkumar.mdroid.helper.TimeFormat;
import in.co.praveenkumar.mdroid.helper.Workaround;
import in.co.praveenkumar.mdroid.model.MoodlePost;
import in.co.praveenkumar.mdroid.task.PostSyncTask;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class PostFragment extends Fragment implements OnRefreshListener {
	private final String DEBUG_TAG = "PostFragment";
	PostListAdapter postListAdapter;
	SessionSetting session;
	int discussionid = 0;
	List<MoodlePost> mPosts;
	LinearLayout postsEmptyLayout;
	SwipeRefreshLayout swipeLayout;

	/**
	 * This constructor lists all forums in the site. Don't use this
	 * constructor.
	 */
	public PostFragment() {
	}

	/**
	 * If you want to list all forums, use courseid = 0
	 * 
	 * @param courseid
	 */
	public PostFragment(int discussionid) {
		this.discussionid = discussionid;
	}

	@Override
	public void onAttach(Activity a) {
		super.onAttach(a);
		try {
			DiscussionIdInterface discussionidInterface = (DiscussionIdInterface) a;
			this.discussionid = discussionidInterface.getDiscussionId();
		} catch (ClassCastException e) {
			e.printStackTrace();
			Log.d(DEBUG_TAG,
					a.toString()
							+ " did not implement DiscussionIdInterface. Fragment may not list any posts.");
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.frag_post, container, false);
		postsEmptyLayout = (LinearLayout) rootView
				.findViewById(R.id.post_empty_layout);

		if (discussionid == 0)
			return rootView;

		session = new SessionSetting(getActivity());
		mPosts = MoodlePost.find(MoodlePost.class,
				"siteid = ? and discussionid = ?", session.getCurrentSiteId()
						+ "", discussionid + "");
		sortPostsByTime();

		ListView postList = (ListView) rootView.findViewById(R.id.content_post);
		postListAdapter = new PostListAdapter(getActivity());
		postList.setAdapter(postListAdapter);

		swipeLayout = (SwipeRefreshLayout) rootView
				.findViewById(R.id.swipe_refresh);
		Workaround.linkSwipeRefreshAndListView(swipeLayout, postList);
		swipeLayout.setOnRefreshListener(this);

		new AsyncPostsSync(session.getmUrl(), session.getToken(),
				session.getCurrentSiteId()).execute("");
		return rootView;
	}

	public class PostListAdapter extends BaseAdapter {
		private final Context context;

		public PostListAdapter(Context context) {
			this.context = context;
			if (mPosts.size() != 0)
				postsEmptyLayout.setVisibility(LinearLayout.GONE);
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			final ViewHolder viewHolder;

			if (convertView == null) {
				viewHolder = new ViewHolder();
				LayoutInflater inflater = (LayoutInflater) context
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

				convertView = inflater.inflate(R.layout.list_item_post, parent,
						false);

				viewHolder.postauthorimage = (TextView) convertView
						.findViewById(R.id.post_authorimage);
				viewHolder.postsubject = (TextView) convertView
						.findViewById(R.id.post_subject);
				viewHolder.postauthor = (TextView) convertView
						.findViewById(R.id.post_author);
				viewHolder.postlastmodified = (TextView) convertView
						.findViewById(R.id.post_lastmodifiedtime);
				viewHolder.postcontent = (TextView) convertView
						.findViewById(R.id.post_content);

				// Save the holder with the view
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}

			// Assign values
			viewHolder.postsubject.setText(mPosts.get(position).getSubject());
			viewHolder.postauthor.setText(mPosts.get(position)
					.getUserfullname());
			viewHolder.postlastmodified.setText(TimeFormat.getNiceTime(mPosts
					.get(position).getModified()));

			// Post content. Could be HTML. So, make it plain text first.
			String message = mPosts.get(position).getMessage();
			if (message == null)
				message = "";
			else
				message = Html.fromHtml(message).toString().trim();
			viewHolder.postcontent.setText(message);

			// Author image color and value
			String authorName = mPosts.get(position).getUserfullname();
			char firstChar = 0;
			if (authorName != null)
				if (authorName.length() != 0)
					firstChar = authorName.charAt(0);
			viewHolder.postauthorimage.setText(firstChar + "");
			viewHolder.postauthorimage.setBackgroundColor(LetterColor
					.of(firstChar));

			return convertView;
		}

		@Override
		public int getCount() {
			return mPosts.size();
		}

		@Override
		public Object getItem(int position) {
			return mPosts.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}
	}

	static class ViewHolder {
		TextView postauthorimage;
		TextView postsubject;
		TextView postauthor;
		TextView postlastmodified;
		TextView postcontent;
	}

	/**
	 * This will sort the posts by the time they were posted. This is requried
	 * after every update to posts list or else the order may be lost.
	 */
	private void sortPostsByTime() {
		Collections.sort(mPosts, new Comparator<MoodlePost>() {
			public int compare(MoodlePost o1, MoodlePost o2) {
				if (o1.getCreated() == o2.getCreated())
					return 0;
				return o1.getCreated() < o2.getCreated() ? -1 : 1;
			}
		});
	}

	private class AsyncPostsSync extends AsyncTask<String, Integer, Boolean> {
		Long siteid;
		PostSyncTask pst;
		Boolean syncStatus;

		public AsyncPostsSync(String mUrl, String token, Long siteid) {
			this.siteid = siteid;
			pst = new PostSyncTask(mUrl, token, siteid);
		}

		@Override
		protected void onPreExecute() {
			swipeLayout.setRefreshing(true);
		}

		@Override
		protected Boolean doInBackground(String... params) {
			syncStatus = pst.syncPosts(discussionid);

			if (syncStatus) {
				mPosts = MoodlePost.find(MoodlePost.class,
						"siteid = ? and discussionid = ?", siteid + "",
						discussionid + "");
				sortPostsByTime();
				return true;
			} else
				return false;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			postListAdapter.notifyDataSetChanged();
			if (mPosts.size() != 0)
				postsEmptyLayout.setVisibility(LinearLayout.GONE);
			swipeLayout.setRefreshing(false);
		}

	}

	@Override
	public void onRefresh() {
		new AsyncPostsSync(session.getmUrl(), session.getToken(),
				session.getCurrentSiteId()).execute("");
	}
}
