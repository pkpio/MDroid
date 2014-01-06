package in.co.praveenkumar.mdroid;

import in.co.praveenkumar.R;
import in.co.praveenkumar.mdroid.helpers.BaseActivity;
import in.co.praveenkumar.mdroid.helpers.FolderDetails;

import java.io.File;
import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class OfflineCoursesActivity extends BaseActivity {
	private ArrayList<String> courses = new ArrayList<String>();
	private ArrayList<String> courseDateModified = new ArrayList<String>();
	private ArrayList<String> courseFilesNumber = new ArrayList<String>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.offline_courses);

		File root = new File(android.os.Environment
				.getExternalStorageDirectory().getPath() + "/MDroid/");
		FolderDetails folder = new FolderDetails(root);
		courses = folder.getSubFolderNames();
		courseDateModified = folder.getSubFolderDateModified();
		courseFilesNumber = folder.getSubFolderFileCount();

		listCoursesInListView();
	}

	private void listCoursesInListView() {
		ListView listView = (ListView) findViewById(R.id.offline_courses_list);
		MySimpleArrayAdapter adapter = new MySimpleArrayAdapter(this, courses,
				courseDateModified, courseFilesNumber);

		// Assign adapter to ListView
		listView.setAdapter(adapter);
	}

	private class MySimpleArrayAdapter extends ArrayAdapter<String> {
		private final Context context;
		private final ArrayList<String> courses;
		private final ArrayList<String> courseDateModified;
		private final ArrayList<String> courseFilesNumber;

		public MySimpleArrayAdapter(Context context, ArrayList<String> courses,
				ArrayList<String> courseDateModified,
				ArrayList<String> courseFilesNumber) {
			super(context, R.layout.offline_courses_listview_layout, courses);
			this.context = context;
			this.courses = courses;
			this.courseDateModified = courseDateModified;
			this.courseFilesNumber = courseFilesNumber;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View rowView = inflater.inflate(
					R.layout.offline_courses_listview_layout, parent, false);
			// Because position can't be used for it is not final
			final int pos = position;

			// Set course name
			final TextView cNameTV = (TextView) rowView
					.findViewById(R.id.course_name);
			final TextView dateModifTV = (TextView) rowView
					.findViewById(R.id.date_modified);
			final TextView fileCountTV = (TextView) rowView
					.findViewById(R.id.files_count);

			cNameTV.setText(courses.get(position));
			dateModifTV.setText(courseDateModified.get(position));
			fileCountTV.setText(courseFilesNumber.get(position)
					+ " offline files");

			// Set OnClickListener on LinearLayout
			final LinearLayout courseCardLL = (LinearLayout) rowView
					.findViewById(R.id.course_folder);
			courseCardLL.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					if (courseFilesNumber.get(pos).contentEquals("0"))
						MainActivity.toaster.showToast("No offline files.");
					else
						openFiles(courses.get(pos));
				}
			});

			return rowView;
		}
	}

	private void openFiles(String cName) {
		Intent i = new Intent(this, OfflineFilesActivity.class);
		i.putExtra("courseName", cName);
		startActivityForResult(i, 2);
	}
}
