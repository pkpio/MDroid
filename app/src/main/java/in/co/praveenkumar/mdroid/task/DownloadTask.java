package in.co.praveenkumar.mdroid.task;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.annotation.TargetApi;
import android.app.DownloadManager;
import android.app.DownloadManager.Request;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.widget.Toast;

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
	 * @author Praveen Kumar Pendyala (praveen@praveenkumar.co.in)
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
	 * @param filepath
	 *            Destination file path. Base path is fixed to /sdcard/MDroid.
	 *            Set this to "" if not required.
	 * @param fileName
	 *            File name
	 * @param visibility
	 *            Set to true if a notification has to be displayed while
	 *            downloading. Works only when you choose SYSTEM_DOWNLOADER
	 * @param choice
	 *            The downloader of choice. Two options available.
	 *            Download.SYSTEM_DOWNLOADER and Download.APP_DOWNLOADER
	 * 
	 * 
	 * @return Bytes of the file downloaded
	 * 
	 * @author Praveen Kumar Pendyala (praveen@praveenkumar.co.in)
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public long download(String fileUrl, String filepath, String fileName,
			Boolean visibility, Boolean choice) {
		// Make directories if required
		File f = new File(
				Environment.getExternalStoragePublicDirectory("/MDroid")
						+ filepath);
		if (!f.exists())
			f.mkdirs();

		long reqId;
		if (choice == SYSTEM_DOWNLOADER) {
			DownloadManager manager = (DownloadManager) context
					.getSystemService(Context.DOWNLOAD_SERVICE);
			/**
			 * -TODO- Offer better alternative. Only a temporary, quick,
			 * workaround for 2.3.x devices. May not work on all sites.
			 */
			if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB)
				fileUrl = fileUrl.replace("https://", "http://");
			Request request = new Request(Uri.parse(fileUrl));
			try {
				request.setDestinationInExternalPublicDir("/MDroid", filepath
						+ fileName);
			} catch (Exception e) {
				Toast.makeText(context, "External storage not found!",
						Toast.LENGTH_SHORT).show();
				return 0;
			}
			request.setTitle(fileName);
			request.setDescription("MDroid file download");

			// Visibility setting not available in versions below Honeycomb
			if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB)
				if (!visibility)
					request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN);
				else
					request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

			// -TODO- save this id somewhere for progress retrieval
			reqId = manager.enqueue(request);
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