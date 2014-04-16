/**************************************************************
 * Author: 	Praveen Kumar Pendyala
 * Project: MDroid
 * Created: 8th April, 2014.
 * License: http://creativecommons.org/licenses/by-nc-sa/3.0/
 **************************************************************/

package in.co.praveenkumar.mdroid.models;

public class MoodleSiteInfo {
	private String sitename;
	private String userid;
	private String username;
	private String firstname;
	private String lastname;
	private String userpictureurl;
	private Boolean downloadfiles;
	private Boolean uploadfiles;
	private String release;
	private String version;

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getSitename() {
		return sitename;
	}

	public void setSitename(String sitename) {
		this.sitename = sitename;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getUserpictureurl() {
		return userpictureurl;
	}

	public void setUserpictureurl(String userpictureurl) {
		this.userpictureurl = userpictureurl;
	}

	public Boolean getDownloadfiles() {
		return downloadfiles;
	}

	public void setDownloadfiles(Boolean downloadfiles) {
		this.downloadfiles = downloadfiles;
	}

	public Boolean getUploadfiles() {
		return uploadfiles;
	}

	public void setUploadfiles(Boolean uploadfiles) {
		this.uploadfiles = uploadfiles;
	}

	public String getRelease() {
		return release;
	}

	public void setRelease(String release) {
		this.release = release;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

}
