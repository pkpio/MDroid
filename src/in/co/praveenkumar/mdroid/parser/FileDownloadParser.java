package in.co.praveenkumar.mdroid.parser;

public class FileDownloadParser {
	private String html = "";

	public FileDownloadParser(String html) {
		this.html = html;
	}

	public String getFileURL() {
		int prevIndex = 0;
		int endIndex = 0;
		String fURL = "";

		prevIndex = html.indexOf("<object", prevIndex) + 7;
		if (prevIndex != 6) {
			prevIndex = html.indexOf("data=\"", prevIndex) + 6;
			endIndex = html.indexOf("\"", prevIndex);
			fURL = html.substring(prevIndex, endIndex);
		}

		return fURL;
	}

	public String getFileExtension() {
		String ext = "pdf";

		// Possibility of FC on choosing wrong URL. Fix required !!
		int prevIndex = 0;
		int endIndex = 0;
		prevIndex = html.indexOf("filename=\"", prevIndex) + 11;
		endIndex = html.indexOf("\"", prevIndex);
		html = html.substring(prevIndex, endIndex);
		prevIndex = html.lastIndexOf(".") + 1;
		endIndex = html.length();
		ext = html.substring(prevIndex, endIndex);

		return ext;
	}

}
