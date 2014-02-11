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

import in.co.praveenkumar.mdroid.models.Mfile;

import java.util.ArrayList;

public class FilesInForumsParser {
	ArrayList<Mfile> files;

	public FilesInForumsParser(String html) {
		int prevIndex = 0;
		int endIndex = 0;

		while (true) {
			Mfile file = new Mfile();
			prevIndex = html.indexOf("<div class=\"attachments\"><a href=\"",
					prevIndex);
			if (prevIndex == -1)
				break;

			// For file Ids
			prevIndex += 34;
			endIndex = html.indexOf("\"", prevIndex);
			if (endIndex == -1)
				break;
			file.setURL(html.substring(prevIndex, endIndex));

			// For file Names
			endIndex = html.indexOf("</a><br /></div><div class=\"posting",
					prevIndex);
			prevIndex = html.lastIndexOf("\">", endIndex) + 2;
			String textConvertedhtml = html.substring(prevIndex, endIndex);
			textConvertedhtml = android.text.Html.fromHtml(textConvertedhtml)
					.toString();
			file.setName(textConvertedhtml);
		}
	}

	public ArrayList<Mfile> getFiles() {
		return files;
	}
}
