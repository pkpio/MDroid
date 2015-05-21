package in.co.praveenkumar.mdroid.model;

import com.google.gson.annotations.SerializedName;
import com.orm.SugarRecord;

/**
 * Represents course of any moodle user. Used in User profiles listing. A better
 * way could be retreiving from logged in user account using courseid but the
 * course may or may not be in his profile.
 * 
 * @author Praveen Kumar Pendyala<praveen@praveenkumar.co.in>
 * 
 */
public class MoodleUserCourse extends SugarRecord<MoodleUserCourse> {
	@SerializedName("id")
	int courseid;

	@SerializedName("fullname")
	String fullname;

	@SerializedName("shortname")
	String shortname;

	// Relational fields - for less logout complexity
	long siteid;
	int userid; // Moodle userid

	/**
	 * Get id of the course
	 * 
	 * @return
	 */
	public int getCourseid() {
		return courseid;
	}

	/**
	 * Get course fullname
	 * 
	 * @return
	 */
	public String getFullname() {
		return fullname;
	}

	/**
	 * Get course shortname
	 * 
	 * @return
	 */
	public String getShortname() {
		return shortname;
	}

	/**
	 * Get siteid of this record
	 * 
	 * @return
	 */
	public long getSiteid() {
		return siteid;
	}

	/**
	 * Userid of user to whom this course belongs to
	 * 
	 * @return
	 */
	public long getUserid() {
		return userid;
	}

	/**
	 * Set siteid of this record
	 * 
	 * @return
	 */
	public void setSiteid(long siteid) {
		this.siteid = siteid;
	}

	/**
	 * Userid of user to whom this course belongs to
	 * 
	 * @return
	 */
	public void setUserid(int userid) {
		this.userid = userid;
	}
}
