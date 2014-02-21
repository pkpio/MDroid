package in.co.praveenkumar.mdroid;

import in.co.praveenkumar.R;
import in.co.praveenkumar.mdroid.helpers.BaseActivity;
import in.co.praveenkumar.mdroid.models.Mnotification;
import in.co.praveenkumar.mdroid.sqlite.databases.SqliteTbNotifications;

import java.util.ArrayList;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class NotificationsActivity extends BaseActivity {
	final String DEBUG_TAG = "MDroid Notification Activity";
	ArrayList<Mnotification> notifications = new ArrayList<Mnotification>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.notifications);

		// Test notifications
		// Mnotification notification1 = new Mnotification();
		// notification1.setCount(12);
		// notification1.setCourseName("Advanced computing for electrical ");
		// notification1.setType(0);
		//
		// Mnotification notification2 = new Mnotification();
		// notification2.setCount(8);
		// notification2.setCourseName("Advanced computing for electrical ");
		// notification2.setType(1);
		// notification2.setPostSubject("Sample post subject for testing");
		//
		// notifications.add(notification1);
		// notifications.add(notification2);

		SqliteTbNotifications stn = new SqliteTbNotifications(
				getApplicationContext());
		notifications = stn.getAllNotifications();

		// List them
		listNotificationsInListView();
	}

	private void listNotificationsInListView() {
		// Set title
		setTitle("Notifications (" + notifications.size() + ")");

		ListView listView = (ListView) findViewById(R.id.notifications_list);
		MySimpleArrayAdapter adapter = new MySimpleArrayAdapter(this,
				notifications);

		// Assign adapter to ListView
		listView.setAdapter(adapter);
	}

	private class MySimpleArrayAdapter extends ArrayAdapter<String> {
		private final Context context;
		private final ArrayList<Mnotification> notifications;

		public MySimpleArrayAdapter(Context context,
				ArrayList<Mnotification> notifications) {
			super(context, R.layout.course_listview_layout,
					new String[notifications.size()]);
			this.context = context;
			this.notifications = notifications;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View rowView = inflater.inflate(
					R.layout.notifications_list_view_layout, parent, false);
			// Because position can't be used for it is not final
			final int pos = position;

			// Get views for course name, details and count
			final TextView cNmeView = (TextView) rowView
					.findViewById(R.id.notification_course_name);
			final TextView detailsView = (TextView) rowView
					.findViewById(R.id.notification_post_subject);
			final TextView countView = (TextView) rowView
					.findViewById(R.id.notification_count);

			// Set course name, notification count and details
			cNmeView.setText(notifications.get(position).getCourseName());
			countView.setText(notifications.get(position).getCount() + "");

			// Check type and set count color
			// 0 - file; 1 - forum

			// File case
			if (notifications.get(position).getType() == 0) {
				countView.setBackgroundResource(R.drawable.circular_count_file);
				detailsView.setText("New files added");
			}

			// Forum case
			else {
				detailsView.setText("New replies: "
						+ notifications.get(position).getPostSubject());
				countView
						.setBackgroundResource(R.drawable.circular_count_forum);
			}

			// Set onClickListeners
			final LinearLayout notifiView = (LinearLayout) rowView
					.findViewById(R.id.notification_card);
			final Button clearBtn = (Button) rowView
					.findViewById(R.id.notification_clear_btn);

			notifiView.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					Log.d(DEBUG_TAG, "Clicked notification: " + pos);
				}
			});

			clearBtn.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					Log.d(DEBUG_TAG, "Clicked clear: " + pos);
				}
			});
			return rowView;
		}
	}
}
