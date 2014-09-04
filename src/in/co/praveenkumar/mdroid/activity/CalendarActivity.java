package in.co.praveenkumar.mdroid.activity;

import in.co.praveenkumar.mdroid.adapter.NavigationDrawer;
import in.co.praveenkumar.mdroid.apis.R;
import in.co.praveenkumar.mdroid.helper.SessionSetting;
import in.co.praveenkumar.mdroid.helper.TimeFormat;
import in.co.praveenkumar.mdroid.moodlemodel.MoodleCourse;
import in.co.praveenkumar.mdroid.moodlemodel.MoodleEvent;
import in.co.praveenkumar.mdroid.task.EventSyncTask;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class CalendarActivity extends NavigationDrawer {
	CalendarListAdapter calendarListAdapter;
	SessionSetting session;
	List<MoodleEvent> mEvents;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_calendar);
		setUpDrawer();

		/**
		 * -TODO- Move db operation to bg thread
		 */
		session = new SessionSetting(this);
		mEvents = MoodleEvent.find(MoodleEvent.class, "siteid = ?",
				session.getCurrentSiteId() + "");

		ListView courseList = (ListView) findViewById(R.id.list_calendar);
		calendarListAdapter = new CalendarListAdapter(this);
		courseList.setAdapter(calendarListAdapter);

		new ListEventsThread(session.getmUrl(), session.getToken(),
				session.getCurrentSiteId()).execute("");
	}

	private class ListEventsThread extends AsyncTask<String, Integer, Boolean> {
		Long siteid;
		EventSyncTask est;
		Boolean syncStatus;

		public ListEventsThread(String mUrl, String token, Long siteid) {
			this.siteid = siteid;
			est = new EventSyncTask(mUrl, token, siteid);
		}

		@Override
		protected Boolean doInBackground(String... params) {
			// Get course ids
			List<MoodleCourse> mCourses = MoodleCourse.find(MoodleCourse.class,
					"siteid = ?", siteid + "");
			ArrayList<String> courseIds = new ArrayList<String>();
			for (int i = 0; i < mCourses.size(); i++)
				courseIds.add(mCourses.get(i).getCourseid() + "");
			syncStatus = est.syncEvents(courseIds);

			if (syncStatus) {
				mEvents = MoodleEvent.find(MoodleEvent.class, "siteid = ?",
						session.getCurrentSiteId() + "");
				return true;
			} else
				return false;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			calendarListAdapter.notifyDataSetChanged();
		}

	}

	public class CalendarListAdapter extends ArrayAdapter<String> {
		final Context context;

		public CalendarListAdapter(Context context) {
			super(context, R.layout.list_item_event, new String[mEvents.size()]);
			this.context = context;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder;
			if (convertView == null) {
				viewHolder = new ViewHolder();
				LayoutInflater inflater = (LayoutInflater) context
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(R.layout.list_item_event,
						parent, false);

				viewHolder.eventname = (TextView) convertView
						.findViewById(R.id.event_name);
				viewHolder.eventcourse = (TextView) convertView
						.findViewById(R.id.event_coursename);
				viewHolder.eventtime = (TextView) convertView
						.findViewById(R.id.event_time);
				viewHolder.eventdesc = (TextView) convertView
						.findViewById(R.id.event_desc);

				// Save the holder with the view
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}

			// Assign values
			viewHolder.eventname.setText(mEvents.get(position).getName());
			viewHolder.eventcourse.setText(mEvents.get(position)
					.getCoursename());
			viewHolder.eventtime.setText(TimeFormat.getMinimalTime(mEvents.get(
					position).getTimestart()));

			String description = mEvents.get(position).getDescription();
			if (description == null)
				description = "";
			else
				description = Html.fromHtml(description).toString().trim();
			viewHolder.eventdesc.setText(description);

			return convertView;
		}
	}

	static class ViewHolder {
		TextView eventname;
		TextView eventcourse;
		TextView eventtime;
		TextView eventdesc;
	}
}
