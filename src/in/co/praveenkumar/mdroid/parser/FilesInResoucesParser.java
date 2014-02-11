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

package in.co.praveenkumar.mdroid.parser;

import in.co.praveenkumar.mdroid.MainActivity;
import in.co.praveenkumar.mdroid.models.Mfile;

import java.util.ArrayList;

public class FilesInResoucesParser {
	private String mURL = MainActivity.mURL;
	private ArrayList<Mfile> files = new ArrayList<Mfile>();

	public FilesInResoucesParser(String html) {
		int prevIndex = 0;
		int endIndex = 0;
		int i = 0;// For naming files which give error while fetching name

		while (true) {
			Mfile file = new Mfile();
			prevIndex = html.indexOf(mURL + "/mod/resource/view.php?id=",
					prevIndex);
			if (prevIndex == -1)
				break;

			// File Ids.
			endIndex = html.indexOf('\"', prevIndex);
			file.setURL(html.substring(prevIndex, endIndex));

			// For file names
			prevIndex = html.indexOf("<span", prevIndex) + 5;
			prevIndex = html.indexOf(">", prevIndex) + 1;
			endIndex = html.indexOf("<span class=\"accesshide", prevIndex);
			String textConvertedhtml = "File " + i;
			if (endIndex != -1)
				textConvertedhtml = html.substring(prevIndex, endIndex);
			textConvertedhtml = android.text.Html.fromHtml(textConvertedhtml)
					.toString();
			file.setName(textConvertedhtml);

			files.add(file);
		}
	}

	public ArrayList<Mfile> getFiles() {
		return files;
	}

}
