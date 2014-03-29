package in.co.praveenkumar.mdroid.models;

public class MoodleCourse {
	private String id;
	private String shortname;
	private String fullname;
	private long startdate;
	private long datecreated;
	private long datemodified;

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

}
