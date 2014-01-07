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

package in.co.praveenkumar.mdroid.networking;

import in.co.praveenkumar.mdroid.FilesActivity.UIupdater;
import in.co.praveenkumar.mdroid.MainActivity;
import in.co.praveenkumar.mdroid.parser.FileDownloadParser;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.util.ArrayList;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

// Only for this class the Async thread is implemented in the Networking file
// This is to avoid too many call backs to the FileActivity class to update progress
public class FileDownloader {
	private String DEBUG_TAG = "NETWORKING_FILE_DOWNLOADER";
	private String mURL = MainActivity.mURL;

	// File details
	private String fId = "";
	private String fName = "";
	private String fSize = "";
	private String cName = "";
	private String fLocation = "";

	// For batch download
	private Boolean BatchMode = false;
	ArrayList<String> rFileIds = new ArrayList<String>();
	ArrayList<String> rFileNms = new ArrayList<String>();
	ArrayList<String> fFileIds = new ArrayList<String>();
	ArrayList<String> fFileNms = new ArrayList<String>();

	// For use across all function in the main class
	AsyncFileDownload AFD = new AsyncFileDownload();
	AsyncBatchFileDownload ABFD = new AsyncBatchFileDownload();

	// For UI updation
	UIupdater UU;

	public FileDownloader(String fId, int sec, int pos, String fName,
			String cName, UIupdater UU) {
		this.fId = fId;
		this.fName = fName;
		this.cName = cName;
		this.UU = UU;

		// Tell user that file download request received.
		UU.setFileDate("Request received..");
	}

	public FileDownloader(ArrayList<String> rFileIds,
			ArrayList<String> rFileNms, ArrayList<String> fFileIds,
			ArrayList<String> fFileNms, String cName, UIupdater UU) {
		this.rFileIds = rFileIds;
		this.rFileNms = rFileNms;
		this.fFileIds = fFileIds;
		this.fFileNms = fFileNms;
		this.cName = cName;
		this.UU = UU;
		this.BatchMode = true;

		// Tell the user his request has been received
		MainActivity.toaster.showToast("Total file count: "
				+ (rFileIds.size() + fFileIds.size()) + "\nResources: "
				+ rFileIds.size() + "  Forums: " + fFileIds.size());

	}

	public void startDownload() {
		// Check if in Batch mode
		if (BatchMode)
			ABFD.execute();
		else
			// Start download for single file
			AFD.execute();
	}

	// Async single file download thread
	private class AsyncFileDownload extends AsyncTask<String, Integer, Long> {
		private Boolean downloadStatus = false;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			UU.setFileDate("Fetching file url");
		}

		protected Long doInBackground(String... values) {
			String fURL = getFileURL(fId);
			publishProgress(101);
			downloadStatus = download(fURL);
			return null;
		}

		public void doProgress(int value) {
			publishProgress(value);
		}

		protected void onProgressUpdate(Integer... progress) {
			// Anything >100 is considered for a different purpose
			// <100 values are directly pushed onto progress
			if (progress[0] > 100) {
				if (progress[0] == 101)
					UU.setFileDate("Starting download");
				if (progress[0] == 102)
					UU.setFileSize(fSize);
			} else {
				UU.setFileDate(progress[0] + "% done");
				UU.setFileProg(progress[0]);
			}
		}

