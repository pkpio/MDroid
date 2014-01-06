package in.co.praveenkumar.mdroid.helpers;

import java.io.File;

import android.content.Intent;
import android.net.Uri;

public class FileOpen {
	private String fileExt = "";

	public Intent getIntent(String fileURL) {
		Intent i = new Intent();
		int startIndex = fileURL.lastIndexOf(".") + 1;
		int endIndex = fileURL.length();
		fileExt = fileURL.substring(startIndex, endIndex);
		File file = new File(fileURL);
		i.setAction(android.content.Intent.ACTION_VIEW);
		i.setDataAndType(Uri.fromFile(file), "application/" + fileExt);

		return i;
	}

	public String getFileExtension() {
		return fileExt;
	}

}
