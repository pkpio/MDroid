package in.co.praveenkumar.mdroid.fragment;

import in.co.praveenkumar.R;
import in.co.praveenkumar.mdroid.helper.LetterColor;
import in.co.praveenkumar.mdroid.helper.SessionSetting;
import in.co.praveenkumar.mdroid.moodlemodel.MoodleMessage;
import in.co.praveenkumar.mdroid.task.MessageSyncTask;

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

public class MessageListingFragment extends Fragment {
	final String DEBUG_TAG = "MessageListingFragment";
	List<MoodleMessage> messages;
	MessageListAdapter adapter;
	SessionSetting session;
	LinearLayout messagesEmptyLayout;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.frag_message_listing,
				container, false);
		messagesEmptyLayout = (LinearLayout) rootView
				.findViewById(R.id.messages_empty_layout);
		ListView navListView = (ListView) rootView
				.findViewById(R.id.content_message_listing);

		// Get sites info
		session = new SessionSetting(getActivity());
		messages = MoodleMessage.find(MoodleMessage.class, "siteid = ?",
				session.getCurrentSiteId() + "");

		adapter = new MessageListAdapter(getActivity());
		navListView.setAdapter(adapter);

		new MessageSyncerBg().execute("");

		return rootView;
	}

	private class MessageSyncerBg extends AsyncTask<String, Integer, Boolean> {

		@Override
		protected Boolean doInBackground(String... params) {
			MessageSyncTask mst = new MessageSyncTask(session.getmUrl(),
					session.getToken(), session.getCurrentSiteId());
			if (mst.syncMessages(session.getSiteInfo().getUserid()))
				return true;
			else
				return false;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			messages = MoodleMessage.find(MoodleMessage.class, "siteid = ?",
					session.getCurrentSiteId() + "");
			adapter.notifyDataSetChanged();
			if (messages.size() != 0)
				messagesEmptyLayout.setVisibility(LinearLayout.GONE);
		}

	}

	public class MessageListAdapter extends BaseAdapter {

		private final Context context;

		public MessageListAdapter(Context context) {
			this.context = context;
			if (messages.size() != 0)
				messagesEmptyLayout.setVisibility(LinearLayout.GONE);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder;

			if (convertView == null) {
				viewHolder = new ViewHolder();
				LayoutInflater inflater = (LayoutInflater) context
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

				convertView = inflater.inflate(R.layout.list_item_message_list,
						parent, false);

				viewHolder.userimage = (TextView) convertView
						.findViewById(R.id.list_message_contact_image);
				viewHolder.userfullname = (TextView) convertView
						.findViewById(R.id.list_message_contact_name);
				viewHolder.unreadcount = (TextView) convertView
						.findViewById(R.id.list_message_unread_count);
				viewHolder.lastmessage = (TextView) convertView
						.findViewById(R.id.list_message_last_message);

				// Save the holder with the view
				convertView.setTag(viewHolder);
			} else {
				// Just use the viewHolder and avoid findviewbyid()
				viewHolder = (ViewHolder) convertView.getTag();
			}

			// Contact image color and value
			String name = messages.get(position).getUserfromfullname();
			char firstChar = 0;
			if (name.length() != 0)
				firstChar = name.charAt(0);
			viewHolder.userimage.setText(firstChar + "");
			viewHolder.userimage.setBackgroundColor(LetterColor.of(firstChar));

			// Name
			viewHolder.userfullname.setText(messages.get(position)
					.getUserfromfullname());

			// Unread counts
			// -TODO- Get contact and do accordingly
			int count = 0;
			if (count == 0)
				viewHolder.unreadcount.setVisibility(TextView.GONE);
			else {
				viewHolder.unreadcount.setVisibility(TextView.VISIBLE);
				viewHolder.unreadcount.setText(count + "");
			}

			// Last message
			viewHolder.lastmessage.setText(messages.get(position).getText());

			return convertView;
		}

		@Override
		public int getCount() {
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
		TextView userfullname;
		TextView unreadcount;
		TextView lastmessage;
	}

}