		protected void onPostExecute(Long result) {
			// Download success. Change fId to local location.
			// Update date changed to a few secs ago.
			if (downloadStatus) {
				UU.setFileId(fLocation);
				UU.setFileDate("A few seconds ago");
			} else {
				UU.setFileDate("Failed. Retry ?");
				UU.setFileProg(0);
				// Delete partial file if downloaded.
				File f = new File(fLocation);
				if (f.exists())
					f.delete();
			}
		}
	}

	// Async batch file download thread
	private class AsyncBatchFileDownload extends
			AsyncTask<String, Integer, Long> {
		Boolean empty = false;
		Boolean wait = false;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		protected Long doInBackground(String... values) {
			// Check resources.
			if (rFileIds.size() != 0) {
				for (int i = 0; i < rFileIds.size(); i++) {
					File f = new File(rFileIds.get(i));
					// If file is not already downloaded
					if (!f.exists()) {
						// Wait for previous progress update to complete
						while (wait) {
						}
						wait = true;
						UU.setPosSec(i, 0);
						fId = rFileIds.get(i);
						fName = rFileNms.get(i);
						publishProgress(105);
						String fURL = getFileURL(fId);
						publishProgress(101);
						if (download(fURL))
							publishProgress(103);
						else
							publishProgress(104);
					}
				}
			}

			// Check forums
			if (fFileIds.size() != 0) {
				for (int i = 0; i < fFileIds.size(); i++) {
					File f = new File(fFileIds.get(i));
					// If file is not already downloaded
					if (!f.exists()) {
						// Wait for previous progress update to complete
						while (wait) {
						}
						wait = true;
						UU.setPosSec(i, 1);
						fId = fFileIds.get(i);
						fName = fFileNms.get(i);
						publishProgress(105);
						String fURL = getFileURL(fId);
						publishProgress(101);
						if (download(fURL))
							publishProgress(103);
						else
							publishProgress(104);
					}
				}
			}

			// Everything is empty.
			else {
				empty = true;
			}

			return null;
		}

		protected void onProgressUpdate(Integer... progress) {
			// Anything >100 is considered for a different purpose
			// <100 values are directly pushed onto progress
			if (progress[0] > 100) {
				if (progress[0] == 101)
					UU.setFileDate("Starting download");
				if (progress[0] == 102)
					UU.setFileSize(fSize);
				if (progress[0] == 103) {
					UU.setFileId(fLocation);
					UU.setFileDate("A few seconds ago");
					wait = false;
				}
				if (progress[0] == 104) {
					UU.setFileDate("Failed. Retry ?");
					UU.setFileProg(0);
					// Delete partial file if downloaded.
					File f = new File(fLocation);
					if (f.exists())
						f.delete();
					wait = false;
				}
				if (progress[0] == 105)
					UU.setFileDate("Fetching file url");
			} else {
				UU.setFileDate(progress[0] + "% done");
				UU.setFileProg(progress[0]);
			}
		}

		public void doProgress(int value) {
			publishProgress(value);
		}

		protected void onPostExecute(Long result) {
			if (empty)
				MainActivity.toaster.showToast("No files to dowload");
			else
				MainActivity.toaster.showToast("Batch download complete.");
		}
	}

	// fId could be the current download URL or the page at fId
	// might have the actual file download address.
	// In any case we fetch the content at fId and attempt to read
	// the url from the response. If fId is infact the file download url
	// then we will get back fId as the final url. We operate on the
	// fURL to download file.
	// ** This way file may be downloaded twice. Figure out a better way **
	private String getFileURL(String fId) {
		String fURL = "";
		try {
			// Get default app client. Only that has the cookie data
			DefaultHttpClient httpclient = MainActivity.httpclient;

			// Fetch thread page
			HttpGet httpgetCourse = new HttpGet(fId);
			HttpResponse response = httpclient.execute(httpgetCourse);
			InputStream is = response.getEntity().getContent();

			// Read content from stream
			String line = "";
			StringBuilder total = new StringBuilder();
			BufferedReader rd = new BufferedReader(new InputStreamReader(is));

			// Read response until the end
			while ((line = rd.readLine()) != null) {
				total.append(line);
			}
			String html = total.toString();
			FileDownloadParser FDP = new FileDownloadParser(html);
			fURL = FDP.getFileURL();

		} catch (MalformedURLException e) {
			// URL malformed
			e.printStackTrace();
			Log.d(DEBUG_TAG, "Malformed URL");
		} catch (IOException e) {
			// Error while making connection
			e.printStackTrace();
			Log.d(DEBUG_TAG, "IOException ! Net problem ?");
		}

		if (fURL.contentEquals(""))
			fURL = fId;

		return fURL;
	}

	private Boolean download(String fURL) {
		String fExt = "";
		Boolean downloadStatus = false;

		try {
			// Get default app client. Only that has the cookie data
			DefaultHttpClient httpclient = MainActivity.httpclient;

			// Fetch thread page
			HttpGet httpget = new HttpGet(fURL);
			HttpResponse response = httpclient.execute(httpget);
			HttpEntity entity = response.getEntity();

			// File attributes finding...like extension, size...presently
			// only for files hosted on Moodle server..//
			// Works for all moodles when the files are hosted on moodle
			Header[] headers = response.getAllHeaders();
			String fileNameInServer = "";
			if (mURL.compareTo("http://moodle.iitb.ac.in") == 0) {
				fileNameInServer = headers[6].getValue();
			} else {
				fileNameInServer = headers[7].getValue();
			}
			int fSizeInt = (int) entity.getContentLength();
			fSize = getFileSize(fSizeInt);

			// Update file size to UI
			if (BatchMode)
				ABFD.doProgress(102);
			else
				AFD.doProgress(102);

			// Find file extension. Name from listing will be used instead of
			// server.
			FileDownloadParser FDP = new FileDownloadParser(fileNameInServer);
			fExt = FDP.getFileExtension();

			// Download file
			InputStream input = new BufferedInputStream(entity.getContent());
			fLocation = Environment.getExternalStorageDirectory() + "/MDroid/"
					+ cName + "/" + fName + "." + fExt;
			File file = new File(fLocation);
			OutputStream output = new FileOutputStream(file);

			byte data[] = new byte[1024];
			long total = 0;
			int count;
			while ((count = input.read(data)) != -1) {
				total += count;
				// publishing the progress....
				if (BatchMode)
					ABFD.doProgress((int) (total * 100 / fSizeInt));
				else
					AFD.doProgress((int) (total * 100 / fSizeInt));
				output.write(data, 0, count);
			}

			// Download successful
			downloadStatus = true;

			output.flush();
			output.close();
			input.close();

		} catch (MalformedURLException e) {
			// URL malformed
			e.printStackTrace();
			Log.d(DEBUG_TAG, "Malformed URL");
		} catch (IOException e) {
			// Error while making connection
			e.printStackTrace();
			Log.d(DEBUG_TAG, "IOException ! Net problem ?");
		}

		return downloadStatus;

	}

	private String getFileSize(int fileSize) {
		String fileSizeInfo = "";
		if (fileSize > (1024 * 1024))
			fileSizeInfo = fileSize / (1024 * 1024) + " MB";
		else if (fileSize > 1024)
			fileSizeInfo = fileSize / 1024 + " KB";
		else
			fileSizeInfo = fileSize + " Bytes";

		return fileSizeInfo;

	}

}
