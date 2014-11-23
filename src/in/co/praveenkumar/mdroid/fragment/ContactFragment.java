package in.co.praveenkumar.mdroid.fragment;

import in.co.praveenkumar.R;
import in.co.praveenkumar.mdroid.dialog.MessageDialog;
import in.co.praveenkumar.mdroid.helper.LetterColor;
import in.co.praveenkumar.mdroid.helper.SessionSetting;
import in.co.praveenkumar.mdroid.moodlemodel.MoodleContact;
import in.co.praveenkumar.mdroid.task.ContactSyncTask;

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
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class ContactFragment extends Fragment implements OnRefreshListener {
	final String DEBUG_TAG = "ContactsFragment";
	List<MoodleContact> contacts;
	ContactListAdapter adapter;
	SessionSetting session;
	LinearLayout chatEmptyLayout;
	SwipeRefreshLayout swipeLayout;
	ListView contactList;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.frag_contact, container,
				false);
		chatEmptyLayout = (LinearLayout) rootView
				.findViewById(R.id.chat_empty_layout);
		contactList = (ListView) rootView.findViewById(R.id.right_nav_list);

		// Get sites info
		session = new SessionSetting(getActivity());
		contacts = MoodleContact.find(MoodleContact.class, "siteid = ?",
				session.getCurrentSiteId() + "");

		adapter = new ContactListAdapter(getActivity());
		contactList.setAdapter(adapter);

		// OnItem click
		contactList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				MoodleContact contact = contacts.get(position);
				MessageDialog md = new MessageDialog(getActivity());
				md.setContact(contact);
				md.show();
			}
		});

		swipeLayout = (SwipeRefreshLayout) rootView
				.findViewById(R.id.swipe_refresh);
		setupSwipeLayout();

		new contactSyncerBg().execute("");

		return rootView;
	}

	public class ContactListAdapter extends BaseAdapter {

		private final Context context;

		public ContactListAdapter(Context context) {
			this.context = context;
			if (contacts.size() != 0)
				chatEmptyLayout.setVisibility(LinearLayout.GONE);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder;

			if (convertView == null) {
				viewHolder = new ViewHolder();
				LayoutInflater inflater = (LayoutInflater) context
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

				convertView = inflater.inflate(R.layout.list_item_contact,
						parent, false);

				viewHolder.userimage = (TextView) convertView
						.findViewById(R.id.list_contact_image);
				viewHolder.userfullname = (TextView) convertView
						.findViewById(R.id.list_contact_name);
				viewHolder.unreadcount = (TextView) convertView
						.findViewById(R.id.list_unread_count);

				// Save the holder with the view
				convertView.setTag(viewHolder);
			} else {
				// Just use the viewHolder and avoid findviewbyid()
				viewHolder = (ViewHolder) convertView.getTag();
			}

			// Contact image color and value
			String name = contacts.get(position).getFullname();
			char firstChar = 0;
			if (name.length() != 0)
				firstChar = name.charAt(0);
			viewHolder.userimage.setText(firstChar + "");
			viewHolder.userimage.setBackgroundColor(LetterColor.of(firstChar));

			// Name
			viewHolder.userfullname.setText(contacts.get(position)
					.getFullname());

			// Unread counts
			int count = contacts.get(position).getUnread();
			if (count == 0)
				viewHolder.unreadcount.setVisibility(TextView.GONE);
			else {
				viewHolder.unreadcount.setVisibility(TextView.VISIBLE);
				viewHolder.unreadcount.setText(count + "");
			}

			switch (contacts.get(position).getStatus()) {
			case MoodleContact.STATUS_ONLINE:
				// viewHolder.unreadcount
				// .setBackgroundResource(R.drawable.circular_online_bg);
				break;
			case MoodleContact.STATUS_OFFLINE:
				// viewHolder.unreadcount
				// .setBackgroundResource(R.drawable.circular_offline_bg);
				break;
			case MoodleContact.STATUS_STRANGER:
				// viewHolder.unreadcount
				// .setBackgroundResource(R.drawable.circular_stranger_bg);
				break;
			}

			return convertView;
		}

		@Override
		public int getCount() {
			return contacts.size();
		}

		@Override
		public Object getItem(int position) {
			return contacts.get(position);
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

	private class contactSyncerBg extends AsyncTask<String, Integer, Boolean> {

		@Override
		protected void onPreExecute() {
			swipeLayout.setRefreshing(true);
		}

		@Override
		protected Boolean doInBackground(String... params) {
			ContactSyncTask cst = new ContactSyncTask(session.getmUrl(),
					session.getToken(), session.getCurrentSiteId());
			if (cst.syncAllContacts())
				return true;
			else
				return false;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			contacts = MoodleContact.find(MoodleContact.class, "siteid = ?",
					session.getCurrentSiteId() + "");
			adapter.notifyDataSetChanged();
			if (contacts.size() != 0)
				chatEmptyLayout.setVisibility(LinearLayout.GONE);
			swipeLayout.setRefreshing(false);
		}

	}

	@Override
	public void onRefresh() {
		new contactSyncerBg().execute("");
	}

	void setupSwipeLayout() {
		if (swipeLayout == null || contactList == null)
			return;

		swipeLayout.setColorSchemeResources(R.color.refresh_green,
				R.color.refresh_red, R.color.refresh_blue,
				R.color.refresh_yellow);
		swipeLayout.setOnRefreshListener(this);

		// Link swipeLayout with underlying listview
		contactList.setOnScrollListener(new AbsListView.OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				int topRowVerticalPosition = (contactList == null || contactList
						.getChildCount() == 0) ? 0 : contactList.getChildAt(0)
						.getTop();
				swipeLayout.setEnabled(topRowVerticalPosition >= 0);
			}
		});
	}

}
