package in.co.praveenkumar.mdroid.fragment;

import in.co.praveenkumar.R;
import in.co.praveenkumar.mdroid.helper.SessionSetting;
import in.co.praveenkumar.mdroid.task.MessageSyncTask;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class MessageListingFragment extends Fragment {
	final String DEBUG_TAG = "MessageListingFragment";
	// List<MoodleContact> contacts;
	// ContactListAdapter adapter;
	SessionSetting session;
	LinearLayout chatEmptyLayout;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.frag_message_listing,
				container, false);

		// Get sites info
		session = new SessionSetting(getActivity());

		new contactSyncerBg().execute("");

		return rootView;
	}

	private class contactSyncerBg extends AsyncTask<String, Integer, Boolean> {

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

		}

	}

}
