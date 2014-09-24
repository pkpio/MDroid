package in.co.praveenkumar.mdroid.fragment;

import in.co.praveenkumar.mdroid.apis.R;
import in.co.praveenkumar.mdroid.helper.AppInterface.DrawerStateInterface;
import in.co.praveenkumar.mdroid.helper.SessionSetting;
import in.co.praveenkumar.mdroid.moodlemodel.MoodleContact;
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
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class ContactsFragment extends Fragment {
	DrawerStateInterface drawerState;
	final String DEBUG_TAG = "ContactsFragment";
	ListView navListView;
	List<MoodleContact> contacts;
	RightNavListAdapter adapter;
	SessionSetting session;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.frag_right_navigation,
				container, false);
		navListView = (ListView) rootView.findViewById(R.id.right_nav_list);

		// Get sites info
		session = new SessionSetting(getActivity());
		contacts = MoodleContact.find(MoodleContact.class, "siteid = ?",
				session.getCurrentSiteId() + "");

		if (contacts != null)
			if (contacts.size() != 0) {
				adapter = new RightNavListAdapter(getActivity());
				navListView.setAdapter(adapter);
			}

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

	public class RightNavListAdapter extends ArrayAdapter<String> {

		private final Context context;

		public RightNavListAdapter(Context context) {
			super(context, R.layout.list_item_account,
					new String[(contacts == null) ? 0 : contacts.size()]);
			this.context = context;
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

			viewHolder.userfullname.setText(contacts.get(position)
					.getFullname());
			viewHolder.unreadcount.setText(contacts.get(position).getUnread()
					+ "");

			return convertView;
		}
	}

	static class ViewHolder {
		TextView userfullname;
		TextView unreadcount;
	}

	private class contactSyncerBg extends AsyncTask<String, Integer, Boolean> {

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
			if (result) {
				contacts = MoodleContact.find(MoodleContact.class,
						"siteid = ?", session.getCurrentSiteId() + "");
				if (adapter == null)
					try {
						adapter = new RightNavListAdapter(getActivity());
					} catch (Exception e) {
						// sometimes user may have navigated to a different
						// activity where contacts layout may not exist. Throws
						// a null pointer exception
					}
				navListView.setAdapter(adapter);
			}
		}

	}

}
