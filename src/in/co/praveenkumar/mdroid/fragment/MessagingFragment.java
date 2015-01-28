package in.co.praveenkumar.mdroid.fragment;

import in.co.praveenkumar.R;
import in.co.praveenkumar.mdroid.helper.AppInterface.UserIdInterface;
import in.co.praveenkumar.mdroid.helper.ApplicationClass;
import in.co.praveenkumar.mdroid.helper.ImageDecoder;
import in.co.praveenkumar.mdroid.helper.LetterColor;
import in.co.praveenkumar.mdroid.helper.Param;
import in.co.praveenkumar.mdroid.helper.SessionSetting;
import in.co.praveenkumar.mdroid.helper.Workaround;
import in.co.praveenkumar.mdroid.model.MoodleMessage;
import in.co.praveenkumar.mdroid.moodlerest.MoodleRestMessage;
import in.co.praveenkumar.mdroid.task.MessageSyncTask;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MessagingFragment extends Fragment implements OnRefreshListener {
	final String DEBUG_TAG = "MessageListingFragment";
	Context context;
	List<MoodleMessage> messages = new ArrayList<MoodleMessage>();
	MessageListAdapter adapter;
	SessionSetting session;
	LinearLayout messagingEmptyLayout;
	int userid;
	Bitmap loginUserImage = null;
	EditText messageET;
	SwipeRefreshLayout swipeLayout;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		// Send a tracker
		((ApplicationClass) getActivity().getApplication())
				.sendScreen(Param.GA_SCREEN_MESSAGING);

		View rootView = inflater.inflate(R.layout.frag_messaging, container,
				false);
		messagingEmptyLayout = (LinearLayout) rootView
				.findViewById(R.id.messaging_empty_layout);
		ListView messageList = (ListView) rootView
				.findViewById(R.id.content_messaging);
		messageET = (EditText) rootView
				.findViewById(R.id.messaging_message_text);
		ImageView sendBtn = (ImageView) rootView
				.findViewById(R.id.messaging_sendbutton);

		// Get siteinfo and user info
		this.context = getActivity();
		session = new SessionSetting(context);
		loginUserImage = ImageDecoder.decodeImage(new File(Environment
				.getExternalStorageDirectory()
				+ "/MDroid/."
				+ session.getCurrentSiteId()));

		// Set on click listener for message sending
		sendBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// Add message to list for a quick visual response to user
				messages.add(new MoodleMessage(session.getSiteInfo()
						.getUserid(), userid, messageET.getText().toString(),
						(int) System.currentTimeMillis() / 1000));
				adapter.notifyDataSetChanged();

				new AsyncMessageSender(session.getmUrl(), session.getToken(),
						userid, messageET.getText().toString()).execute("");
				messageET.setText("");
			}
		});

		// Setup listview adapter
		adapter = new MessageListAdapter(getActivity());
		messageList.setAdapter(adapter);

		// Set initial messages
		setupMessages();
		adapter.notifyDataSetChanged();

		swipeLayout = (SwipeRefreshLayout) rootView
				.findViewById(R.id.swipe_refresh);
		Workaround.linkSwipeRefreshAndListView(swipeLayout, messageList);
		swipeLayout.setOnRefreshListener(this);

		// Refresh messages from server
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
			Log.d(DEBUG_TAG, a.toString()
					+ " did not implement UserIdInterface.");
		}
	}

	private class MessageSyncerBg extends AsyncTask<String, Integer, Boolean> {

		@Override
		protected void onPreExecute() {
			swipeLayout.setRefreshing(true);
		}

		@Override
		protected Boolean doInBackground(String... params) {
			// Sync from server and update
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
			swipeLayout.setRefreshing(false);
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

			String msg = null;

			// Choose layout
			switch (type) {
			case TYPE_MESSAGE_IN:
				// Contact image color
				String name = messages.get(position).getUserfromfullname();
				char firstChar = 0;
				if (name.length() != 0)
					firstChar = name.charAt(0);
				viewHolder.userimage.setText(firstChar + "");
				viewHolder.userimage.setBackgroundColor(LetterColor
						.of(firstChar));

				// Set message after trimming html special chars
				msg = messages.get(position).getText();
				msg = (msg == null) ? "" : Html.fromHtml(msg).toString().trim();
				viewHolder.message.setText(msg);
				break;
			case TYPE_MESSAGE_OUT:
				// Set message after trimming html special chars
				msg = messages.get(position).getText();
				msg = (msg == null) ? "" : Html.fromHtml(msg).toString().trim();
				viewHolder.message.setText(msg);

				// Set user image
				if (loginUserImage != null)
					viewHolder.userIcon.setImageBitmap(loginUserImage);
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

		@Override
		public void notifyDataSetChanged() {
			if (messages.size() != 0)
				messagingEmptyLayout.setVisibility(LinearLayout.GONE);
			super.notifyDataSetChanged();
		}
	}

	static class ViewHolder {
		TextView userimage;
		ImageView userIcon;
		TextView message;
	}

	void setupMessages() {
		List<MoodleMessage> mMessages = MoodleMessage.find(MoodleMessage.class,
				"useridfrom = ? and siteid = ? or useridto = ? and siteid = ?",
				userid + "", session.getCurrentSiteId() + "", userid + "",
				session.getCurrentSiteId() + "");

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

	private class AsyncMessageSender extends
			AsyncTask<String, Integer, Boolean> {
		String mUrl;
		String token;
		int userid;
		String message;
		MoodleRestMessage mrm;

		public AsyncMessageSender(String mUrl, String token, int userid,
				String message) {
			this.mUrl = mUrl;
			this.token = token;
			this.userid = userid;
			this.message = message;
		}

		@Override
		protected Boolean doInBackground(String... params) {
			mrm = new MoodleRestMessage(mUrl, token);
			MoodleMessage mMessage = new MoodleMessage(userid, message + "\n"
					+ session.getMessageSignature());
			return mrm.sendMessage(mMessage);
		}

		@Override
		protected void onPostExecute(Boolean result) {
			if (!result) {
				Toast.makeText(context, mrm.getError(), Toast.LENGTH_LONG)
						.show();
				messageET.setText(messages.get(messages.size() - 1).getText());
				messages.remove(messages.size() - 1);
			} else {
				// Refresh messages from server. The user will probably see the
				// same thing though.
				new MessageSyncerBg().execute("");
			}
		}

	}

	@Override
	public void onRefresh() {
		new MessageSyncerBg().execute("");
	}
}
