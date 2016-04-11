package in.co.praveenkumar.mdroid.fragment;

import in.co.praveenkumar.R;
import in.co.praveenkumar.mdroid.activity.AppBrowserActivity;
import in.co.praveenkumar.mdroid.helper.FileOpener;
import in.co.praveenkumar.mdroid.helper.IconMap;
import in.co.praveenkumar.mdroid.helper.SessionSetting;
import in.co.praveenkumar.mdroid.helper.Workaround;
import in.co.praveenkumar.mdroid.model.MoodleModule;
import in.co.praveenkumar.mdroid.model.MoodleModuleContent;
import in.co.praveenkumar.mdroid.model.MoodleSection;
import in.co.praveenkumar.mdroid.task.CourseContentSyncTask;
import in.co.praveenkumar.mdroid.task.DownloadTask;
import in.co.praveenkumar.mdroid.view.StickyListView;
import in.co.praveenkumar.mdroid.view.StickyListView.PinnedSectionListAdapter;

import java.io.File;
import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class ContentFragment extends Fragment implements OnRefreshListener {
	Context context;
	int courseid;
	CourseListAdapter courseContentListAdapter;
	SessionSetting session;
	ArrayList<CourseContentObject> listObjects = new ArrayList<>();
	LinearLayout contentEmptyLayout;
	SwipeRefreshLayout swipeLayout;

	/**
	 * This constructor is required to prevent exceptions on app usage. Don't
	 * use this constructor.
	 */
	public ContentFragment() {
	}

	/**
	 *
	 * @param courseid
	 *            Choose which course contents to be listed
	 */
	public void setCourseid(int courseid) {
		this.courseid = courseid;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.frag_content, container,
				false);
		contentEmptyLayout = (LinearLayout) rootView
				.findViewById(R.id.content_empty_layout);
		this.context = getActivity();
		session = new SessionSetting(context);

		CourseContentSyncTask ccs = new CourseContentSyncTask(
				session.getmUrl(), session.getToken(),
				session.getCurrentSiteId());
		ArrayList<MoodleSection> sections = ccs.getCourseContents(courseid);
		mapSectionsToListObjects(sections);

		ListView contentList = (ListView) rootView
				.findViewById(R.id.list_course_content);
		courseContentListAdapter = new CourseListAdapter(context);
		((StickyListView) contentList).setShadowVisible(false);
		contentList.setAdapter(courseContentListAdapter);

		swipeLayout = (SwipeRefreshLayout) rootView
				.findViewById(R.id.swipe_refresh);
		Workaround.linkSwipeRefreshAndListView(swipeLayout, contentList);
		swipeLayout.setOnRefreshListener(this);

		new listCoursesThread(session.getmUrl(), session.getToken(), courseid,
				session.getCurrentSiteId()).execute("");

		return rootView;
	}

	private class listCoursesThread extends AsyncTask<String, Integer, Boolean> {
		CourseContentSyncTask ccs;
		int courseid;
		Boolean syncStatus;

		public listCoursesThread(String mUrl, String token, int courseid,
				Long siteid) {
			ccs = new CourseContentSyncTask(mUrl, token, siteid);
			this.courseid = courseid;
		}

		@Override
		protected void onPreExecute() {
			swipeLayout.setRefreshing(true);
		}

		@Override
		protected Boolean doInBackground(String... params) {
			System.out.println("Background execute");
			syncStatus = ccs.syncCourseContents(courseid);
			ArrayList<MoodleSection> sections = ccs.getCourseContents(courseid);

			// Save all sections into a listObject array for easy access inside
			mapSectionsToListObjects(sections);

			return syncStatus;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			courseContentListAdapter.notifyDataSetChanged();
			if (!listObjects.isEmpty())
				contentEmptyLayout.setVisibility(LinearLayout.GONE);
			swipeLayout.setRefreshing(false);
		}

	}

	public class CourseListAdapter extends BaseAdapter implements
			PinnedSectionListAdapter {
		static final int TYPE_MODULE = 0;
		static final int TYPE_HEADER = 1;
		static final int TYPE_COUNT = 2;
		final Context context;

		public CourseListAdapter(Context context) {
			this.context = context;
			if (!listObjects.isEmpty())
				contentEmptyLayout.setVisibility(LinearLayout.GONE);
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
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			ViewHolder viewHolder;
			int type = getItemViewType(position);

			if (convertView == null) {
				viewHolder = new ViewHolder();
				LayoutInflater inflater = (LayoutInflater) context
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

				// Choose layout
				switch (type) {
				case TYPE_HEADER:
					convertView = inflater.inflate(R.layout.list_item_section,
							parent, false);

					viewHolder.sectionname = (TextView) convertView
							.findViewById(R.id.list_sectionname);
					break;

				case TYPE_MODULE:
					convertView = inflater.inflate(R.layout.list_item_module,
							parent, false);

					viewHolder.modulename = (TextView) convertView
							.findViewById(R.id.list_modulename);
					viewHolder.moduledesc = (TextView) convertView
							.findViewById(R.id.list_moduledescription);
					viewHolder.moduleicon = (ImageView) convertView
							.findViewById(R.id.list_moduleicon);
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
			case TYPE_HEADER:
				String sectionname = listObjects.get(position).sectionname;
				if (sectionname == null)
					sectionname = "";
				else
					sectionname = Html.fromHtml(sectionname).toString().trim();
				viewHolder.sectionname.setText(sectionname);
				break;

			case TYPE_MODULE:
				MoodleModule module = listObjects.get(position).module;

				// Module name
				String modulename = module.getName();
				if (modulename == null)
					modulename = "";
				else
					modulename = Html.fromHtml(modulename).toString().trim();
				viewHolder.modulename.setText(modulename);

				// Module icon
				viewHolder.moduleicon.setImageResource(IconMap.moduleIcon(module));

				// Module description
				String description = module.getDescription();
				if (description == null)
					description = "";
				else
					description = Html.fromHtml(description).toString().trim();
				viewHolder.moduledesc.setText(description);

				break;
			}
			convertView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					MoodleModule module = listObjects.get(position).module;
					if (module == null)
						return;
					Intent i = new Intent(context, AppBrowserActivity.class);

					String modurl = module.getUrl();
					String courseurl = session.getmUrl()
							+ "/course/view.php?id=" + courseid;
					modurl = (modurl == null) ? courseurl : modurl;
					i.putExtra("url", modurl);

					if (!module.getModname().contentEquals("resource")) {
						context.startActivity(i);
						return;
					}

					if (module.getContents() == null) {
						context.startActivity(i);
						return;
					}

					if (module.getContents().isEmpty()) {
						context.startActivity(i);
						return;
					}

					MoodleModuleContent content = module.getContents().get(0);
					String path = "/s" + session.getCurrentSiteId() + "c"
							+ courseid + "/";
					File file = new File(Environment
							.getExternalStoragePublicDirectory("/MDroid")
							+ path + content.getFilename());

					// Download if file doesn't already exist
					if (!file.exists()) {
						String fileurl = content.getFileurl();
						fileurl += "&token=" + session.getToken();
						DownloadTask dt = new DownloadTask(context);
						dt.download(fileurl, path, content.getFilename(), true,
								DownloadTask.SYSTEM_DOWNLOADER);
					} else {
						FileOpener.open(context, file);
					}

				}
			});

			return convertView;
		}

		@Override
		public boolean isItemViewTypePinned(int viewType) {
			return viewType == TYPE_HEADER;
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
		TextView sectionname;
		TextView modulename;
		ImageView moduleicon;
		TextView moduledesc;
	}

	/**
	 * This is used to hold all data of any list item in the coursecontent
	 * listview. A list can be a Module or a section header. This view will be
	 * to get appropriate view from position for our stickylistview which has
	 * multiple view types
	 * 
	 * @author praveen
	 * 
	 */
	class CourseContentObject {
		/**
		 * Follows type values as defined in CourseListAdapter. Check
		 * CourseListAdapter.TYPE_ for available values
		 */
		int viewType;
		/**
		 * Name of the section to which this object belongs
		 */
		String sectionname;
		/**
		 * Id of the section to which this object belongs
		 */
		int sectionid;
		/**
		 * Module object. (Only for viewType = TYPE_MODULE)
		 */
		MoodleModule module;

	}

	private void mapSectionsToListObjects(ArrayList<MoodleSection> sections) {
		if (sections == null)
			return;

		// To avoid duplicates in listing
		listObjects.clear();

		MoodleSection section;
		ArrayList<MoodleModule> modules;
		for (int i = 0; i < sections.size(); i++) {
			section = sections.get(i);
			modules = section.getModules();
			if (!modules.isEmpty()) {
				CourseContentObject object = new CourseContentObject();
				object.viewType = CourseListAdapter.TYPE_HEADER;
				object.sectionid = section.getSectionid();
				object.sectionname = section.getName();
				listObjects.add(object);

				// Add modules
				for (int j = 0; j < modules.size(); j++) {
					CourseContentObject mObject = new CourseContentObject();
					mObject.viewType = CourseListAdapter.TYPE_MODULE;
					mObject.sectionid = section.getSectionid();
					mObject.sectionname = section.getName();
					mObject.module = modules.get(j);
					listObjects.add(mObject);
				}
			}
		}
	}

	@Override
	public void onRefresh() {
		new listCoursesThread(session.getmUrl(), session.getToken(), courseid,
				session.getCurrentSiteId()).execute("");
	}

}
