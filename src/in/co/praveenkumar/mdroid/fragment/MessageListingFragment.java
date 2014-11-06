package in.co.praveenkumar.mdroid.fragment;

import in.co.praveenkumar.R;
import in.co.praveenkumar.mdroid.helper.LetterColor;
import in.co.praveenkumar.mdroid.helper.SessionSetting;
import in.co.praveenkumar.mdroid.moodlemodel.MoodleMessage;
import in.co.praveenkumar.mdroid.task.MessageSyncTask;

import java.util.ArrayList;
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
	List<ListMessage> messages;
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
		setupMessages();

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
			setupMessages();
			adapter.notifyDataSetChanged();
			if (messages.size() != 0)
				messagesEmptyLayout.setVisibility(LinearLayout.GONE);
		}

	}

	public class MessageListAdapter extends BaseAdapter {

		private final Context context;

		public MessageListAdapter(Context context) {
			this.context = context;
			if (messages == null || messages.size() != 0)
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
				viewHolder.lastmessage = (TextView) convertView
						.findViewById(R.id.list_message_last_message);

				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}

			// Contact image color and value
			String name = messages.get(position).userfullname;
			char firstChar = 0;
			if (name.length() != 0)
				firstChar = name.charAt(0);
			viewHolder.userimage.setText(firstChar + "");
			viewHolder.userimage.setBackgroundColor(LetterColor.of(firstChar));

			// Name and last message
			viewHolder.userfullname
					.setText(messages.get(position).userfullname);
			viewHolder.lastmessage.setText(messages.get(position).message
					.getText());

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
		TextView userfullname;
		TextView lastmessage;
	}

	void setupMessages() {
		List<MoodleMessage> mMessages = MoodleMessage.find(MoodleMessage.class,
				"siteid = ?", session.getCurrentSiteId() + "");
		List<ListMessage> lMessages = new ArrayList<MessageListingFragment.ListMessage>();

		// -TODO- Sort messages by time

		List<Integer> userids = new ArrayList<Integer>();
		int currentuserid = session.getSiteInfo().getUserid();

		for (int i = 0; i < mMessages.size(); i++) {

			// Message sent by current user
			if (currentuserid != mMessages.get(i).getUseridto()
					&& !isInList(userids, mMessages.get(i).getUseridto())) {
				ListMessage mes = new ListMessage();
				mes.message = mMessages.get(i);
				mes.userid = mMessages.get(i).getUseridto();
				mes.userfullname = mMessages.get(i).getUsertofullname();
				lMessages.add(mes);
				userids.add(mMessages.get(i).getUseridto());
			}

			// Message received by current user
			else if (!isInList(userids, mMessages.get(i).getUseridfrom())) {
				ListMessage mes = new ListMessage();
				mes.message = mMessages.get(i);
				mes.userid = mMessages.get(i).getUseridfrom();
				mes.userfullname = mMessages.get(i).getUserfromfullname();
				lMessages.add(mes);
				userids.add(mMessages.get(i).getUseridfrom());
			}
		}

		// Assign to messages now and make sure notifydatasetchanged is called
		// soon after this
		messages = (lMessages != null) ? lMessages
				: new ArrayList<MessageListingFragment.ListMessage>();

	}

	Boolean isInList(List<Integer> ids, int id) {
		for (int i = 0; i < ids.size(); i++)
			if (ids.get(i).intValue() == id)
				return true;
		return false;
	}

	class ListMessage {
		/**
		 * Moodle message
		 */
		MoodleMessage message;
		/**
		 * userid of the user, other the current login user, who is
		 * participating in this message
		 */
		int userid;

		/**
		 * Fullname of the user, other the current login user, who is
		 * participating in this message
		 */
		String userfullname;
	}

}
