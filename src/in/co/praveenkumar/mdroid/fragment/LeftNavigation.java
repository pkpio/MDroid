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
		private static final int TYPE_ACCOUNT = 0;
		private static final int TYPE_MENUITEM = 1;
		private static final int TYPE_COUNT = 2;

		private final Context context;

		public LeftNavListAdapter(Context context) {
			super(context, R.layout.list_item_account, new String[sites.size()
					+ menuItems.length]);
			this.context = context;
		}

		@Override
		public int getViewTypeCount() {
			return TYPE_COUNT;
		}

		@Override
		public int getItemViewType(int position) {
			return (position >= sites.size()) ? TYPE_MENUITEM : TYPE_ACCOUNT;
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
				case TYPE_ACCOUNT:
					convertView = inflater.inflate(R.layout.list_item_account,
							parent, false);

					viewHolder.userfullname = (TextView) convertView
							.findViewById(R.id.nav_user_fullname);
					viewHolder.sitename = (TextView) convertView
							.findViewById(R.id.nav_sitename);
					viewHolder.userimage = (ImageView) convertView
							.findViewById(R.id.nav_user_image);
					break;

				case TYPE_MENUITEM:
					convertView = inflater.inflate(R.layout.list_item_menu,
							parent, false);

					viewHolder.menuItemName = (TextView) convertView
							.findViewById(R.id.nav_menuitem);
					break;
				}

				// Save the holder with the view
				convertView.setTag(viewHolder);
			} else {
				// Just use the viewHolder and avoid findviewbyid()
				viewHolder = (ViewHolder) convertView.getTag();
			}

			// Assign values
			switch (type) {
			case TYPE_ACCOUNT:
				viewHolder.userfullname.setText(sites.get(position)
						.getFullname());
				viewHolder.sitename.setText(sites.get(position).getSitename());
				Bitmap userImage = ImageDecoder.decodeImage(new File(
						Environment.getExternalStorageDirectory() + "/MDroid/."
								+ sites.get(position).getId()));
				if (userImage != null)
					viewHolder.userimage.setImageBitmap(userImage);
				break;

			case TYPE_MENUITEM:
				viewHolder.menuItemName.setText(menuItems[position
						- sites.size()]);
				break;
			}

			return convertView;
		}
	}

	static class ViewHolder {
		TextView userfullname;
		TextView sitename;
		ImageView userimage;
		TextView menuItemName;
	}

}
