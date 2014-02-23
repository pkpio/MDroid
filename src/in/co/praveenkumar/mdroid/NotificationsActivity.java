/*
 * Author: 	Praveen Kumar Pendyala
 * Project: MDroid
 * Created:	15-02-2014
 * 
 * © 2013-2014 Praveen Kumar Pendyala. 
 * Licensed under the Creative Commons Attribution-NonCommercial-ShareAlike 
 * 3.0 Unported license, http://creativecommons.org/licenses/by-nc-sa/3.0/ 
 * 
 * This is a part of the MDroid project. You may use the contents of this file
 * or the project only if you comply with license of the project, available at the 
 * Github repo: https://github.com/praveendath92/MDroid/blob/master/README.md
 * 
 */

package in.co.praveenkumar.mdroid;

import in.co.praveenkumar.R;
import in.co.praveenkumar.mdroid.helpers.BaseActivity;
import in.co.praveenkumar.mdroid.helpers.Database;
import in.co.praveenkumar.mdroid.models.Mnotification;
import in.co.praveenkumar.mdroid.networking.DoLogin;
import in.co.praveenkumar.mdroid.sqlite.databases.SqliteTbNotifications;

import java.util.ArrayList;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class NotificationsActivity extends BaseActivity {
	final String DEBUG_TAG = "MDroid Notification Activity";

	ArrayList<Mnotification> notifications = new ArrayList<Mnotification>();
	int unReadCount = 0;
	int openNotifPos = 0;

	ProgressDialog loginDialog;
	DoLogin l = new DoLogin();
	SqliteTbNotifications stn;

	MySimpleArrayAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.notifications);

		stn = new SqliteTbNotifications(getApplicationContext());

		// Get all unread notifications first
		notifications = stn.getAllUnreadNotifications();
		unReadCount = notifications.size();

		// Get the rest of the read notifications
		notifications.addAll(stn.getAllReadNotifications());

		// List them
		listNotificationsInListView();
	}

	private void listNotificationsInListView() {
		// Set title
		setTitle("Notifications (" + unReadCount + ")");

		ListView listView = (ListView) findViewById(R.id.notifications_list);
		adapter = new MySimpleArrayAdapter(this, notifications);

		// Assign adapter to ListView
		listView.setAdapter(adapter);
	}

	private class MySimpleArrayAdapter extends ArrayAdapter<String> {
		private final Context context;
		private final ArrayList<Mnotification> notifications;

		public MySimpleArrayAdapter(Context context,
				ArrayList<Mnotification> notifications) {
			super(context, R.layout.course_listview_layout,
					new String[notifications.size() + 1]);
			this.context = context;
			this.notifications = notifications;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View rowView = inflater.inflate(
					R.layout.notifications_list_view_layout, parent, false);

			// Because position 0 will be used for minion message
			final int pos = position - 1;

			// Display a minion message if necessary
			if (position == 0) {
				final LinearLayout notifiContent = (LinearLayout) rowView
						.findViewById(R.id.notification_content);
				final LinearLayout noUnreadLayout = (LinearLayout) rowView
						.findViewById(R.id.notifications_no_unread_message_layout);
				if (unReadCount == 0) {
					notifiContent.setVisibility(LinearLayout.GONE);
					noUnreadLayout.setVisibility(LinearLayout.VISIBLE);
				} else {
					notifiContent.setVisibility(LinearLayout.GONE);
					noUnreadLayout.setVisibility(LinearLayout.GONE);
				}
			}

			// Normal notifications from here.
			else {

				// Get views for course name, details and count
				final TextView cNmeView = (TextView) rowView
						.findViewById(R.id.notification_course_name);
				final TextView detailsView = (TextView) rowView
						.findViewById(R.id.notification_post_subject);
				final TextView countView = (TextView) rowView
						.findViewById(R.id.notification_count);

				// Set course name, notification count and details
				cNmeView.setText(notifications.get(pos).getCourseName());
				countView.setText(notifications.get(pos).getCount() + "");

				// Check type and set count color
				// 0 - file; 1 - forum

				// File case
				if (notifications.get(pos).getType() == 0) {
					countView
							.setBackgroundResource(R.drawable.circular_count_file);
					detailsView.setText("New files added");
				}

				// Forum case
				else {
					// If count = 0. It means it is a new thread.
					if (notifications.get(pos).getCount() == 0) {
						detailsView.setText("New topic: "
								+ notifications.get(pos).getPostSubject());
						countView
								.setBackgroundResource(R.drawable.circular_count_topic);
						countView.setText("new");
					} else {
						detailsView.setText("New replies: "
								+ notifications.get(pos).getPostSubject());
						countView
								.setBackgroundResource(R.drawable.circular_count_forum);
					}
				}

				final LinearLayout notifiView = (LinearLayout) rowView
						.findViewById(R.id.notification_card);

				// Card background change based on read or not
				if (notifications.get(pos).getRead() == 0)
					notifiView.setBackgroundResource(R.drawable.clickable_card);
				else
					notifiView
							.setBackgroundResource(R.drawable.clickable_grey_card);

				// Set onClickListeners
				notifiView.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						openNotifPos = pos;
						goToContentPage();
					}
				});
			}

			return rowView;
		}
	}

	private void goToContentPage() {
		// Check if logged in else log in
		if (!l.isLoggedIn()) {
			loginDialog = new ProgressDialog(this);
			loginDialog.setMessage("Doing one time log in..");
			loginDialog.setIndeterminate(true);
			loginDialog.setCancelable(false);

			Database db = new Database(getApplicationContext());
			new tryAsyncLogin().execute(db.getLDAP(), db.getPswd());
		}
		// Logged in. Safe to openActivity
		else {
			openActivity();
		}
	}

	private class tryAsyncLogin extends AsyncTask<String, Integer, Long> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			loginDialog.show();
		}

		protected Long doInBackground(String... credentials) {
			l.doLogin(credentials[0], credentials[1]);
			return null;
		}

		protected void onPostExecute(Long result) {
			try {
				loginDialog.dismiss();
			} catch (IllegalArgumentException e) {

			}

			if (l.isLoggedIn()) {
				openActivity();
			} else {
				Log.d(DEBUG_TAG, "Error logging in");
			}
		}
	}

	private void openActivity() {
		Mnotification notification = notifications.get(openNotifPos);

		// Mark it as read - update UI
		stn.markNotificationAsRead(notification.getId());
		notifications.get(openNotifPos).setRead(1);
		adapter.notifyDataSetChanged();

		// File notification
		if (notification.getType() == 0) {
			Intent i = new Intent(this, FilesActivity.class);
			i.putExtra("cId", notification.getCourseId());
			i.putExtra("cName", notification.getCourseName());
			startActivityForResult(i, 3);
		}

		// Forum notification
		else {
			Intent i = new Intent(this, ForumThreadActivity.class);
			i.putExtra("threadId", notification.getPostId() + "&mode=1");
			i.putExtra("threadSub", notification.getPostSubject());
			i.putExtra("cName", notification.getCourseName());
			startActivityForResult(i, 4);
		}
	}
}
