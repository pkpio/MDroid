/**************************************************************
 * Author: 	Praveen Kumar Pendyala
 * Project: MDroid
 * Created: 31st May, 2014.
 * License: http://creativecommons.org/licenses/by-nc-sa/3.0/
 **************************************************************/

package in.co.praveenkumar.mdroid.models;

public class MoodleFile {
	String filename;
	String filepath;
	long filesize;
	String fileurl;
	long timecreated;
	long timemodified;
	String author;
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public String getFilepath() {
		return filepath;
	}
	public void setFilepath(String filepath) {
		this.filepath = filepath;
	}
	public long getFilesize() {
		return filesize;
	}
	public void setFilesize(long filesize) {
		this.filesize = filesize;
	}
	public String getFileurl() {
		return fileurl;
	}
	public void setFileurl(String fileurl) {
		this.fileurl = fileurl;
	}
	public long getTimecreated() {
		return timecreated;
	}
	public void setTimecreated(long timecreated) {
		this.timecreated = timecreated;
	}
	public long getTimemodified() {
		return timemodified;
	}
	public void setTimemodified(long timemodified) {
		this.timemodified = timemodified;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}

}
