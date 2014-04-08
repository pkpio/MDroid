package in.co.praveenkumar.mdroid.helpers;

import android.app.DownloadManager;
import android.app.DownloadManager.Request;
import android.content.Context;
import android.net.Uri;

public class MDroidDownloader {
	public final Boolean SYSTEM_DOWNLOADER = true;
	public final Boolean APP_DOWNLOADER = false;
	DownloadManager manager;

	public MDroidDownloader(Context context) {
		manager = (DownloadManager) context
				.getSystemService(Context.DOWNLOAD_SERVICE);
	}

	public void download(String fileUrl, Boolean visibility, Boolean choice) {
		if (choice == SYSTEM_DOWNLOADER) {
			Request request = new Request(Uri.parse(fileUrl));
			if (!visibility)
				request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN);
			else
				request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
			// Request and save it's value
		} else {

		}
	}

	public void setOptions() {

	}
}
