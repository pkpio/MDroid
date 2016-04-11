package in.co.praveenkumar.mdroid.fragment;

import in.co.praveenkumar.R;
import in.co.praveenkumar.mdroid.dialog.MessageDialog;
import in.co.praveenkumar.mdroid.helper.AppInterface.DrawerStateInterface;
import in.co.praveenkumar.mdroid.helper.LetterColor;
import in.co.praveenkumar.mdroid.helper.SessionSetting;
import in.co.praveenkumar.mdroid.model.MoodleContact;
import in.co.praveenkumar.mdroid.task.ContactSyncTask;

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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class RightNavigationFragment extends Fragment {
	DrawerStateInterface drawerState;
	final String DEBUG_TAG = "ContactsFragment";
	List<MoodleContact> contacts;
	RightNavListAdapter adapter;
	SessionSetting session;
	LinearLayout chatEmptyLayout;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.frag_contact,
				container, false);
		chatEmptyLayout = (LinearLayout) rootView
				.findViewById(R.id.chat_empty_layout);
		ListView navListView = (ListView) rootView
				.findViewById(R.id.right_nav_list);

		// Get sites info
		session = new SessionSetting(getActivity());
		contacts = MoodleContact.find(MoodleContact.class, "siteid = ?",
				session.getCurrentSiteId() + "");

		adapter = new RightNavListAdapter(getActivity());
		navListView.setAdapter(adapter);

		// OnItem click
		navListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				MoodleContact contact = contacts.get(position);
				MessageDialog md = new MessageDialog(getActivity());
				md.setContact(contact);
				md.show();
			}
		});

		new contactSyncerBg().execute("");

		return rootView;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			drawerState = (DrawerStateInterface) activity;
		} catch (ClassCastException castException) {
			Log.d(DEBUG_TAG, "The activity does not implement the listener");
		}
	}

	public class RightNavListAdapter extends BaseAdapter {

		private final Context context;

		public RightNavListAdapter(Context context) {
			this.context = context;
			if (!contacts.isEmpty())
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
			if (!name.isEmpty())
				firstChar = name.charAt(0);
			viewHolder.userimage.setText(String.valueOf(firstChar));
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
				viewHolder.unreadcount.setText(String.valueOf(count));
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
		protected Boolean doInBackground(String... params) {
			ContactSyncTask cst = new ContactSyncTask(session.getmUrl(),
					session.getToken(), session.getCurrentSiteId());
			return cst.syncAllContacts();
		}

		@Override
		protected void onPostExecute(Boolean result) {
			contacts = MoodleContact.find(MoodleContact.class, "siteid = ?",
					session.getCurrentSiteId() + "");
			adapter.notifyDataSetChanged();
			if (!contacts.isEmpty())
				chatEmptyLayout.setVisibility(LinearLayout.GONE);
		}

	}

}
