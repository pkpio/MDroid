package in.co.praveenkumar.mdroid.fragment;

import in.co.praveenkumar.mdroid.apis.R;
import in.co.praveenkumar.mdroid.helper.ImageDecoder;
import in.co.praveenkumar.mdroid.helper.SessionSetting;
import in.co.praveenkumar.mdroid.moodlemodel.MoodleSiteInfo;

import java.io.File;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class LeftNavigation extends Fragment {
	ListView navListView;
	List<MoodleSiteInfo> sites;
	MoodleSiteInfo currentSite;
	String[] menuItems = new String[] { "Courses", "Calender", "Forums",
			"Notes" };

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.frag_left_navigation,
				container, false);
		navListView = (ListView) rootView.findViewById(R.id.left_nav_list);

		// Get sites info
		SessionSetting session = new SessionSetting(getActivity());
		currentSite = session.getSiteInfo();
		sites = MoodleSiteInfo.listAll(MoodleSiteInfo.class);

		final LeftNavListAdapter adapter = new LeftNavListAdapter(getActivity());
		navListView.setAdapter(adapter);

		return rootView;
	}

	public class LeftNavListAdapter extends ArrayAdapter<String> {
		private final Context context;

		public LeftNavListAdapter(Context context) {
			super(context, R.layout.list_item_account, new String[sites.size()]);
			this.context = context;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View rowView = convertView;
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			rowView = inflater.inflate(R.layout.list_item_account, parent,
					false);

			final TextView userfullnameView = (TextView) rowView
					.findViewById(R.id.nav_user_fullname);
			final TextView sitenameView = (TextView) rowView
					.findViewById(R.id.nav_sitename);
			final ImageView userimageView = (ImageView) rowView
					.findViewById(R.id.nav_user_image);

			userfullnameView.setText(sites.get(position).getFullname());
			sitenameView.setText(sites.get(position).getSitename());
			Bitmap userImage = ImageDecoder.decodeImage(new File(Environment
					.getExternalStorageDirectory()
					+ "/MDroid/."
					+ sites.get(position).getId()));
			if (userImage != null)
				userimageView.setImageBitmap(userImage);

			return rowView;
		}
	}

}
