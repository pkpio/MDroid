package in.co.praveenkumar.mdroid.parser;

import java.util.ArrayList;

public class FilesInForumsParser {
	private ArrayList<String> fFileIDs = new ArrayList<String>();
	private ArrayList<String> fFileNames = new ArrayList<String>();
	private int nFiles = 0;

	public FilesInForumsParser(String html) {
		int prevIndex = 0;
		int endIndex = 0;

		while (true) {
			prevIndex = html.indexOf("<div class=\"attachments\"><a href=\"",
					prevIndex);
			if (prevIndex == -1)
				break;

			// For file Ids
			prevIndex += 34;
			endIndex = html.indexOf("\"", prevIndex);
			if (endIndex == -1)
				break;
			fFileIDs.add(html.substring(prevIndex, endIndex));

			// For file Names
			endIndex = html.indexOf("</a><br /></div><div class=\"posting",
					prevIndex);
			prevIndex = html.lastIndexOf("\">", endIndex) + 2;
			String textConvertedhtml = html.substring(prevIndex, endIndex);
			textConvertedhtml = android.text.Html.fromHtml(textConvertedhtml)
					.toString();
			fFileNames.add(textConvertedhtml);

			nFiles++;
		}
	}

	public ArrayList<String> getFileIds() {
		return fFileIDs;
	}

	public ArrayList<String> getFileNames() {
		return fFileNames;
	}

	public int getFileCount() {
		return nFiles;
	}
}
