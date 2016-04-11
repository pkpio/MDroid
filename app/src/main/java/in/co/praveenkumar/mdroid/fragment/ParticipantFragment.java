package in.co.praveenkumar.mdroid.fragment;

import in.co.praveenkumar.R;
import in.co.praveenkumar.mdroid.dialog.UserinfoDialog;
import in.co.praveenkumar.mdroid.helper.LetterColor;
import in.co.praveenkumar.mdroid.helper.SessionSetting;
import in.co.praveenkumar.mdroid.helper.Workaround;
import in.co.praveenkumar.mdroid.model.MoodleUser;
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
	public void setCourseid(int courseid) {
		this.courseid = courseid;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.frag_participant, container,
				false);
		listEmptyLayout = (LinearLayout) rootView
				.findViewById(R.id.list_empty_layout);
		ListView participantList = (ListView) rootView
				.findViewById(R.id.participant_list);

		// Get sites info
		session = new SessionSetting(getActivity());
		participants = MoodleUser.find(MoodleUser.class,
				"siteid = ? and courseid = ?", String.valueOf(session.getCurrentSiteId()),
				String.valueOf(courseid));

		adapter = new ParticipantListAdapter(getActivity());
		participantList.setAdapter(adapter);

		// OnItem click
		participantList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View view,
					int position, long arg3) {
				MoodleUser participant = participants.get(position);
				UserinfoDialog uid = new UserinfoDialog(getActivity(), session
						.getCurrentSiteId(), participant.getUserid());
				uid.show();
			}
		});

		swipeLayout = (SwipeRefreshLayout) rootView
				.findViewById(R.id.swipe_refresh);
		Workaround.linkSwipeRefreshAndListView(swipeLayout, participantList);
		swipeLayout.setOnRefreshListener(this);

		new ParticipantSyncerBg().execute("");

		return rootView;
	}

	public class ParticipantListAdapter extends BaseAdapter {

		private final Context context;

		public ParticipantListAdapter(Context context) {
			this.context = context;
			if (!participants.isEmpty())
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
			return ust.syncUsers(courseid);
		}

		@Override
		protected void onPostExecute(Boolean result) {
			participants = MoodleUser.find(MoodleUser.class,
					"siteid = ? and courseid = ?", String.valueOf(session.getCurrentSiteId())
							, String.valueOf(courseid));
			adapter.notifyDataSetChanged();
			if (!participants.isEmpty())
				listEmptyLayout.setVisibility(LinearLayout.GONE);
			swipeLayout.setRefreshing(false);
		}

	}

	@Override
	public void onRefresh() {
		new ParticipantSyncerBg().execute("");
	}
}
