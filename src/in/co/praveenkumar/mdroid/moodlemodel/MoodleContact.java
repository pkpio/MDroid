package in.co.praveenkumar.mdroid.moodlemodel;

import com.google.gson.annotations.SerializedName;
import com.orm.SugarRecord;
import com.orm.dsl.Ignore;

public class MoodleContact extends SugarRecord<MoodleContact> {

	// Constants
	@Ignore
	public static final int STATUS_ONLINE = 0;
	@Ignore
	public static final int STATUS_OFFLINE = 1;
	@Ignore
	public static final int STATUS_STRANGER = 2;

	// since id is a reserved field in SugarRecord
	@SerializedName("id")
	int contactid;

	@SerializedName("fullname")
	String fullname;

	@SerializedName("profileimageurl")
	String profileimageurl;

	@SerializedName("profileimageurlsmall")
	String profileimageurlsmall;

	@SerializedName("unread")
	int unread;

	@SerializedName("status")
	int status;

	// Errors. Not to be stored in sql db.
	@Ignore
	@SerializedName("exception")
	String exception;

	@Ignore
	@SerializedName("errorcode")
	String errorcode;

	@Ignore
	@SerializedName("message")
	String message;

	@Ignore
	@SerializedName("debuginfo")
	String debuginfo;

	// Relational fields
	long siteid;

	/**
	 * Moodle contact id for the contact
	 * 
	 * @return
	 */
	public int getContactid() {
		return contactid;
	}

	/**
	 * Contact fullname
	 * 
	 * @return
	 */
	public String getFullname() {
		return fullname;
	}

	/**
	 * Contact profile picture url
	 * 
	 * @return
	 */
	public String getProfileimageurl() {
		return profileimageurl;
	}

	/**
	 * Smaller picture of user
	 * 
	 * @return
	 */
	public String getProfileimageurlsmall() {
		return profileimageurlsmall;
	}

	/**
	 * Unread message count
	 * 
	 * @return
	 */
	public int getUnread() {
		return unread;
	}

	/**
	 * Status of the user. There are 3 possibilities, <br/>
	 * <ul>
	 * <li>STATUS_ONLINE</li>
	 * <li>STATUS_OFFLINE</li>
	 * <li>STATUS_STRANGER</li>
	 * <ul>
	 * 
	 * @return status
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * Exception occurred while retrieving
	 * 
	 * @return
	 */
	public String getException() {
		return exception;
	}

	/**
	 * Errorcode of error occurred while retrieving
	 * 
	 * @return
	 */
	public String getErrorcode() {
		return errorcode;
	}

	/**
	 * Message of error occurred while retrieving
	 * 
	 * @return
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Debug info on the error occurred
	 * 
	 * @return
	 */
	public String getDebuginfo() {
		return debuginfo;
	}

	/**
	 * Get the siteid of this course
	 * 
	 * @return
	 */
	public long getSiteid() {
		return siteid;
	}

	/**
	 * set the site id of this course
	 * 
	 * @param siteid
	 */
	public void setSiteid(long siteid) {
		this.siteid = siteid;
	}

	/**
	 * Set the status of the user. There are 3 possibilities, <br/>
	 * <ul>
	 * <li>STATUS_ONLINE</li>
	 * <li>STATUS_OFFLINE</li>
	 * <li>STATUS_STRANGER</li>
	 * <ul>
	 * 
	 * @return status
	 */
	public void setStatus(int status) {
		this.status = status;
	}

}
