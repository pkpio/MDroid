package in.co.praveenkumar;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
import android.widget.Toast;

public class fileListing extends Activity {
	ArrayList<String> ForumFileIDs = new ArrayList<String>();
	ArrayList<String> ForumFileNames = new ArrayList<String>();
	ArrayList<String> ResourcesFileIDs = new ArrayList<String>();
	ArrayList<String> ResourcesFileNames = new ArrayList<String>();
	String courseName = "";
	String serverAddress = MDroidActivity.serverAddress;
	AppPreferences appPrefs;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.filelisting);
		// For getting Preferences
		appPrefs = new AppPreferences(getApplicationContext());
		
		TabHost tabHost = (TabHost) findViewById(R.id.tabHost);
		tabHost.setup();

		TabSpec spec1 = tabHost.newTabSpec("Tab 1");
		spec1.setContent(R.id.resourcesFileList);
		spec1.setIndicator("from Resources");

		TabSpec spec2 = tabHost.newTabSpec("Tab 2");
		spec2.setIndicator("from Forums");
		spec2.setContent(R.id.forumsFileList);

		tabHost.addTab(spec1);
		tabHost.addTab(spec2);

		Bundle extras = getIntent().getExtras();
		if (extras == null) {
			return;
		}

		String courseID = extras.getString("courseID");
		courseName = extras.getString("courseName");

		// Setting title of FileListing activity..
		setTitle("Files (): " + courseName);

		new getPageContent().execute(courseID);

	}

	/* AsycTask Thread */

	private class getPageContent extends AsyncTask<String, Integer, Long> {
		protected Long doInBackground(String... courseID) {

			try {
				getPageContentResources(courseID[0]);
			} catch (ClientProtocolException e) {
				Toast.makeText(
						getBaseContext(),
						"ClientProtocolException " + e
								+ " while trying to use postData();",
						Toast.LENGTH_SHORT).show();
			} catch (IOException e) {
				Toast.makeText(
						getBaseContext(),
						"IOException " + e + " while trying to use postData();",
						Toast.LENGTH_SHORT).show();
			}

			try {
				getPageContentForumsOne(courseID[0]);
			} catch (ClientProtocolException e) {

			} catch (IOException e) {

			}

			return null;
		}

		// Purely for publishprogress call..as this can't be done from outside!!
		public void doProgress(int value) {
			publishProgress(value);
		}

		protected void onProgressUpdate(Integer... progress) {
			// setProgressPercent(progress[0]);
			listFilesInListView(ResourcesFileNames, ResourcesFileIDs,
					"myFilesResources");
			Integer filesCount = 0;
			filesCount += ForumFileNames.size() + ResourcesFileNames.size();
			setTitle("Files (" + filesCount + "): " + courseName);

			if (ResourcesFileNames.size() != 0) {
				hideLoadingMessageLayout(0);
			} else {
				hideLoadingMessageLayout(2);
			}

			listFilesInListView(ForumFileNames, ForumFileIDs, "myFilesForums");
		}

		protected void onPostExecute(Long result) {
			if (ResourcesFileNames.size() != 0) {
				hideLoadingMessageLayout(0);
			} else {
				hideLoadingMessageLayout(2);
			}

			if (ForumFileNames.size() != 0) {
				hideLoadingMessageLayout(1);
			} else {
				hideLoadingMessageLayout(3);
			}

			Integer filesCount = 0;
			filesCount += ForumFileNames.size() + ResourcesFileNames.size();
			setTitle("Files (" + filesCount + "): " + courseName);

		}

	}

	/*
	 * Resources page specific functions . All functions below are just for
	 * resources page details!
	 */

	public void getPageContentResources(String courseID)
			throws ClientProtocolException, IOException {

		DefaultHttpClient httpclient = MDroidActivity.httpclient;

		HttpGet httpgetCourse = new HttpGet(serverAddress
				+ "/course/view.php?id=" + courseID);

		HttpResponse responseCourse = httpclient.execute(httpgetCourse);
		HttpEntity entityCourse = responseCourse.getEntity();

		try {
			inputStreamToStringResources(responseCourse.getEntity()
					.getContent());
		} catch (IOException e) {

			Toast.makeText(getBaseContext(),
					"WhileCallingString IOException " + e, Toast.LENGTH_LONG)
					.show();

		}

		if (entityCourse != null) {
			entityCourse.consumeContent();
		}
	}

	private void inputStreamToStringResources(InputStream is)
			throws IOException {
		String line = "";
		StringBuilder total = new StringBuilder();

		// Wrap a BufferedReader around the InputStream
		BufferedReader rd = new BufferedReader(new InputStreamReader(is));

		// Read response until the end
		while ((line = rd.readLine()) != null) {
			total.append(line);
		}

		extractFileDetailsForResourses(total.toString());
	}

	public void extractFileDetailsForResourses(String htmlDataString) {

		int prevIndex = 0;
		int endIndex = 0;

		while (true) {
			prevIndex = htmlDataString.indexOf(serverAddress
					+ "/mod/resource/view.php?id=", prevIndex);

			if (prevIndex == -1)
				break;

			endIndex = htmlDataString.indexOf('\"', prevIndex);
			ResourcesFileIDs.add(htmlDataString.substring(prevIndex, endIndex));

			prevIndex = htmlDataString.indexOf("<span", prevIndex) + 5;
			prevIndex = htmlDataString.indexOf(">", prevIndex) + 1;
			endIndex = htmlDataString.indexOf("<span class=\"accesshide",
					prevIndex);
			String textConvertedhtmlDataString = htmlDataString.substring(
					prevIndex, endIndex);
			textConvertedhtmlDataString = android.text.Html.fromHtml(
					textConvertedhtmlDataString).toString();
			ResourcesFileNames.add(textConvertedhtmlDataString);
			// Update the present list on to the UI thread
			getPageContent getpagecontent = new getPageContent();
			getpagecontent.doProgress(1);
		}

	}

	/* End of resources page specific functions */

	/*
	 * Forum page specific functions . All functions below are just for
	 * resources page details!
	 */

	public void getPageContentForumsOne(String courseID)
			throws ClientProtocolException, IOException {

		DefaultHttpClient httpclient = MDroidActivity.httpclient;

		HttpGet httpgetCourse = new HttpGet(serverAddress
				+ "/mod/forum/index.php?id=" + courseID);

		HttpResponse responseCourse = httpclient.execute(httpgetCourse);
		HttpEntity entityCourse = responseCourse.getEntity();

		try {
			inputStreamToStringForumsOne(responseCourse.getEntity()
					.getContent());
		} catch (IOException e) {

		}

		if (entityCourse != null) {
			entityCourse.consumeContent();
		}
	}

	private void inputStreamToStringForumsOne(InputStream is)
			throws IOException {
		String line = "";
		StringBuilder total = new StringBuilder();

		// Wrap a BufferedReader around the InputStream
		BufferedReader rd = new BufferedReader(new InputStreamReader(is));

		// Read response until the end
		while ((line = rd.readLine()) != null) {
			total.append(line);
		}

		extractFileDetailsForForumsOne(total.toString());
	}

	public void extractFileDetailsForForumsOne(String htmlDataString) {

		int prevIndex = 0;
		int endIndex = 0;
		String forumViewID = "";

		while (true) {
			prevIndex = htmlDataString.indexOf("<a href=\"view.php?f=",
					prevIndex);

			if (prevIndex == -1)
				break;

			prevIndex += 20;
			endIndex = htmlDataString.indexOf("\"", prevIndex);
			forumViewID = (htmlDataString.substring(prevIndex, endIndex));
		}

		try {
			getPageContentForumsTwo(forumViewID);
		} catch (ClientProtocolException e) {

		} catch (IOException e) {

		}

	}

	public void getPageContentForumsTwo(String forumViewID)
			throws ClientProtocolException, IOException {

		DefaultHttpClient httpclient = MDroidActivity.httpclient;

		HttpGet httpgetCourse = new HttpGet(serverAddress
				+ "/mod/forum/view.php?f=" + forumViewID);

		HttpResponse responseCourse = httpclient.execute(httpgetCourse);
		HttpEntity entityCourse = responseCourse.getEntity();

		try {
			inputStreamToStringForumsTwo(responseCourse.getEntity()
					.getContent());
		} catch (IOException e) {

		}

		if (entityCourse != null) {
			entityCourse.consumeContent();
		}
	}

	private void inputStreamToStringForumsTwo(InputStream is)
			throws IOException {
		String line = "";
		StringBuilder total = new StringBuilder();

		// Wrap a BufferedReader around the InputStream
		BufferedReader rd = new BufferedReader(new InputStreamReader(is));

		// Read response until the end
		while ((line = rd.readLine()) != null) {
			total.append(line);
		}

		extractFileDetailsForForumsTwo(total.toString());
	}

	// Call to ListFilesInListView for Forum files made here..

	public void extractFileDetailsForForumsTwo(String htmlDataString) {

		ArrayList<String> DiscussionIDs = new ArrayList<String>();
		int prevIndex = 0;
		int endIndex = 0;

		while (true) {
			prevIndex = htmlDataString.indexOf(
					"class=\"topic starter\"><a href=\"", prevIndex);

			if (prevIndex == -1)
				break;
			prevIndex += 31;
			prevIndex = htmlDataString.indexOf("/mod/forum/discuss.php?d=",
					prevIndex) + 25;
			endIndex = htmlDataString.indexOf("\"", prevIndex);
			DiscussionIDs.add(htmlDataString.substring(prevIndex, endIndex));
		}

		for (int i = 0; i < DiscussionIDs.size(); i++) {

			try {
				getPageContentForumsFinal(DiscussionIDs.get(i));
			} catch (ClientProtocolException e) {

			} catch (IOException e) {

			}

		}

	}

	public void getPageContentForumsFinal(String DiscussionID)
			throws ClientProtocolException, IOException {

		DefaultHttpClient httpclient = MDroidActivity.httpclient;

		HttpGet httpgetCourse = new HttpGet(serverAddress
				+ "/mod/forum/discuss.php?d=" + DiscussionID);

		HttpResponse responseCourse = httpclient.execute(httpgetCourse);
		HttpEntity entityCourse = responseCourse.getEntity();

		try {
			inputStreamToStringForumsFinal(responseCourse.getEntity()
					.getContent());
		} catch (IOException e) {

		}

		if (entityCourse != null) {
			entityCourse.consumeContent();
		}
	}

	private void inputStreamToStringForumsFinal(InputStream is)
			throws IOException {
		String line = "";
		StringBuilder total = new StringBuilder();

		// Wrap a BufferedReader around the InputStream
		BufferedReader rd = new BufferedReader(new InputStreamReader(is));

		// Read response until the end
		while ((line = rd.readLine()) != null) {
			total.append(line);
		}

		extractFileDetailsForForumsFinal(total.toString());
	}

	public void extractFileDetailsForForumsFinal(String htmlDataString) {

		int prevIndex = 0;
		int endIndex = 0;

		while (true) {
			prevIndex = htmlDataString.indexOf(
					"<div class=\"attachments\"><a href=\"", prevIndex);

			if (prevIndex == -1)
				break;

			prevIndex += 34;
			endIndex = htmlDataString.indexOf("\"", prevIndex);
			
			if(endIndex== -1)
				break;
			ForumFileIDs.add(htmlDataString.substring(prevIndex, endIndex));

			prevIndex = endIndex + 3;
			prevIndex = htmlDataString.indexOf("\">", prevIndex);
			prevIndex += 2;
			endIndex = htmlDataString.indexOf(
					"</a><br /></div><div class=\"posting\">", prevIndex);
			String textConvertedhtmlDataString = htmlDataString.substring(
					prevIndex, endIndex);
			textConvertedhtmlDataString = android.text.Html.fromHtml(
					textConvertedhtmlDataString).toString();
			ForumFileNames.add(textConvertedhtmlDataString);

			// Update the list on to UI thread;
			getPageContent getpagecontent = new getPageContent();
			getpagecontent.doProgress(1);

		}

	}

	// /* End of forum page specific function */
	//
	// /* Common functions again */public void getPageContentForumsFinal(String
	// DiscussionID)

	public String getPageContentForIITBDownload(String fileID)
			throws ClientProtocolException, IOException {
		DefaultHttpClient httpclient = MDroidActivity.httpclient;
		String fileDownloadAddress = "";

		HttpGet httpgetCourse = new HttpGet(fileID);

		HttpResponse responseCourse = httpclient.execute(httpgetCourse);
		HttpEntity entityCourse = responseCourse.getEntity();

		try {
			fileDownloadAddress = inputStreamToStringForIITBDownload(responseCourse
					.getEntity().getContent());
		} catch (IOException e) {

		}

		if (entityCourse != null) {
			entityCourse.consumeContent();
		}
		return fileDownloadAddress;
	}

	private String inputStreamToStringForIITBDownload(InputStream is)
			throws IOException {
		String line = "";
		StringBuilder total = new StringBuilder();

		// Wrap a BufferedReader around the InputStream
		BufferedReader rd = new BufferedReader(new InputStreamReader(is));

		// Read response until the end
		while ((line = rd.readLine()) != null) {
			total.append(line);
		}

		String fileDownloadAddress = extractFileDetailsForIITBDownload(total
				.toString());
		return fileDownloadAddress;
	}

	public String extractFileDetailsForIITBDownload(String htmlDataString) {

		int prevIndex = 0;
		int endIndex = 0;
		String fileDownloadAddress = "";

		prevIndex = htmlDataString.indexOf("<object", prevIndex) + 7;
		if (prevIndex == 6) {
			fileDownloadAddress = "";
		} else {
			prevIndex = htmlDataString.indexOf("data=\"", prevIndex) + 6;
			endIndex = htmlDataString.indexOf("\"", prevIndex);
			fileDownloadAddress = htmlDataString.substring(prevIndex, endIndex);
		}

		return fileDownloadAddress;

	}

	public Context context = this;
	public Object adapter;

	public void hideLoadingMessageLayout(int i) {
		switch (i) {
		case 0: {
			LinearLayout mainLayout = (LinearLayout) this
					.findViewById(R.id.linearLayoutLoadingResourses);
			mainLayout.setVisibility(LinearLayout.GONE);
			break;
		}
		case 1: {
			LinearLayout mainLayout = (LinearLayout) this
					.findViewById(R.id.linearLayoutLoadingForums);
			mainLayout.setVisibility(LinearLayout.GONE);
			break;
		}
		case 2: {
			ProgressBar loadingImage = (ProgressBar) this
					.findViewById(R.id.linearLayoutLoadingProgressImageResourses);
			loadingImage.setVisibility(ProgressBar.GONE);

			TextView loadingText = (TextView) this
					.findViewById(R.id.linearLayoutLoadingProgressTextResourses);
			loadingText.setText("No files found!");
			break;
		}
		case 3: {
			ProgressBar loadingImage = (ProgressBar) this
					.findViewById(R.id.linearLayoutLoadingProgressImageForums);
			loadingImage.setVisibility(ProgressBar.GONE);

			TextView loadingText = (TextView) this
					.findViewById(R.id.linearLayoutLoadingProgressTextForums);
			loadingText.setText("No files found!");
			break;
		}
		}
	}

	public void listFilesInListView(ArrayList<String> fileNames,
			ArrayList<String> fileIDs, String ListViewID) {

		int resID = getResources().getIdentifier(ListViewID, "id",
				"in.co.praveenkumar");
		ListView listView = (ListView) findViewById(resID);

		adapter = new MySimpleArrayAdapter(this, fileNames, fileIDs, ListViewID);
		// Assign adapter to ListView
		listView.setAdapter((ListAdapter) adapter);
	}

	// AsyncTask task for download...IMPORTANT:: No UI elements in this!
	private class DownloadFile extends AsyncTask<String, Integer, String> {

		int resID;
		int resIDButton;
		String fileSizeString = "";

		@Override
		protected String doInBackground(String... fileDetails) {

			try {

				// Extracting FileName and FileUrl from FileData....
				String fileName = fileDetails[0];
				String fileID = fileDetails[1];
				String DownloadStatusTextViewID = fileDetails[2];
				String fOperationButtonID = fileDetails[3];
				resID = getResources().getIdentifier(DownloadStatusTextViewID,
						"id", "in.co.praveenkumar");
				resIDButton = getResources().getIdentifier(fOperationButtonID,
						"tag", "in.co.praveenkumar");

				DefaultHttpClient httpclient = MDroidActivity.httpclient;

				HttpGet httpgetFile;
				if (serverAddress.compareTo("http://moodle.iitb.ac.in") == 0) {
					String fileDownloadAddress = getPageContentForIITBDownload(fileID);
					if (fileDownloadAddress.compareTo("") == 0) {
						httpgetFile = new HttpGet(fileID);
					} else {
						httpgetFile = new HttpGet(fileDownloadAddress);
					}
				} else {
					httpgetFile = new HttpGet(fileID);
				}

				HttpResponse responseFile = httpclient.execute(httpgetFile);
				HttpEntity entityFile = responseFile.getEntity();

				// File attributes finding...like extension, size...presently
				// only for files hosted on Moodle server..//
				// Works for all moodles when the files are hosted on moodle
				Header[] headers = responseFile.getAllHeaders();
				String fileNameInServer = "";
				if (serverAddress.compareTo("http://moodle.iitb.ac.in") == 0) {
					fileNameInServer = headers[6].getValue();
				} else {
					fileNameInServer = headers[7].getValue();
				}
				int fileLength = (int) entityFile.getContentLength();
				// File extension finding --- In general for all moodles
				int prevIndex = 0;
				int endIndex = 0;
				prevIndex = fileNameInServer.indexOf("filename=\"", prevIndex) + 11;
				endIndex = fileNameInServer.indexOf("\"", prevIndex);
				fileNameInServer = fileNameInServer.substring(prevIndex,
						endIndex);
				prevIndex = fileNameInServer.lastIndexOf(".") + 1;
				endIndex = fileNameInServer.length();
				String fileExtension = fileNameInServer.substring(prevIndex,
						endIndex);

				// download the file
				InputStream input = new BufferedInputStream(
						entityFile.getContent());
				File file = new File(Environment.getExternalStorageDirectory(),
						"/MDroid/" + courseName + "/" + fileName + "."
								+ fileExtension);
				OutputStream output = new FileOutputStream(file);

				byte data[] = new byte[1024];
				long total = 0;
				int count;
				while ((count = input.read(data)) != -1) {
					total += count;
					// publishing the progress....
					publishProgress((int) (total * 100 / fileLength),
							fileLength / 1024);
					output.write(data, 0, count);
				}

				output.flush();
				output.close();
				input.close();
			} catch (Exception e) {

			}
			return null;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected void onProgressUpdate(final Integer... progress) {
			super.onProgressUpdate(progress);
			if (progress[1] > 1024) {
				fileSizeString = (progress[1] / 1024) + " MB";
			} else {
				fileSizeString = (progress[1]) + " KB";
			}

			TextView downloadStatusTextView = (TextView) findViewById(resID);
			Button fileOperationButton = (Button) findViewById(resIDButton);
			if (downloadStatusTextView != null) {
				downloadStatusTextView.setText(fileSizeString + ", "
						+ progress[0] + "% downloaded");
				fileOperationButton.setText("Downloading..");
				fileOperationButton.setEnabled(false);
				fileOperationButton.setBackgroundResource(R.drawable.black_btm);
			}
		}

		@Override
		protected void onPostExecute(String result) {
			TextView downloadStatusTextView = (TextView) findViewById(resID);
			Button fileOperationButton = (Button) findViewById(resIDButton);
			if (downloadStatusTextView != null) {
				downloadStatusTextView.setText(fileSizeString
						+ ", download complete");
				fileOperationButton.setText("Open");
				fileOperationButton.setBackgroundResource(R.drawable.blue_btn);
			}

		}
	}

	public class MySimpleArrayAdapter extends ArrayAdapter<String> {
		private final Context context;
		private final ArrayList<String> fileName;
		private final ArrayList<String> fileID;
		private final String ListViewID;

		public MySimpleArrayAdapter(Context context,
				ArrayList<String> fileName, ArrayList<String> fileID,
				String ListViewID) {
			super(context, R.layout.filelistviewlayout, fileName);
			this.context = context;
			this.fileName = fileName;
			this.fileID = fileID;
			this.ListViewID = ListViewID;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View rowView = inflater.inflate(R.layout.filelistviewlayout,
					parent, false);

			if (position % 2 == 0)
				rowView.setBackgroundResource(R.drawable.listview_evenitem_color);

			final TextView textView = (TextView) rowView
					.findViewById(R.id.myFileName);
			textView.setText(fileName.get(position));

			final TextView textViewDownload = (TextView) rowView
					.findViewById(R.id.myFileDownloadStatus);

			final Button fileOperationButton = (Button) rowView
					.findViewById(R.id.myFileButton);

			if (ListViewID == "myFilesResources") {
				textView.setTag(position + 1000);
				textViewDownload.setId(position + 1000);
				fileOperationButton.setId(position + 10001000);
			} else {
				textView.setTag(position + 2000);
				textViewDownload.setId(position + 2000);
				fileOperationButton.setId(position + 20002000);
			}

			// check if file exists!
			String courseDir = android.os.Environment
					.getExternalStorageDirectory().getPath()
					+ "/MDroid/"
					+ courseName + "/";
			File dir = new File(courseDir);
			ArrayList<String> fileNames = new ArrayList<String>();
			ArrayList<String> fileExtensions = new ArrayList<String>();
			File[] files = dir.listFiles();
			for (File file : files) {
				if (!(file.isDirectory())) {
					int startIndex;
					int endIndex;
					String fileNameDir = file.toString();
					startIndex = fileNameDir.lastIndexOf("/") + 1;
					endIndex = fileNameDir.length();
					fileNameDir = fileNameDir.substring(startIndex, endIndex);
					startIndex = 0;
					endIndex = fileNameDir.lastIndexOf(".");
					String fileNameNoExtension = fileNameDir.substring(
							startIndex, endIndex);
					startIndex = endIndex + 1;
					endIndex = fileNameDir.length();
					String fileExtension = fileNameDir.substring(startIndex,
							endIndex);
					if (fileNameNoExtension.equals("") == false
							&& fileExtension.equals("") == false) {
						fileNames.add(fileNameNoExtension);
						fileExtensions.add(fileExtension);
					}

				}
			}

			File f = null;
			int match = 0;
			String extension = "";
			// Check if file matches with any of the above files without
			// extensions
			for (int i = 0; i < fileNames.size(); i++) {
				if (fileName.get(position).equals(fileNames.get(i)) == true) {
					f = new File(android.os.Environment
							.getExternalStorageDirectory().getPath()
							+ "/MDroid/"
							+ courseName
							+ "/"
							+ fileNames.get(i)
							+ "." + fileExtensions.get(i));
					match = 1;
					extension = fileExtensions.get(i);
					break;
				}

			}

			if (match == 1) {// f.exists()
				// Saving file extension in Hint for existing file..
				textView.setHint(extension);

				long fileSize = f.length();
				Date lastModDate = new Date(f.lastModified());
				SimpleDateFormat format = new SimpleDateFormat(
						"MM/dd/yyyy hh:mm a");
				String lastModDateformatted = format.format(lastModDate);

				String fileSizeInfo;
				if (fileSize > (1024 * 1024))
					fileSizeInfo = fileSize / (1024 * 1024) + "MB";
				else if (fileSize > 1024)
					fileSizeInfo = fileSize / 1024 + "KB";
				else
					fileSizeInfo = fileSize + "Bytes";

				fileOperationButton.setText("Open");
				fileOperationButton.setBackgroundResource(R.drawable.blue_btn);
				textViewDownload.setTextColor(Color.parseColor("#3f3f3f"));
				textViewDownload.setText(fileSizeInfo + ", modified: "
						+ lastModDateformatted);

			} else {
				// Saving fileID in Hint for not existing file..
				textView.setHint(fileID.get(position));
				fileOperationButton.setText("Download");
			}

			fileOperationButton.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					String fOperationButtonID = fileOperationButton.getId()
							+ "";
					if (fileOperationButton.getText() == "Download") {
						fileOperationButton.setEnabled(false);
						fileOperationButton
								.setBackgroundResource(R.drawable.black_btm);
						DownloadFile downloadFile = new DownloadFile();
						downloadFile.execute(textView.getText().toString(),
								textView.getHint().toString(), textView
										.getTag().toString(),
								fOperationButtonID);
					} else {
						String file = textView.getText().toString() + "."
								+ textView.getHint();
						String fileUrl = android.os.Environment
								.getExternalStorageDirectory().getPath()
								+ "/MDroid/" + courseName + "/" + file;
						File fileToBeOpened = new File(fileUrl);
						Intent i = new Intent();
						i.setAction(android.content.Intent.ACTION_VIEW);
						i.setDataAndType(Uri.fromFile(fileToBeOpened),
								"application/" + textView.getHint());
						try {
							startActivity(i);
						} catch (ActivityNotFoundException e) {
							Toast.makeText(
									getBaseContext(),
									"No application found to open file type\n"
											+ textView.getHint(),
									Toast.LENGTH_LONG).show();
						}
					}
				}
			});

			rowView.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					String fOperationButtonID = fileOperationButton.getId()
							+ "";
					if (fileOperationButton.getText() == "Download") {
						fileOperationButton.setEnabled(false);
						fileOperationButton
								.setBackgroundResource(R.drawable.black_btm);
						DownloadFile downloadFile = new DownloadFile();
						downloadFile.execute(textView.getText().toString(),
								textView.getHint().toString(), textView
										.getTag().toString(),
								fOperationButtonID);
					} else {
						String file = textView.getText().toString() + "."
								+ textView.getHint();
						String fileUrl = android.os.Environment
								.getExternalStorageDirectory().getPath()
								+ "/MDroid/" + courseName + "/" + file;
						File fileToBeOpened = new File(fileUrl);
						Intent i = new Intent();
						i.setAction(android.content.Intent.ACTION_VIEW);
						i.setDataAndType(Uri.fromFile(fileToBeOpened),
								"application/" + textView.getHint());
						try {
							startActivity(i);
						} catch (ActivityNotFoundException e) {
							Toast.makeText(
									getBaseContext(),
									"No application found to open file type\n"
											+ textView.getHint(),
									Toast.LENGTH_LONG).show();
						}
					}
				}
			});

			return rowView;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.AboutMDroid:
			showDialog(0);

			break;
		case R.id.Credits:
			showDialog(1);

			break;
		case R.id.HelpMenu:
			showDialog(2);

			break;
		case R.id.ChangeServer:
			showDialog(3);

			break;
		case R.id.Rating:
			showDialog(4);

			break;
		}
		return true;
	}

	public Dialog onCreateDialog(int id) {
		final Dialog dialog = new Dialog(this);

		switch (id) {
		case 0:
			dialog.setContentView(R.layout.aboutmdroid);
			dialog.setTitle("About MDroid");
			break;

		case 1:
			dialog.setContentView(R.layout.credits);
			dialog.setTitle("Credits");
			break;

		case 2:
			dialog.setContentView(R.layout.mdroidhelp);
			dialog.setTitle("Help");
			break;

		case 4:
			dialog.setContentView(R.layout.rating);
			dialog.setTitle("Rate MDroid");

			Button submitRatingButton = (Button) dialog
					.findViewById(R.id.submitRating);

			submitRatingButton.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					RatingBar mBar = (RatingBar) dialog
							.findViewById(R.id.ratingBar);
					int[] i = new int[] { (int) mBar.getRating() };
					
					// Saving prefs
					appPrefs.saveIntPrefs("rated", 1);

					if (i[0] <= 3) {
						Toast.makeText(getBaseContext(),
								"submiting " + i[0] + " star rating...",
								Toast.LENGTH_SHORT).show();
						dialog.dismiss();
					} else {
						dialog.dismiss();
						Intent intent = new Intent(Intent.ACTION_VIEW);
						intent.setData(Uri
								.parse("market://details?id=in.co.praveenkumar"));
						startActivity(intent);
					}

					// statusMessage.setText("value is " + i[0]);
				}
			});
			break;

		case 3:
			dialog.setContentView(R.layout.changeserver);
			dialog.setTitle("change server");

			final EditText changeServerEdittextValue = (EditText) dialog
					.findViewById(R.id.changeServerEditText);
			changeServerEdittextValue.setText(serverAddress);

			Button changeServerValueButton = (Button) dialog
					.findViewById(R.id.changeServerValue);
			Button resetServerValueButton = (Button) dialog
					.findViewById(R.id.resetServerValue);

			changeServerValueButton.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					// String newServervalue =
					// changeServerEdittextValue.getText()
					// .toString();
					// // Saving in preferences..
					// SharedPreferences prefs =
					// getPreferences(Context.MODE_PRIVATE);
					// SharedPreferences.Editor editor = prefs.edit();
					// editor.putString("serverAddress", newServervalue);
					// editor.commit();
					// serverAddress = newServervalue;
					// dialog.dismiss();
					//
					// Toast.makeText(getBaseContext(),
					// "Server prefernce saved to \n" + serverAddress,
					// Toast.LENGTH_SHORT).show();
					Toast.makeText(
							getBaseContext(),
							"Can't change! Go to login screen to change server",
							Toast.LENGTH_SHORT).show();
				}
			});

			resetServerValueButton.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					// changeServerEdittextValue
					// .setText("http://moodle.iitb.ac.in");
					// // Saving in preferences..
					// SharedPreferences prefs =
					// getPreferences(Context.MODE_PRIVATE);
					// SharedPreferences.Editor editor = prefs.edit();
					// editor.putString("serverAddress",
					// "http://moodle.iitb.ac.in");
					// editor.commit();
					// serverAddress = "http://moodle.iitb.ac.in";
					// dialog.dismiss();

					Toast.makeText(
							getBaseContext(),
							"Can't change! Go to login screen to change server",
							Toast.LENGTH_SHORT).show();
				}
			});

			break;
		}
		return dialog;
	}
}