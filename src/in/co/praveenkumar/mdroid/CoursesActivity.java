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
import in.co.praveenkumar.mdroid.helpers.BaseActivity;
import in.co.praveenkumar.mdroid.helpers.FolderDetails;
import in.co.praveenkumar.mdroid.models.Course;
import in.co.praveenkumar.mdroid.parser.CoursesParser;
import in.co.praveenkumar.mdroid.services.MDroidService;

import java.util.ArrayList;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

@SuppressLint("DefaultLocale")
public class CoursesActivity extends BaseActivity {
	private ArrayList<Course> courses;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.courses);

		// testing purpose
		startService(new Intent(this, MDroidService.class));

		// Get html data and extract courses
		Bundle extras = getIntent().getExtras();
		if (extras == null) {
			return;
		}
		String html = extras.getString("html");
		CoursesParser cParser = new CoursesParser(html);
		courses = cParser.getCourses();
		String uName = cParser.getUserName();

		// Say hello to user
		TextView uNameTV = (TextView) findViewById(R.id.user_name);
		uNameTV.setText("Hello " + uName.toLowerCase(Locale.getDefault()) + "!");

		// Create folder for each course. Also replace course names with values
		// obtained back This is required because a few not allowed characters
		// are present in default names from Moodle
		FolderDetails FD = new FolderDetails(courses);
		courses = FD.createCourseFolders();

		listCoursesInListView();

	}

	private void listCoursesInListView() {
		// Set title
		setTitle("Courses (" + courses.size() + ")");

		ListView listView = (ListView) findViewById(R.id.courses_list);
		MySimpleArrayAdapter adapter = new MySimpleArrayAdapter(this, courses);

		// Assign adapter to ListView
		listView.setAdapter(adapter);
	}

	private class MySimpleArrayAdapter extends ArrayAdapter<String> {
		private final Context context;
		private final ArrayList<Course> courses;

		public MySimpleArrayAdapter(Context context, ArrayList<Course> courses) {
			super(context, R.layout.course_listview_layout, new String[courses
					.size()]);
			this.context = context;
			this.courses = courses;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View rowView = inflater.inflate(R.layout.course_listview_layout,
					parent, false);
			// Because position can't be used for it is not final
			final int pos = position;

			// Set course name
			final TextView cNmeView = (TextView) rowView
					.findViewById(R.id.course_name);
			cNmeView.setText(courses.get(position).getName());

			// Set onClickListeners on buttons
			final Button forumsBtn = (Button) rowView
					.findViewById(R.id.forums_btn);
			final Button filesBtn = (Button) rowView
					.findViewById(R.id.files_btn);

			forumsBtn.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					openActivity(0, courses.get(pos).getId(), courses.get(pos)
							.getName());
				}
			});
			filesBtn.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					openActivity(1, courses.get(pos).getId(), courses.get(pos)
							.getName());
				}
			});

			return rowView;
		}
	}

	private void openActivity(int selAct, String cId, String cName) {
		// 0: forums, else: files
		if (selAct == 0) {
			Intent i = new Intent(this, ForumActivity.class);
			i.putExtra("cId", cId);
			i.putExtra("cName", cName);
			startActivityForResult(i, 3);
		} else {
			Intent i = new Intent(this, FilesActivity.class);
			i.putExtra("cId", cId);
			i.putExtra("cName", cName);
			startActivityForResult(i, 3);
		}

	}
}
