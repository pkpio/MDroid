package in.co.praveenkumar.mdroid.helper;

import java.io.File;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

public class FileOpener {

	/**
	 * Opens a file. Shows a toast when opening failed.
	 * 
	 * @param context
	 * @param file
	 */
	public static void open(Context context, String file) {
		open(context, new File(file));
	}

	/**
	 * Opens a file. Shows a toast when opening failed.
	 * 
	 * @param context
	 * @param file
	 */
	public static void open(Context context, File file) {
		MimeTypeMap myMime = MimeTypeMap.getSingleton();
		String extension = MimeTypeMap.getFileExtensionFromUrl(file.toString());
		String mimeType = myMime.getMimeTypeFromExtension(extension);

		// We failed to determine mimeType from above. Use a generic type and
		// let Android system decide
		if (mimeType == null)
			mimeType = "*/*";

		// Setup intent
		Intent newIntent = new Intent(Intent.ACTION_VIEW);
		newIntent.setDataAndType(Uri.fromFile(file), mimeType);
		newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

		// Open file
		try {
			context.startActivity(newIntent);
		} catch (android.content.ActivityNotFoundException e) {
			Toast.makeText(context,
					"No application to open file type : " + extension,
					Toast.LENGTH_LONG).show();
		}
	}

}
