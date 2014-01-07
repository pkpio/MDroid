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
