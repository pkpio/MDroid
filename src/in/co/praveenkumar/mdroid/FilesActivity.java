/*
 * Author: 	Praveen Kumar Pendyala
 * Project: MDroid
 * Created:	28-12-2013
 * 
 * © 2013, Praveen Kumar Pendyala. 
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
import in.co.praveenkumar.mdroid.helpers.BaseFragmentActivity;
import in.co.praveenkumar.mdroid.helpers.FileOpen;
import in.co.praveenkumar.mdroid.helpers.FolderDetails;
import in.co.praveenkumar.mdroid.models.Course;
import in.co.praveenkumar.mdroid.models.ForumThread;
import in.co.praveenkumar.mdroid.models.Mfile;
import in.co.praveenkumar.mdroid.networking.FetchForumFiles;
import in.co.praveenkumar.mdroid.networking.FetchResourceFiles;
import in.co.praveenkumar.mdroid.networking.FileDownloader;
import in.co.praveenkumar.mdroid.sqlite.databases.SqliteTbCourses;
import in.co.praveenkumar.mdroid.sqlite.databases.SqliteTbForums;

import java.io.File;
import java.util.ArrayList;
import java.util.Locale;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class FilesActivity extends BaseFragmentActivity {
	Course course = new Course();
	ArrayList<Mfile> rFiles = new ArrayList<Mfile>();
	ArrayList<Mfile> fFiles = new ArrayList<Mfile>();

	// Databases
	SqliteTbCourses stc = new SqliteTbCourses(this);
	SqliteTbForums stf = new SqliteTbForums(this);

	static LinearLayout rLoadingMsgLL;
	static ProgressBar rProgBar;
	static TextView rProgMsgTV;
	static LinearLayout fLoadingMsgLL;
	static ProgressBar fProgBar;
	static TextView fProgMsgTV;

	FetchResourceFiles FRF = new FetchResourceFiles();
	FetchForumFiles FFF; // This requires params to instantiate
	AsyncFilesFetch AFF = new AsyncFilesFetch();

	UIupdater UU;

	MySimpleArrayAdapter rListAdapter;
	MySimpleArrayAdapter fListAdapter;

	// For swipe view classes
	public static View[] sectionRootView = new View[2];
	SectionsPagerAdapter mSectionsPagerAdapter;
	ViewPager mViewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.files);

		// Get cName and cId sent from prev. activity
		Bundle extras = getIntent().getExtras();
		if (extras == null) {
			return;
		}
		course.setId(extras.getString("cId"));
		course.setName(extras.getString("cName"));

		// Set course name and fav status
		TextView cNameTV = (TextView) findViewById(R.id.files_course_name);
		cNameTV.setText(course.getName());

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		// Fetch files
		UU = new UIupdater(); // Async file load progress
		AFF.execute();
	}

	// Page swiping view classes
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			Fragment fragment = new DummySectionFragment();
			Bundle args = new Bundle();
			args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position + 1);
			fragment.setArguments(args);
			return fragment;
		}

		@Override
		public int getCount() {
			// Show 2 total pages.
			return 2;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.files_title_resources).toUpperCase(l);
			case 1:
				return getString(R.string.files_title_forums).toUpperCase(l);
			}
			return null;
		}
	}

	public static class DummySectionFragment extends Fragment {
		private final String DEBUG_TAG = "FilesActivity.DummySectionFragment";
		public static final String ARG_SECTION_NUMBER = "section_number";

		public DummySectionFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			int sectionNum = getArguments().getInt(ARG_SECTION_NUMBER);
			View rootView = inflater.inflate(R.layout.files_fragment_host,
					container, false);
			switch (sectionNum) {
			case 1:
				Log.d(DEBUG_TAG, "Creating section 1");
				sectionRootView[0] = rootView;
				rLoadingMsgLL = (LinearLayout) rootView
						.findViewById(R.id.files_loading_message);
				rProgBar = (ProgressBar) rootView
						.findViewById(R.id.files_progress_bar);
				rProgMsgTV = (TextView) rootView
						.findViewById(R.id.files_progress_msg);
				break;
			case 2:
				Log.d(DEBUG_TAG, "Creating section 2");
				sectionRootView[1] = rootView;
				fLoadingMsgLL = (LinearLayout) sectionRootView[1]
						.findViewById(R.id.files_loading_message);
				fProgBar = (ProgressBar) sectionRootView[1]
						.findViewById(R.id.files_progress_bar);
				fProgMsgTV = (TextView) sectionRootView[1]
						.findViewById(R.id.files_progress_msg);
				break;
			}
			return rootView;
		}
	}

	public class UIupdater {
		private int pos;
		private int sec;

		public UIupdater(int pos, int sec) {
			this.pos = pos;
			this.sec = sec;
		}

		public UIupdater() {
		}

		public void setPosSec(int pos, int sec) {
			this.pos = pos;
			this.sec = sec;
		}

		public void setFileSize(String size) {
			if (sec == 0) {
				rFiles.get(pos).setSize(size);
				rListAdapter.notifyDataSetChanged();
			}

			if (sec == 1) {
				fFiles.get(pos).setSize(size);
				fListAdapter.notifyDataSetChanged();
			}
		}

		public void setFileDate(String date) {
			if (sec == 0) {
				rFiles.get(pos).setDate(date);
				rListAdapter.notifyDataSetChanged();
			}

			if (sec == 1) {
				fFiles.get(pos).setDate(date);
				fListAdapter.notifyDataSetChanged();
			}
		}

		public void setFileProg(int prog) {
			if (sec == 0) {
				rFiles.get(pos).setProgress(prog);
				rListAdapter.notifyDataSetChanged();
			}

			if (sec == 1) {
				fFiles.get(pos).setProgress(prog);
				fListAdapter.notifyDataSetChanged();
			}
		}

		public void setFileId(String fId) {
			if (sec == 0) {
				rFiles.get(pos).setURL(fId);
				rListAdapter.notifyDataSetChanged();
			}

			if (sec == 1) {
				fFiles.get(pos).setURL(fId);
				fListAdapter.notifyDataSetChanged();
			}
		}

		public void setForumProgress(int curFThread, int totalFThreads,
				ArrayList<Mfile> files) {
			AFF.doProgress(curFThread, totalFThreads);
			fFiles = files;
		}
	}

	// Async files fetch thread
	private class AsyncFilesFetch extends AsyncTask<String, Integer, Long> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			if (rLoadingMsgLL != null)
				rLoadingMsgLL.setVisibility(LinearLayout.VISIBLE);
			if (fLoadingMsgLL != null)
				fLoadingMsgLL.setVisibility(LinearLayout.VISIBLE);
		}

		protected Long doInBackground(String... values) {
			FRF.fetchFiles(course.getId());
			publishProgress(0);
			FFF = new FetchForumFiles(UU);
			FFF.fetchFiles(course.getId());
			return null;
		}

		public void doProgress(int curFThread, int totalFthreads) {
			publishProgress(curFThread, totalFthreads);
		}

		protected void onProgressUpdate(Integer... progress) {
			if (progress[0] == 0) {
				rLoadingMsgLL.setVisibility(LinearLayout.GONE);
				rFiles = FRF.getFiles();
				listFilesInListView(0);
			}
			// Non zero values for forum loading
			else {
				fProgMsgTV.setText("Fetching files from thread: " + progress[0]
						+ "/" + progress[1]);
				if (!fFiles.isEmpty())
					listFilesInListView(1);
			}
		}

		protected void onPostExecute(Long result) {
			fLoadingMsgLL.setVisibility(LinearLayout.GONE);
			fFiles = FFF.getFiles();
			listFilesInListView(1);

			// Also get the threads info for database indexing
			ArrayList<ForumThread> threads = FFF.getThreads();

			// Update counts in database for services
			stc.updateFileCount(course.getId(), rFiles.size() + fFiles.size());
			stc.updateForumCount(course.getId(), threads.size());
			stf.addThreads(course.getId(), threads);
		}
	}

	private void listFilesInListView(int section) {
		if (section == 0) {
			if (!rFiles.isEmpty()) {
				// Set title
				setTitle("Files (" + rFiles.size() + ")");

				// Build the fileInfo array for offline files.
				// This will avoid re-checking them on every re-draw of view
				buildFileInfo(section);

				// List them now
				ListView listView = (ListView) sectionRootView[0]
						.findViewById(R.id.files_list);
				rListAdapter = new MySimpleArrayAdapter(this, rFiles, section);

				// Assign adapter to ListView
				listView.setAdapter(rListAdapter);
			} else {
				rLoadingMsgLL.setVisibility(LinearLayout.VISIBLE);
				rProgBar.setVisibility(ProgressBar.GONE);
				rProgMsgTV.setText("No files.");
				MainActivity.toaster.showToast("No files found in resources.");
			}
		}

		if (section == 1) {
			if (!fFiles.isEmpty()) {
				// Set title
				setTitle("Files (" + (rFiles.size() + fFiles.size()) + ")");

				// Build the fileInfo array for offline files.
				// This will avoid re-checking them on every re-draw of view
				buildFileInfo(section);

				ListView listView = (ListView) sectionRootView[1]
						.findViewById(R.id.files_list);
				fListAdapter = new MySimpleArrayAdapter(this, fFiles, section);

				// Assign adapter to ListView
				listView.setAdapter(fListAdapter);
				// adapter.notifyDataSetChanged();
			} else {
				fLoadingMsgLL.setVisibility(LinearLayout.VISIBLE);
				fProgBar.setVisibility(ProgressBar.GONE);
				fProgMsgTV.setText("No files.");
				MainActivity.toaster.showToast("No files found in forums.");
			}
		}

	}

	private class MySimpleArrayAdapter extends ArrayAdapter<String> {
		private final Context context;
		private ArrayList<Mfile> files;
		private int section;

		public MySimpleArrayAdapter(Context context, ArrayList<Mfile> files,
				int section) {
			super(context, R.layout.files_listview_layout, new String[files
					.size()]);
			this.context = context;
			this.files = files;
			this.section = section;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View rowView = inflater.inflate(R.layout.files_listview_layout,
					parent, false);
			final int pos = position;

			// Obtain views to set details
			final TextView fNameView = (TextView) rowView
					.findViewById(R.id.files_file_name);
			final TextView fSizeView = (TextView) rowView
					.findViewById(R.id.files_file_size);
			final TextView fDateView = (TextView) rowView
					.findViewById(R.id.files_file_date);
			final LinearLayout fLayoutLL = (LinearLayout) rowView
					.findViewById(R.id.files_file_layout);
			final ProgressBar fProgBar = (ProgressBar) rowView
					.findViewById(R.id.files_progress);

			// Set values
			fNameView.setText(files.get(pos).getName());
			if (section == 0) {
				fSizeView.setText(rFiles.get(pos).getSize());
				fDateView.setText(rFiles.get(pos).getDate());
				fProgBar.setProgress(rFiles.get(pos).getProgress());
			}

			if (section == 1) {
				fSizeView.setText(fFiles.get(pos).getSize());
				fDateView.setText(fFiles.get(pos).getDate());
				fProgBar.setProgress(fFiles.get(pos).getProgress());
			}

			fLayoutLL.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					// Open or download ?
					if (section == 0) {
						File f = new File(rFiles.get(pos).getURL());
						if (f.exists()) {
							// Open
							openFile(rFiles.get(pos).getURL());
						} else {
							// Download
							// Define a UIupdater to update UI.
							UIupdater UU = new UIupdater(pos, section);
							FileDownloader FD = new FileDownloader(rFiles
									.get(pos), course, section, pos, UU);
							FD.startDownload();

						}
					}

					if (section == 1) {
						File f = new File(fFiles.get(pos).getURL());
						if (f.exists()) {
							// Open
							openFile(fFiles.get(pos).getURL());
						} else {
							// Download
							// Define a UIupdater to update UI.
							UIupdater UU = new UIupdater(pos, section);
							FileDownloader FD = new FileDownloader(fFiles
									.get(pos), course, section, pos, UU);
							FD.startDownload();
						}
					}
				}
			});

			return rowView;
		}
	}

	private void buildFileInfo(int section) {
		if (section == 0) {
			for (int i = 0; i < rFiles.size(); i++) {
				File f = new File(android.os.Environment
						.getExternalStorageDirectory().getPath()
						+ "/MDroid/"
						+ course.getName() + "/" + rFiles.get(i).getName());
				FolderDetails fd = new FolderDetails(f);
				if (fd.existWOExtCheck()) {
					rFiles.get(i).setURL(fd.getFileId());
					rFiles.get(i).setSize(fd.getFileSize());
					rFiles.get(i).setDate(fd.getFileRelDate());
					rFiles.get(i).setProgress(100);
				} else {
					rFiles.get(i).setSize("");
					rFiles.get(i).setDate("Click to download");
					rFiles.get(i).setProgress(0);
				}
			}
		}

		if (section == 1) {
			for (int i = 0; i < fFiles.size(); i++) {
				File f = new File(android.os.Environment
						.getExternalStorageDirectory().getPath()
						+ "/MDroid/"
						+ course.getName() + "/" + fFiles.get(i).getName());
				FolderDetails fd = new FolderDetails(f);
				if (fd.existWOExtCheck()) {
					fFiles.get(i).setURL(fd.getFileId());
					fFiles.get(i).setSize(fd.getFileSize());
					fFiles.get(i).setDate(fd.getFileRelDate());
					fFiles.get(i).setProgress(100);
				} else {
					fFiles.get(i).setSize("");
					fFiles.get(i).setDate("Click to download");
					fFiles.get(i).setProgress(0);
				}
			}
		}
	}

	private void openFile(String fName) {
		// File open will return intent with all other parameters set
		FileOpen fo = new FileOpen();
		Intent i = fo.getIntent(fName);
		try {
			startActivity(i);
		} catch (ActivityNotFoundException e) {
			String fileExt = fo.getFileExtension();
			MainActivity.toaster
					.showToast("No application found to open file type: "
							+ fileExt);
		}
	}

	@Override
	public void downloadAllFiles() {
		new AlertDialog.Builder(FilesActivity.this)
				.setMessage("Download all files?")
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								// Download files here
								UU = new UIupdater();
								FileDownloader FD = new FileDownloader(rFiles,
										fFiles, course, UU);
								FD.startDownload();

							}
						}).setNegativeButton("Cancel", null).show();

	}

	@Override
	public void updateFavStatus() {
		if (!stc.isFav(course.getId())) {
			stc.favCourse(course.getId());
			getFavMenuItem().setIcon(R.drawable.ic_action_important);
			getFavMenuItem().setTitle(R.string.menu_favourite_undo);
		} else {
			stc.unFavCourse(course.getId());
			getFavMenuItem().setIcon(R.drawable.ic_action_not_important);
			getFavMenuItem().setTitle(R.string.menu_favourite);
		}
	}

	@Override
	public void checkFavStatus() {
		if (stc.isFav(course.getId()))
			getFavMenuItem().setIcon(R.drawable.ic_action_important);
		else
			getFavMenuItem().setIcon(R.drawable.ic_action_not_important);
	}
}
