package in.co.praveenkumar.mdroid.fragment;

import in.co.praveenkumar.R;
import in.co.praveenkumar.mdroid.activity.CalendarActivity;
import in.co.praveenkumar.mdroid.activity.ContactActivity;
import in.co.praveenkumar.mdroid.activity.CourseContentActivity;
import in.co.praveenkumar.mdroid.activity.DiscussionActivity;
import in.co.praveenkumar.mdroid.activity.MessagingActivity;
import in.co.praveenkumar.mdroid.activity.PostActivity;
import in.co.praveenkumar.mdroid.helper.IconMap;
import in.co.praveenkumar.mdroid.helper.SessionSetting;
import in.co.praveenkumar.mdroid.helper.Workaround;
import in.co.praveenkumar.mdroid.model.MDroidNotification;
import in.co.praveenkumar.mdroid.service.MDroidService;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class NotificationFragment extends Fragment implements OnRefreshListener {
	final String DEBUG_TAG = "NotificationFragment";
	List<MDroidNotification> notifications;
	Context context;
	NotificationListAdapter adapter;
	SessionSetting session;
	LinearLayout chatEmptyLayout;
	SwipeRefreshLayout swipeLayout;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		context = getActivity();
		View rootView = inflater.inflate(R.layout.frag_notification, container,
				false);
		chatEmptyLayout = (LinearLayout) rootView
				.findViewById(R.id.chat_empty_layout);
		ListView notificationList = (ListView) rootView
				.findViewById(R.id.content_list);

		// Get sites info
		session = new SessionSetting(getActivity());
		notifications = MDroidNotification
				.find(MDroidNotification.class, "siteid = ? and read = ?",
						session.getCurrentSiteId() + "", "0");

		adapter = new NotificationListAdapter(getActivity());
		notificationList.setAdapter(adapter);

		// OnItem click
		notificationList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {

				MDroidNotification notification = notifications.get(position);
				int extras = notification.getExtras();
				Intent i = null;

				switch (notification.getType()) {

				case MDroidNotification.TYPE_CONTACT:
					i = new Intent(context, ContactActivity.class);
					break;

				case MDroidNotification.TYPE_COURSE_CONTENT:
					i = new Intent(context, CourseContentActivity.class);
					i.putExtra("courseid", extras);
					break;

				case MDroidNotification.TYPE_EVENT:
					i = new Intent(context, CalendarActivity.class);
					break;

				case MDroidNotification.TYPE_FORUM:
					i = new Intent(context, CourseContentActivity.class);
					i.putExtra("courseid", extras);
					break;

				case MDroidNotification.TYPE_FORUM_TOPIC:
					i = new Intent(context, DiscussionActivity.class);
					i.putExtra("forumid", extras);
					break;

				case MDroidNotification.TYPE_FORUM_REPLY:
					i = new Intent(context, PostActivity.class);
					i.putExtra("discussionid", extras);
					break;

				case MDroidNotification.TYPE_MESSAGE:
					i = new Intent(context, MessagingActivity.class);
					break;

				case MDroidNotification.TYPE_PARTICIPANT:
					i = new Intent(context, CourseContentActivity.class);
					i.putExtra("courseid", extras);
					break;

				default:
					Toast.makeText(context,
							"Unknown notification type! Please report!",
							Toast.LENGTH_LONG).show();
					break;
				}

				if (i != null) {
					notification.setRead(true);
					notification.save();
					context.startActivity(i);
				}

			}
		});

		swipeLayout = (SwipeRefreshLayout) rootView
				.findViewById(R.id.swipe_refresh);
		Workaround.linkSwipeRefreshAndListView(swipeLayout, notificationList);
		swipeLayout.setOnRefreshListener(this);

		return rootView;
	}

	@Override
	public void onResume() {
		super.onResume();

		// Recheck notifications list
		notifications = MDroidNotification
				.find(MDroidNotification.class, "siteid = ? and read = ?",
						session.getCurrentSiteId() + "", "0");
		adapter.notifyDataSetChanged();
	}

	public class NotificationListAdapter extends BaseAdapter {

		private final Context context;

		public NotificationListAdapter(Context context) {
			this.context = context;
			if (!notifications.isEmpty())
				chatEmptyLayout.setVisibility(LinearLayout.GONE);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder;

			if (convertView == null) {
				viewHolder = new ViewHolder();
				LayoutInflater inflater = (LayoutInflater) context
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

				convertView = inflater.inflate(R.layout.list_item_notification,
						parent, false);

				viewHolder.icon = (ImageView) convertView
						.findViewById(R.id.list_notification_icon);
				viewHolder.title = (TextView) convertView
						.findViewById(R.id.list_notification_title);
				viewHolder.content = (TextView) convertView
						.findViewById(R.id.list_notification_content);

				// Save the holder with the view
				convertView.setTag(viewHolder);
			} else {
				// Just use the viewHolder and avoid findviewbyid()
				viewHolder = (ViewHolder) convertView.getTag();
			}

			// Set texts
			viewHolder.icon.setImageResource(IconMap
					.notificationIcon(notifications.get(position).getType()));
			viewHolder.title.setText(notifications.get(position).getTitle());
			String content = notifications.get(position).getContent();
			if (content == null)
				content = "";
			else
				content = Html.fromHtml(content).toString().trim();
			viewHolder.content.setText(content);

			return convertView;
		}

		@Override
		public int getCount() {
			return notifications.size();
		}

		@Override
		public Object getItem(int position) {
			return notifications.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public void notifyDataSetChanged() {
			super.notifyDataSetChanged();
			if (!notifications.isEmpty())
				chatEmptyLayout.setVisibility(LinearLayout.GONE);
			else
				chatEmptyLayout.setVisibility(LinearLayout.VISIBLE);
		}
	}

	static class ViewHolder {
		ImageView icon;
		TextView title;
		TextView content;
	}

	@Override
	public void onRefresh() {
		SharedPreferences settings = PreferenceManager
				.getDefaultSharedPreferences(context);

		// Check if notifications are enabled
		if (!settings.getBoolean("notifications", false))
			Toast.makeText(context,
					"Please turn on notifications from settings",
					Toast.LENGTH_LONG).show();
		else {
			Toast.makeText(context, "Checking started. You will be notified",
					Toast.LENGTH_LONG).show();

			// Start service
			Intent i = new Intent(context, MDroidService.class);
			i.putExtra("forceCheck", true);
			i.putExtra("siteid", session.getCurrentSiteId());
			context.startService(i);
		}
		swipeLayout.setRefreshing(false);
	}

}
