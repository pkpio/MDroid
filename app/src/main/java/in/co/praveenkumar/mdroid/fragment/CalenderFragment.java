package in.co.praveenkumar.mdroid.fragment;

import in.co.praveenkumar.R;
import in.co.praveenkumar.mdroid.helper.SessionSetting;
import in.co.praveenkumar.mdroid.helper.TimeFormat;
import in.co.praveenkumar.mdroid.helper.Workaround;
import in.co.praveenkumar.mdroid.model.MoodleCourse;
import in.co.praveenkumar.mdroid.model.MoodleEvent;
import in.co.praveenkumar.mdroid.task.EventSyncTask;
import in.co.praveenkumar.mdroid.view.StickyListView;
import in.co.praveenkumar.mdroid.view.StickyListView.PinnedSectionListAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class CalenderFragment extends Fragment implements OnRefreshListener {
	Context context;
	int courseid = 0;
	CalendarListAdapter calendarListAdapter;
	SessionSetting session;
	List<MoodleEvent> mEvents;
	ArrayList<CalenderObject> listObjects = new ArrayList<CalenderObject>();
	LinearLayout calenderEmptyLayout;
	SwipeRefreshLayout swipeLayout;

	/**
	 * Don't use this constructor
	 */
	public CalenderFragment() {
	}

	/**
	 * If you want to list all events, use courseid = 0
	 * 
	 * @param courseid
	 */
	public CalenderFragment(int courseid) {
		this.courseid = courseid;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.context = getActivity();
		View rootView = inflater.inflate(R.layout.frag_calender, container,
				false);
		calenderEmptyLayout = (LinearLayout) rootView
				.findViewById(R.id.calender_empty_layout);
		ListView eventList = (ListView) rootView
				.findViewById(R.id.list_calendar);

		session = new SessionSetting(context);
		if (courseid == 0)
			mEvents = MoodleEvent.find(MoodleEvent.class, "siteid = ?",
					session.getCurrentSiteId() + "");
		else
			mEvents = MoodleEvent.find(MoodleEvent.class,
					"siteid = ? and courseid = ?", session.getCurrentSiteId()
							+ "", courseid + "");
		setupCalenderObjects();

		calendarListAdapter = new CalendarListAdapter(context);
		((StickyListView) eventList).setShadowVisible(false);
		eventList.setAdapter(calendarListAdapter);

		swipeLayout = (SwipeRefreshLayout) rootView
				.findViewById(R.id.swipe_refresh);
		Workaround.linkSwipeRefreshAndListView(swipeLayout, eventList);
		swipeLayout.setOnRefreshListener(this);

		new ListEventsThread(session.getmUrl(), session.getToken(),
				session.getCurrentSiteId()).execute("");

		return rootView;
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
		protected void onPreExecute() {
			swipeLayout.setRefreshing(true);
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
				if (courseid == 0)
					mEvents = MoodleEvent.find(MoodleEvent.class, "siteid = ?",
							session.getCurrentSiteId() + "");
				else
					mEvents = MoodleEvent.find(MoodleEvent.class,
							"siteid = ? and courseid = ?",
							session.getCurrentSiteId() + "", courseid + "");
				setupCalenderObjects();
				return true;
			} else
				return false;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			calendarListAdapter.notifyDataSetChanged();
			if (listObjects.size() != 0)
				calenderEmptyLayout.setVisibility(LinearLayout.GONE);
			swipeLayout.setRefreshing(false);
		}

	}

	public class CalendarListAdapter extends BaseAdapter implements
			PinnedSectionListAdapter {
		final Context context;
		static final int TYPE_EVENT = 0;
		static final int TYPE_DATE = 1;
		static final int TYPE_COUNT = 2;

		public CalendarListAdapter(Context context) {
			this.context = context;
			if (listObjects.size() != 0)
				calenderEmptyLayout.setVisibility(LinearLayout.GONE);
		}

		@Override
		public int getViewTypeCount() {
			return TYPE_COUNT;
		}

		@Override
		public int getItemViewType(int position) {
			return listObjects.get(position).viewType;
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
				case TYPE_EVENT:
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
					break;
				case TYPE_DATE:
					convertView = inflater.inflate(
							R.layout.list_item_calender_day, parent, false);

					viewHolder.title = (TextView) convertView
							.findViewById(R.id.list_calender_time);
					break;
				}

				// Save the holder with the view
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}

			// Assign values
			switch (type) {
			case TYPE_EVENT:
				viewHolder.eventname.setText(listObjects.get(position).event
						.getName());
				viewHolder.eventcourse.setText(listObjects.get(position).event
						.getCoursename());
				viewHolder.eventtime.setText(TimeFormat.getNiceTime(listObjects
						.get(position).event.getTimestart()));

				String description = listObjects.get(position).event
						.getDescription();
				if (description == null)
					description = "";
				else
					description = Html.fromHtml(description).toString().trim();
				viewHolder.eventdesc.setText(description);
				break;
			case TYPE_DATE:
				viewHolder.title.setText(listObjects.get(position).title);
				break;
			}

			return convertView;
		}

		@Override
		public boolean isItemViewTypePinned(int viewType) {
			if (viewType == TYPE_DATE)
				return true;
			else
				return false;
		}

		@Override
		public int getCount() {
			return listObjects.size();
		}

		@Override
		public Object getItem(int position) {
			return listObjects.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}
	}

	static class ViewHolder {
		TextView eventname;
		TextView eventcourse;
		TextView eventtime;
		TextView eventdesc;
		TextView title;
	}

	private void setupCalenderObjects() {
		if (mEvents == null)
			return;
		if (mEvents.size() == 0)
			return;

		Collections.sort(mEvents, new Comparator<MoodleEvent>() {
			public int compare(MoodleEvent o1, MoodleEvent o2) {
				if (o1.getTimestart() == o2.getTimestart())
					return 0;
				return o1.getTimestart() < o2.getTimestart() ? -1 : 1;
			}
		});

		// To avoid duplicates in listing
		listObjects.clear();

		// Build titles + events objects for pinned listview
		String titlePrev = TimeFormat.getSectionTitle(mEvents.get(0)
				.getTimestart());
		String titleNow = "";
		listObjects.add(new CalenderObject(null, CalendarListAdapter.TYPE_DATE,
				titlePrev));
		listObjects.add(new CalenderObject(mEvents.get(0),
				CalendarListAdapter.TYPE_EVENT, titlePrev));
		for (int i = 1; i < mEvents.size(); i++) {
			titleNow = TimeFormat
					.getSectionTitle(mEvents.get(i).getTimestart());
			if (!titleNow.contentEquals(titlePrev))
				listObjects.add(new CalenderObject(null,
						CalendarListAdapter.TYPE_DATE, titleNow));
			listObjects.add(new CalenderObject(mEvents.get(i),
					CalendarListAdapter.TYPE_EVENT, titleNow));
			titlePrev = titleNow;
		}
	}

	/**
	 * For simplified pinned listview usage
	 * 
	 * @author Praveen Kumar Pendyala (praveen@praveenkumar.co.in)
	 * 
	 */
	class CalenderObject {
		MoodleEvent event;
		int viewType;
		String title;

		public CalenderObject() {
		}

		public CalenderObject(MoodleEvent event, int viewType, String title) {
			this.event = event;
			this.viewType = viewType;
			this.title = title;
		}
	}

	@Override
	public void onRefresh() {
		new ListEventsThread(session.getmUrl(), session.getToken(),
				session.getCurrentSiteId()).execute("");
	}

}
