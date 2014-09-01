package in.co.praveenkumar.mdroid.task;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.DownloadManager;
import android.app.DownloadManager.Request;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;

public class DownloadTask {
	public static final Boolean SYSTEM_DOWNLOADER = true;
	public static final Boolean APP_DOWNLOADER = false;
	Context context;

	/**
	 * Download files.<br/>
	 * This supports two methods,
	 * <ol>
	 * <li>System Downloader</li>
	 * <li>App downloader</li>
	 * </ol>
	 * <br/>
	 * The downloader may be chosen while starting the download using the
	 * download method.
	 * 
	 * @author praveen
	 * 
	 */
	public DownloadTask(Context context) {
		this.context = context;

	}

	/**
	 * Download file
	 * 
	 * @param fileUrl
	 *            Source url of the file
	 * @param fileName
	 *            File name. Path is fixed to /sdcard/MDroid
	 * @param visibility
	 *            Set to true if a notification has to be displayed while
	 *            downloading. Works only when you choose SYSTEM_DOWNLOADER
	 * @param choice
	 *            The downloader of choice. Two options available.
	 *            Download.SYSTEM_DOWNLOADER and Download.APP_DOWNLOADER
	 * 
	 * 
	 * @return Bytes of the file downloaded
	 */
	public long download(String fileUrl, String fileName, Boolean visibility,
			Boolean choice) {
		long reqId;
		if (choice == SYSTEM_DOWNLOADER) {
			DownloadManager manager = (DownloadManager) context
					.getSystemService(Context.DOWNLOAD_SERVICE);
			Request request = new Request(Uri.parse(fileUrl));
			if (!visibility)
				request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN);
			else
				request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

			reqId = manager.enqueue(request);
			// save this id somewhere
		} else {
			mdroidDownload(fileUrl, fileName);
			reqId = 0;
		}
		return reqId;
	}

	void mdroidDownload(String fUrl, String fName) {
		InputStream input = null;
		OutputStream output = null;
		HttpURLConnection connection = null;
		try {
			URL url = new URL(fUrl);
			connection = (HttpURLConnection) url.openConnection();
			connection.connect();

			// Make directories if required
			File f = new File(Environment.getExternalStorageDirectory()
					+ "/MDroid/");
			f.mkdirs();

			// download the file
			input = connection.getInputStream();
			output = new FileOutputStream(
					Environment.getExternalStorageDirectory() + "/MDroid/"
							+ fName);

			byte data[] = new byte[4096];
			int count;
			while ((count = input.read(data)) != -1) {
				output.write(data, 0, count);
			}

			output.close();
			input.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}