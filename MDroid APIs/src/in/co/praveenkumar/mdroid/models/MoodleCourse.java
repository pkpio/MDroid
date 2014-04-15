/**************************************************************
 * Author: 	Praveen Kumar Pendyala
 * Project: MDroid
 * Created: 27th May, 2014.
 * License: http://creativecommons.org/licenses/by-nc-sa/3.0/
 **************************************************************/

package in.co.praveenkumar.mdroid.models;

public class MoodleCourse {
	private String id;
	private String shortname;
	private String fullname;
	private long startdate;
	private long datecreated;
	private long datemodified;
	private String summary;
	private int enrolledusercount;

	public MoodleCourse() {
		this.id = "-1";
		this.shortname = "";
		this.fullname = "";
		this.startdate = 0;
		this.datecreated = 0;
		this.datemodified = 0;
		this.summary = "";
		this.enrolledusercount = -1;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getShortname() {
		return shortname;
	}

	public void setShortname(String shortname) {
		this.shortname = shortname;
	}

	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	public long getStartdate() {
		return startdate;
	}

	public void setStartdate(long startdate) {
		this.startdate = startdate;
	}

	public long getDatecreated() {
		return datecreated;
	}

	public void setDatecreated(long datecreated) {
		this.datecreated = datecreated;
	}

	public long getDatemodified() {
		return datemodified;
	}

	public void setDatemodified(long datemodified) {
		this.datemodified = datemodified;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public int getEnrolledusercount() {
		return enrolledusercount;
	}

	public void setEnrolledusercount(int enrolledusercount) {
		this.enrolledusercount = enrolledusercount;
	}

}
