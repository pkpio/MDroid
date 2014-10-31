package in.co.praveenkumar.mdroid.fragment;

import in.co.praveenkumar.R;
import in.co.praveenkumar.mdroid.activity.CalendarActivity;
import in.co.praveenkumar.mdroid.activity.ContactActivity;
import in.co.praveenkumar.mdroid.activity.CourseActivity;
import in.co.praveenkumar.mdroid.activity.DonationActivity;
import in.co.praveenkumar.mdroid.activity.ForumActivity;
import in.co.praveenkumar.mdroid.activity.LoginActivity;
import in.co.praveenkumar.mdroid.activity.SettingsActivity;
import in.co.praveenkumar.mdroid.activity.WorkInProgressActivity;
import in.co.praveenkumar.mdroid.helper.AppInterface.DrawerStateInterface;
import in.co.praveenkumar.mdroid.helper.ImageDecoder;
import in.co.praveenkumar.mdroid.helper.SessionSetting;
import in.co.praveenkumar.mdroid.moodlemodel.MoodleSiteInfo;

import java.io.File;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

@SuppressLint("InlinedApi")
public class LeftNavigationFragment extends Fragment {
	DrawerStateInterface drawerState;
	Context context;
	final String DEBUG_TAG = "Left Navigation Fragment";
	ListView navListView;
	List<MoodleSiteInfo> sites;
	SessionSetting session;

	String[] moodleMenuItems = new String[] { "Courses", "Contacts",
			"Calender", "Forums", "Notifications" };
	String[] appMenuItems = new String[] { "Request features", "Settings",
			"Add account" };

	int[] moodleMenuIcons = new int[] { R.drawable.icon_school_greyscale,
			R.drawable.icon_people_greyscale2, R.drawable.icon_today_greyscale,
			R.drawable.icon_forum_greyscale,
			R.drawable.icon_notifications_greyscale };
	int[] appMenuIcons = new int[] { R.drawable.icon_extension_greyscale,
			R.drawable.icon_settings_greyscale, R.drawable.icon_plus_greyscale };

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.frag_left_navigation,
				container, false);
		navListView = (ListView) rootView.findViewById(R.id.left_nav_list);
		this.context = getActivity();

		// Get sites info
		session = new SessionSetting(getActivity());
		Log.d(DEBUG_TAG, session.getCurrentSiteId() + "");
		sites = MoodleSiteInfo.listAll(MoodleSiteInfo.class);

		final LeftNavListAdapter adapter = new LeftNavListAdapter(getActivity());
		navListView.setAdapter(adapter);

		navListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				switch (adapter.getItemViewType(position)) {
				case LeftNavListAdapter.TYPE_ACCOUNT:
					session.setCurrentSiteId(sites.get(position).getId());
					Intent i = new Intent(context, CourseActivity.class);
					i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
							| Intent.FLAG_ACTIVITY_CLEAR_TASK);
					context.startActivity(i);
					break;
				case LeftNavListAdapter.TYPE_MOODLE_MENUITEM:
					switch (position - sites.size()) {
					case 0:
						context.startActivity(new Intent(context,
								CourseActivity.class));
						break;
					case 1:
						context.startActivity(new Intent(context,
								ContactActivity.class));
						break;
					case 2:
						context.startActivity(new Intent(context,
								CalendarActivity.class));
						break;
					case 3:
						context.startActivity(new Intent(context,
								ForumActivity.class));
						break;
					case 4:
						// ASSIGNMENTS HERE
						context.startActivity(new Intent(context,
								WorkInProgressActivity.class));
						break;
					case 5:
						// NOTES HERE
						context.startActivity(new Intent(context,
								WorkInProgressActivity.class));
						break;
					case 6:
						// NOTIFICATIONS HERE
						context.startActivity(new Intent(context,
								WorkInProgressActivity.class));
						break;
					}
					break;
				case LeftNavListAdapter.TYPE_APP_MENUITEM:
					switch (position - sites.size() - moodleMenuItems.length) {
					case 0:
						context.startActivity(new Intent(context,
								DonationActivity.class));
						break;
					case 1:
						context.startActivity(new Intent(context,
								SettingsActivity.class));
						break;
					case 2:
						Intent j = new Intent(context, LoginActivity.class);
						j.putExtra("explicitCall", true);
						context.startActivity(j);
						break;
					}
					break;
				}
				drawerState.setDrawerState(false);
			}
		});

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

	public class LeftNavListAdapter extends BaseAdapter {
		private static final int TYPE_ACCOUNT = 0;
		private static final int TYPE_MOODLE_MENUITEM = 1;
		private static final int TYPE_APP_MENUITEM = 2;
		private static final int TYPE_COUNT = 3;

		private final Context context;

		public LeftNavListAdapter(Context context) {
			this.context = context;
		}

		@Override
		public int getViewTypeCount() {
			return TYPE_COUNT;
		}

		@Override
		public int getItemViewType(int position) {
			if (position >= sites.size() + moodleMenuItems.length)
				return TYPE_APP_MENUITEM;
			if (position >= sites.size())
				return TYPE_MOODLE_MENUITEM;
			return TYPE_ACCOUNT;

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
					viewHolder.userselected = (ImageView) convertView
							.findViewById(R.id.nav_user_selected);
					break;

				case TYPE_MOODLE_MENUITEM:
					convertView = inflater.inflate(
							R.layout.list_item_moodle_menu, parent, false);

					viewHolder.menuItemName = (TextView) convertView
							.findViewById(R.id.nav_menuitem);
					viewHolder.menuItemIcon = (ImageView) convertView
							.findViewById(R.id.nav_menuicon);
					break;
				case TYPE_APP_MENUITEM:
					convertView = inflater.inflate(R.layout.list_item_app_menu,
							parent, false);

					viewHolder.menuItemName = (TextView) convertView
							.findViewById(R.id.nav_menuitem);
					viewHolder.menuItemIcon = (ImageView) convertView
							.findViewById(R.id.nav_menuicon);
				}
				convertView.setTag(viewHolder);
			} else {
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

				// Show this as current account if it is
				if (session.getCurrentSiteId() == sites.get(position).getId())
					viewHolder.userselected.setVisibility(ImageView.VISIBLE);
				else
					viewHolder.userselected.setVisibility(ImageView.GONE);
				break;

			case TYPE_MOODLE_MENUITEM:
				viewHolder.menuItemName.setText(moodleMenuItems[position
						- sites.size()]);
				viewHolder.menuItemIcon
						.setImageResource(moodleMenuIcons[position
								- sites.size()]);
				break;
			case TYPE_APP_MENUITEM:
				viewHolder.menuItemName.setText(appMenuItems[position
						- sites.size() - moodleMenuItems.length]);
				viewHolder.menuItemIcon.setImageResource(appMenuIcons[position
						- sites.size() - moodleMenuItems.length]);
				break;
			}
			return convertView;
		}

		@Override
		public int getCount() {
			return sites.size() + moodleMenuItems.length + appMenuItems.length;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}
	}

	static class ViewHolder {
		TextView userfullname;
		TextView sitename;
		ImageView userselected;
		ImageView userimage;
		TextView menuItemName;
		ImageView menuItemIcon;
	}

}
