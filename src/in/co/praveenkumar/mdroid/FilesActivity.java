package in.co.praveenkumar.mdroid;

import in.co.praveenkumar.R;
import in.co.praveenkumar.mdroid.helpers.BaseFragmentActivity;
import in.co.praveenkumar.mdroid.helpers.FileOpen;
import in.co.praveenkumar.mdroid.helpers.FolderDetails;
import in.co.praveenkumar.mdroid.networking.FetchForumFiles;
import in.co.praveenkumar.mdroid.networking.FetchResourceFiles;
import in.co.praveenkumar.mdroid.networking.FileDownloader;

import java.io.File;
import java.util.ArrayList;
import java.util.Locale;

import android.content.ActivityNotFoundException;
import android.content.Context;
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
	private String cId = "";
	private ArrayList<String> rFileIDs = new ArrayList<String>();
	private ArrayList<String> rFileNames = new ArrayList<String>();
	private ArrayList<String> rFileSize = new ArrayList<String>();
	private ArrayList<String> rFileDate = new ArrayList<String>();
	private int[] rFileProg; // Size wlll be declared later

	private ArrayList<String> fFileIDs = new ArrayList<String>();
	private ArrayList<String> fFileNames = new ArrayList<String>();
	private ArrayList<String> fFileSize = new ArrayList<String>();
	private ArrayList<String> fFileDate = new ArrayList<String>();
	private int[] fFileProg; // Size will be declared later

	private int rFileCount = 0;
	private int fFileCount = 0;
	private static LinearLayout rLoadingMsgLL;
	private static ProgressBar rProgBar;
	private static TextView rProgMsgTV;
	private static LinearLayout fLoadingMsgLL;
	private static ProgressBar fProgBar;
	private static TextView fProgMsgTV;
	private FetchResourceFiles FRF = new FetchResourceFiles();
	private FetchForumFiles FFF; // This requires params to instantiate
	private UIupdater UU;
	AsyncFilesFetch AFF = new AsyncFilesFetch();
	private MySimpleArrayAdapter rListAdapter;
	private MySimpleArrayAdapter fListAdapter;
	private String cName = "";

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
		cId = extras.getString("cId");
		cName = extras.getString("cName");

		// Set course name
		TextView cNameTV = (TextView) findViewById(R.id.files_course_name);
		cNameTV.setText(cName);

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

		public void setFileSize(String msg) {
			if (sec == 0) {
				rFileSize.set(pos, msg);
				rListAdapter.notifyDataSetChanged();
			}

			if (sec == 1) {
				fFileSize.set(pos, msg);
				fListAdapter.notifyDataSetChanged();
			}
		}

		public void setFileDate(String msg) {
			if (sec == 0) {
				rFileDate.set(pos, msg);
				rListAdapter.notifyDataSetChanged();
			}

			if (sec == 1) {
				fFileDate.set(pos, msg);
				fListAdapter.notifyDataSetChanged();
			}
		}

		public void setFileProg(int prog) {
			if (sec == 0) {
				rFileProg[pos] = prog;
				rListAdapter.notifyDataSetChanged();
			}

			if (sec == 1) {
				fFileProg[pos] = prog;
				fListAdapter.notifyDataSetChanged();
			}
		}

		public void setFileId(String fId) {
			if (sec == 0) {
				rFileIDs.set(pos, fId);
				rListAdapter.notifyDataSetChanged();
			}

			if (sec == 1) {
				fFileIDs.set(pos, fId);
				fListAdapter.notifyDataSetChanged();
			}
		}

		public void setForumProgress(int curFThread, int totalFThreads,
				ArrayList<String> fFileIDs, ArrayList<String> fFileNames,
				int nFiles) {
			AFF.doProgress(curFThread, totalFThreads);
			FilesActivity.this.fFileIDs = fFileIDs;
			FilesActivity.this.fFileNames = fFileNames;
			FilesActivity.this.fFileCount = nFiles;
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
			FRF.fetchFiles(cId);
			publishProgress(0);
			FFF = new FetchForumFiles(UU);
			FFF.fetchFiles(cId);
			return null;
		}

		public void doProgress(int curFThread, int totalFthreads) {
			publishProgress(curFThread, totalFthreads);
		}

		protected void onProgressUpdate(Integer... progress) {
			if (progress[0] == 0) {
				rLoadingMsgLL.setVisibility(LinearLayout.GONE);
				rFileIDs = FRF.getFileIds();
				rFileNames = FRF.getFileNames();
				rFileCount = FRF.getFilesCount();
				listFilesInListView(0);
			}
			// Non zero values for forum loading
			else {
				fProgMsgTV.setText("Fetching files from thread: " + progress[0]
						+ "/" + progress[1]);
				if (!fFileIDs.isEmpty())
					listFilesInListView(1);
			}
		}

		protected void onPostExecute(Long result) {
			fLoadingMsgLL.setVisibility(LinearLayout.GONE);
			fFileIDs = FFF.getFileIds();
			fFileNames = FFF.getFileNames();
			fFileCount = FFF.getFilesCount();
			listFilesInListView(1);
		}
	}

	private void listFilesInListView(int section) {
		if (section == 0) {
			if (!rFileIDs.isEmpty()) {
				// Set title
				setTitle("Files (" + rFileCount + ")");

				// Set size of progress array
				rFileProg = new int[rFileIDs.size()];

				// Build the fileInfo array for offline files.
				// This will avoid re-checking them on every re-draw of view
				buildFileInfo(section);

				// List them now
				ListView listView = (ListView) sectionRootView[0]
						.findViewById(R.id.files_list);
				rListAdapter = new MySimpleArrayAdapter(this, rFileNames,
						section);

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
			if (!fFileIDs.isEmpty()) {
				// Set title
				setTitle("Files (" + (rFileCount + fFileCount) + ")");

				// Set size of progress array
				fFileProg = new int[fFileIDs.size()];

				// Build the fileInfo array for offline files.
				// This will avoid re-checking them on every re-draw of view
				buildFileInfo(section);

				ListView listView = (ListView) sectionRootView[1]
						.findViewById(R.id.files_list);
				fListAdapter = new MySimpleArrayAdapter(this, fFileNames,
						section);

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
		private ArrayList<String> fileNames;
		private int section;

		public MySimpleArrayAdapter(Context context,
				ArrayList<String> fileNames, int section) {
			super(context, R.layout.files_listview_layout, fileNames);
			this.context = context;
			this.fileNames = fileNames;
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
			fNameView.setText(fileNames.get(pos));
			if (section == 0) {
				fSizeView.setText(rFileSize.get(pos));
				fDateView.setText(rFileDate.get(pos));
				fProgBar.setProgress(rFileProg[pos]);
			}

			if (section == 1) {
				fSizeView.setText(fFileSize.get(pos));
				fDateView.setText(fFileDate.get(pos));
				fProgBar.setProgress(fFileProg[pos]);
			}

			fLayoutLL.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					// Open or download ?
					if (section == 0) {
						File f = new File(rFileIDs.get(pos));
						if (f.exists()) {
							// Open
							openFile(rFileIDs.get(pos));
						} else {
							// Download
							// Define a UIupdater to update UI.
							UIupdater UU = new UIupdater(pos, section);
							FileDownloader FD = new FileDownloader(rFileIDs
									.get(pos), section, pos,
									fileNames.get(pos), cName, UU);
							FD.startDownload();

						}
					}

					if (section == 1) {
						File f = new File(fFileIDs.get(pos));
						if (f.exists()) {
							// Open
							openFile(fFileIDs.get(pos));
						} else {
							// Download
							// Define a UIupdater to update UI.
							UIupdater UU = new UIupdater(pos, section);
							FileDownloader FD = new FileDownloader(fFileIDs
									.get(pos), section, pos,
									fileNames.get(pos), cName, UU);
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
			for (int i = 0; i < rFileIDs.size(); i++) {
				File f = new File(android.os.Environment
						.getExternalStorageDirectory().getPath()
						+ "/MDroid/"
						+ cName + "/" + rFileNames.get(i));
				FolderDetails fd = new FolderDetails(f);
				if (fd.existWOExtCheck()) {
					rFileIDs.set(i, fd.getFileId());
					rFileSize.add(fd.getFileSize());
					rFileDate.add(fd.getFileRelDate());
					rFileProg[i] = 100;
				} else {
					rFileSize.add("");
					rFileDate.add("Click to download");
					rFileProg[i] = 0;
				}
			}
		}

		if (section == 1) {
			for (int i = 0; i < fFileIDs.size(); i++) {
				File f = new File(android.os.Environment
						.getExternalStorageDirectory().getPath()
						+ "/MDroid/"
						+ cName + "/" + fFileNames.get(i));
				FolderDetails fd = new FolderDetails(f);
				if (fd.existWOExtCheck()) {
					fFileIDs.set(i, fd.getFileId());
					fFileSize.add(i, fd.getFileSize());
					fFileDate.add(i, fd.getFileRelDate());
					fFileProg[i] = 100;
				} else {
					fFileSize.add(i, "");
					fFileDate.add(i, "Click to download");
					fFileProg[i] = 0;
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

	private void downloadAllFiles() {
		UU = new UIupdater();
		FileDownloader FD = new FileDownloader(fFileIDs, fFileNames, rFileIDs,
				rFileNames, cName, UU);
		FD.startBatchDownload();
	}
}
