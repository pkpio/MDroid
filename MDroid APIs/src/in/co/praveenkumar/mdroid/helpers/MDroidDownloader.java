package in.co.praveenkumar.mdroid.helpers;

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

public class MDroidDownloader {
	public static final Boolean SYSTEM_DOWNLOADER = true;
	public static final Boolean APP_DOWNLOADER = false;
	Context context;

	public MDroidDownloader(Context context) {
		this.context = context;

	}

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

	public void mdroidDownload(String fUrl, String fName) {
		InputStream input = null;
		OutputStream output = null;
		HttpURLConnection connection = null;
		try {
			URL url = new URL(fUrl);
			connection = (HttpURLConnection) url.openConnection();
			connection.connect();

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
			// File download failed.
		}
	}
}
