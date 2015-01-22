package in.co.praveenkumar.mdroid.fragment;

import in.co.praveenkumar.R;
import in.co.praveenkumar.mdroid.dialog.MessageDialog;
import in.co.praveenkumar.mdroid.dialog.UserinfoDialog;
import in.co.praveenkumar.mdroid.helper.LetterColor;
import in.co.praveenkumar.mdroid.helper.SessionSetting;
import in.co.praveenkumar.mdroid.moodlemodel.MoodleUser;
import in.co.praveenkumar.mdroid.task.UserSyncTask;

import java.util.List;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class ParticipantFragment extends Fragment implements OnRefreshListener {
	final String DEBUG_TAG = "ContactsFragment";
	List<MoodleUser> participants;
	int courseid = 0;
	ParticipantListAdapter adapter;
	SessionSetting session;
	LinearLayout listEmptyLayout;
	SwipeRefreshLayout swipeLayout;
	ListView participantList;

	/**
	 * Don't use this constructor.
	 */
	public ParticipantFragment() {
	}

	/**
	 * If you want to list all forums, use courseid = 0
	 * 
	 * @param courseid
	 */
	public ParticipantFragment(int courseid) {
		this.courseid = courseid;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.frag_participant, container,
				false);
		listEmptyLayout = (LinearLayout) rootView
				.findViewById(R.id.list_empty_layout);
		participantList = (ListView) rootView
				.findViewById(R.id.participant_list);

		// Get sites info
		session = new SessionSetting(getActivity());
		participants = MoodleUser.find(MoodleUser.class,
				"siteid = ? and courseid = ?", session.getCurrentSiteId() + "",
				courseid + "");

		adapter = new ParticipantListAdapter(getActivity());
		participantList.setAdapter(adapter);

		// OnItem click
		participantList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// MoodleContact contact = contacts.get(position);
				UserinfoDialog uid = new UserinfoDialog(getActivity(), session
						.getCurrentSiteId());
				uid.show();
			}
		});

		swipeLayout = (SwipeRefreshLayout) rootView
				.findViewById(R.id.swipe_refresh);
		setupSwipeLayout();

		new ParticipantSyncerBg().execute("");

		return rootView;
	}

	public class ParticipantListAdapter extends BaseAdapter {

		private final Context context;

		public ParticipantListAdapter(Context context) {
			this.context = context;
			if (participants.size() != 0)
				listEmptyLayout.setVisibility(LinearLayout.GONE);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder;

			if (convertView == null) {
				viewHolder = new ViewHolder();
				LayoutInflater inflater = (LayoutInflater) context
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

				convertView = inflater.inflate(R.layout.list_item_participant,
						parent, false);

				viewHolder.userimage = (TextView) convertView
						.findViewById(R.id.list_contact_image);
				viewHolder.userfullname = (TextView) convertView
						.findViewById(R.id.list_contact_name);

				// Save the holder with the view
				convertView.setTag(viewHolder);
			} else {
				// Just use the viewHolder and avoid findviewbyid()
				viewHolder = (ViewHolder) convertView.getTag();
			}

			// Contact image color and value
			String name = participants.get(position).getFullname();
			char firstChar = 0;
			if (name.length() != 0)
				firstChar = name.charAt(0);
			viewHolder.userimage.setText(firstChar + "");
			viewHolder.userimage.setBackgroundColor(LetterColor.of(firstChar));

			// Name
			viewHolder.userfullname.setText(participants.get(position)
					.getFullname());

			return convertView;
		}

		@Override
		public int getCount() {
			return participants.size();
		}

		@Override
		public Object getItem(int position) {
			return participants.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}
	}

	static class ViewHolder {
		TextView userimage;
		TextView userfullname;
		TextView unreadcount;
	}

	private class ParticipantSyncerBg extends
			AsyncTask<String, Integer, Boolean> {

		@Override
		protected void onPreExecute() {
			swipeLayout.setRefreshing(true);
		}

		@Override
		protected Boolean doInBackground(String... params) {
			UserSyncTask ust = new UserSyncTask(session.getmUrl(),
					session.getToken(), session.getCurrentSiteId());
			if (ust.syncUsers(courseid))
				return true;
			else
				return false;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			participants = MoodleUser.find(MoodleUser.class,
					"siteid = ? and courseid = ?", session.getCurrentSiteId()
							+ "", courseid + "");
			adapter.notifyDataSetChanged();
			if (participants.size() != 0)
				listEmptyLayout.setVisibility(LinearLayout.GONE);
			swipeLayout.setRefreshing(false);
		}

	}

	@Override
	public void onRefresh() {
		new ParticipantSyncerBg().execute("");
	}

	void setupSwipeLayout() {
		if (swipeLayout == null || participantList == null)
			return;

		swipeLayout.setColorSchemeResources(R.color.refresh_green,
				R.color.refresh_red, R.color.refresh_blue,
				R.color.refresh_yellow);
		swipeLayout.setOnRefreshListener(this);

		// Link swipeLayout with underlying listview
		participantList.setOnScrollListener(new AbsListView.OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				int topRowVerticalPosition = (participantList == null || participantList
						.getChildCount() == 0) ? 0 : participantList
						.getChildAt(0).getTop();
				swipeLayout.setEnabled(topRowVerticalPosition >= 0);
			}
		});
	}
}
