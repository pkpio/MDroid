package in.co.praveenkumar.mdroid;

import in.co.praveenkumar.R;
import in.co.praveenkumar.mdroid.helpers.BaseActivity;
import in.co.praveenkumar.mdroid.helpers.FileOpen;
import in.co.praveenkumar.mdroid.helpers.FolderDetails;

import java.io.File;
import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
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

public class OfflineFilesActivity extends BaseActivity {
	private String cName = "";
	private Context context;
	private ArrayList<String> files = new ArrayList<String>();
	private ArrayList<String> fileDateModified = new ArrayList<String>();
	private ArrayList<String> fileSize = new ArrayList<String>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.offline_files);

		// Get course name
		Bundle extras = getIntent().getExtras();
		if (extras == null) {
			return;
		}
		String cName = extras.getString("courseName");
		this.cName = cName;
		this.context = this;

		// Get file details.
		File root = new File(android.os.Environment
				.getExternalStorageDirectory().getPath()
				+ "/MDroid/"
				+ cName
				+ "/");
		FolderDetails folder = new FolderDetails(root);
		files = folder.getFileNames();
		fileDateModified = folder.getFilesDateModified();
		fileSize = folder.getFilesSize();

		// Set course name
		TextView cNameTV = (TextView) findViewById(R.id.course_name);
		cNameTV.setText(cName);

		// List them
		listFilesInListView();
	}

	private void listFilesInListView() {
		ListView listView = (ListView) findViewById(R.id.offline_files_list);
		MySimpleArrayAdapter adapter = new MySimpleArrayAdapter(this, files,
				fileDateModified, fileSize);

		// Assign adapter to ListView
		listView.setAdapter(adapter);
	}

	private class MySimpleArrayAdapter extends ArrayAdapter<String> {
		private final Context context;
		private final ArrayList<String> files;
		private final ArrayList<String> fileDateModified;
		private final ArrayList<String> fileSize;

		public MySimpleArrayAdapter(Context context, ArrayList<String> courses,
				ArrayList<String> courseDateModified,
				ArrayList<String> courseFilesNumber) {
			super(context, R.layout.offline_files_listview_layout, courses);
			this.context = context;
			this.files = courses;
			this.fileDateModified = courseDateModified;
			this.fileSize = courseFilesNumber;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View rowView = inflater.inflate(
					R.layout.offline_files_listview_layout, parent, false);
			// Because position can't be used for it is not final
			final int pos = position;

			// Set file details
			final TextView fNameTV = (TextView) rowView
					.findViewById(R.id.file_name);
			final TextView fDateModifTV = (TextView) rowView
					.findViewById(R.id.date_modified);
			final TextView fSizeTV = (TextView) rowView
					.findViewById(R.id.file_size);

			fNameTV.setText(files.get(position));
			fDateModifTV.setText(fileDateModified.get(position));
			fSizeTV.setText(fileSize.get(position));

			// Set OnClickListener on LinearLayout
			// Open button
			final Button fOpenBtn = (Button) rowView
					.findViewById(R.id.open_btn);
			fOpenBtn.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					String fileUrl = android.os.Environment
							.getExternalStorageDirectory().getPath()
							+ "/MDroid/" + cName + "/" + files.get(pos);
					openFile(fileUrl);
				}
			});
			// Delete button
			final TextView fDelBtn = (TextView) rowView
					.findViewById(R.id.delete_btn);
			fDelBtn.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					new AlertDialog.Builder(OfflineFilesActivity.this.context)
							.setMessage("Delete file?")
							.setPositiveButton("Delete",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog,
												int whichButton) {
											// Delete file here
											String fileUrl = android.os.Environment
													.getExternalStorageDirectory()
													.getPath()
													+ "/MDroid/"
													+ cName
													+ "/"
													+ files.get(pos);
											File fileToBeDeleted = new File(
													fileUrl);
											boolean deleted = fileToBeDeleted
													.delete();
											if (deleted) {
												MainActivity.toaster
														.showToast("File deleted");
												OfflineFilesActivity.this.files
														.remove(pos);
												OfflineFilesActivity.this.fileDateModified
														.remove(pos);
												OfflineFilesActivity.this.fileSize
														.remove(pos);
												listFilesInListView();

											}
										}
									}).setNegativeButton("Cancel", null).show();
				}
			});

			return rowView;
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

}
