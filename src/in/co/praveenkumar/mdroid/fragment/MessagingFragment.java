package in.co.praveenkumar.mdroid.fragment;

import in.co.praveenkumar.R;
import in.co.praveenkumar.mdroid.helper.AppInterface.UserIdInterface;
import in.co.praveenkumar.mdroid.helper.LetterColor;
import in.co.praveenkumar.mdroid.helper.SessionSetting;
import in.co.praveenkumar.mdroid.moodlemodel.MoodleMessage;
import in.co.praveenkumar.mdroid.task.MessageSyncTask;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class MessagingFragment extends Fragment {
	final String DEBUG_TAG = "MessageListingFragment";
	List<MoodleMessage> messages = new ArrayList<MoodleMessage>();
	MessageListAdapter adapter;
	SessionSetting session;
	LinearLayout messagingEmptyLayout;
	int userid;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.frag_messaging, container,
				false);
		messagingEmptyLayout = (LinearLayout) rootView
				.findViewById(R.id.messaging_empty_layout);
		ListView navListView = (ListView) rootView
				.findViewById(R.id.content_messaging);

		adapter = new MessageListAdapter(getActivity());
		navListView.setAdapter(adapter);

		new MessageSyncerBg().execute("");

		return rootView;
	}

	@Override
	public void onAttach(Activity a) {
		super.onAttach(a);
		try {
			UserIdInterface useridInterface = (UserIdInterface) a;
			this.userid = useridInterface.getUserId();
		} catch (ClassCastException e) {
			e.printStackTrace();
			Log.d(DEBUG_TAG,
					a.toString()
							+ " did not implement DiscussionIdInterface. Fragment may not list any posts.");
		}
	}

	private class MessageSyncerBg extends AsyncTask<String, Integer, Boolean> {

		@Override
		protected Boolean doInBackground(String... params) {
			// Get sites info
			session = new SessionSetting(getActivity());

			// Setup previously sync messages
			setupMessages();
			publishProgress(0);

			// Sync from server and update
			MessageSyncTask mst = new MessageSyncTask(session.getmUrl(),
					session.getToken(), session.getCurrentSiteId());
			if (mst.syncMessages(session.getSiteInfo().getUserid())) {
				setupMessages();
				return true;
			}
			return false;
		}

		@Override
		protected void onProgressUpdate(Integer... progress) {
			adapter.notifyDataSetChanged();
			if (messages.size() != 0)
				messagingEmptyLayout.setVisibility(LinearLayout.GONE);
		}

		@Override
		protected void onPostExecute(Boolean result) {
			adapter.notifyDataSetChanged();
			if (messages.size() != 0)
				messagingEmptyLayout.setVisibility(LinearLayout.GONE);
		}

	}

	public class MessageListAdapter extends BaseAdapter {
		private final Context context;
		static final int TYPE_MESSAGE_IN = 0;
		static final int TYPE_MESSAGE_OUT = 1;
		static final int TYPE_COUNT = 2;

		public MessageListAdapter(Context context) {
			this.context = context;
			if (messages == null || messages.size() != 0)
				messagingEmptyLayout.setVisibility(LinearLayout.GONE);
		}

		@Override
		public int getViewTypeCount() {
			return TYPE_COUNT;
		}

		@Override
		public int getItemViewType(int position) {
			if (messages.get(position).getUseridto() == session.getSiteInfo()
					.getUserid())
				return TYPE_MESSAGE_IN;
			else
				return TYPE_MESSAGE_OUT;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder;
			int type = getItemViewType(position);

			if (convertView == null) {
				viewHolder = new ViewHolder();
				LayoutInflater inflater = (LayoutInflater) context
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

				// Choose layout
				switch (type) {
				case TYPE_MESSAGE_IN:
					convertView = inflater.inflate(
							R.layout.list_item_message_in, parent, false);

					viewHolder.userimage = (TextView) convertView
							.findViewById(R.id.list_message_user_image);
					viewHolder.message = (TextView) convertView
							.findViewById(R.id.list_message_text);
					break;
				case TYPE_MESSAGE_OUT:
					convertView = inflater.inflate(
							R.layout.list_item_message_out, parent, false);

					viewHolder.userIcon = (ImageView) convertView
							.findViewById(R.id.list_message_user_image);
					viewHolder.message = (TextView) convertView
							.findViewById(R.id.list_message_text);
					break;

				}

				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}

			// Choose layout
			switch (type) {
			case TYPE_MESSAGE_IN:
				// Contact image color and message
				String name = messages.get(position).getUserfromfullname();
				char firstChar = 0;
				if (name.length() != 0)
					firstChar = name.charAt(0);
				viewHolder.userimage.setText(firstChar + "");
				viewHolder.userimage.setBackgroundColor(LetterColor
						.of(firstChar));
				viewHolder.message.setText(messages.get(position).getText());
				break;
			case TYPE_MESSAGE_OUT:
				viewHolder.message.setText(messages.get(position).getText());
				break;
			}

			return convertView;
		}

		@Override
		public int getCount() {
			if (messages == null)
				return 0;
			return messages.size();
		}

		@Override
		public Object getItem(int position) {
			return messages.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}
	}

	static class ViewHolder {
		TextView userimage;
		ImageView userIcon;
		TextView message;
	}

	void setupMessages() {
		List<MoodleMessage> mMessages = MoodleMessage.find(MoodleMessage.class,
				"useridfrom = ? or useridto = ? and siteid = ?", userid + "",
				userid + "", session.getCurrentSiteId() + "");
		System.out.println("size- " + mMessages.size());

		// Sort messages with newest last in list
		Collections.sort(mMessages, new Comparator<MoodleMessage>() {
			public int compare(MoodleMessage m1, MoodleMessage m2) {
				if (m1.getTimecreated() == m2.getTimecreated())
					return 0;
				return m1.getTimecreated() < m2.getTimecreated() ? -1 : 1;
			}
		});

		messages = mMessages;
	}
}
