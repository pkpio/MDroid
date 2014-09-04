package in.co.praveenkumar.mdroid.moodlemodel;

import com.google.gson.annotations.SerializedName;
import com.orm.SugarRecord;

public class MoodleEvent extends SugarRecord<MoodleEvent> {

	// since id is a reserved field in SugarRecord
	@SerializedName("id")
	int eventid;

	@SerializedName("name")
	String name;

	@SerializedName("description")
	String description;

	@SerializedName("format")
	int format;

	@SerializedName("courseid")
	int courseid;

	@SerializedName("groupid")
	int groupid;

	@SerializedName("userid")
	int userid;

	@SerializedName("repeatid")
	int repeatid;

	@SerializedName("modulename")
	String modulename;

	@SerializedName("instance")
	int instance;

	@SerializedName("eventtype")
	String eventtype;

	@SerializedName("timestart")
	int timestart;

	@SerializedName("timeduration")
	int timeduration;

	@SerializedName("visible")
	int visible;

	@SerializedName("uuid")
	String uuid;

	@SerializedName("sequence")
	int sequence;

	@SerializedName("timemodified")
	int timemodified;

	@SerializedName("subscriptionid")
	int subscriptionid;

	// Error fields are required only for Events instead

	// Relational and other fields
	long siteid;

	/**
	 * get the site id of this course
	 * 
	 * @param siteid
	 */
	public long getSiteid() {
		return siteid;
	}

	/**
	 * set the site id
	 * 
	 * @param siteid
	 */
	public void setSiteid(long siteid) {
		this.siteid = siteid;
	}

	/**
	 * get event id
	 * 
	 * @return
	 */
	public int getEventid() {
		return eventid;
	}

	/**
	 * Get event name
	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * Get Description
	 * 
	 * @return
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Get description format (1 = HTML, 0 = MOODLE, 2 = PLAIN or 4 = MARKDOWN)
	 * 
	 * @return
	 */
	public int getFormat() {
		return format;
	}

	/**
	 * get course id
	 * 
	 * @return
	 */
	public int getCourseid() {
		return courseid;
	}

	/**
	 * Get group id
	 * 
	 * @return
	 */
	public int getGroupid() {
		return groupid;
	}

	/**
	 * Get user id
	 * 
	 * @return
	 */
	public int getUserid() {
		return userid;
	}

	/**
	 * Get repeat id
	 * 
	 * @return
	 */
	public int getRepeatid() {
		return repeatid;
	}

	/**
	 * Get (Optional) module name
	 * 
	 * @return
	 */
	public String getModulename() {
		return modulename;
	}

	/**
	 * Get instance id
	 * 
	 * @return
	 */
	public int getInstance() {
		return instance;
	}

	/**
	 * Get Event type
	 * 
	 * @return
	 */
	public String getEventtype() {
		return eventtype;
	}

	/**
	 * Get timestart
	 * 
	 * @return
	 */
	public int getTimestart() {
		return timestart;
	}

	/**
	 * Get timeduration
	 * 
	 * @return
	 */
	public int getTimeduration() {
		return timeduration;
	}

	/**
	 * Get visible
	 * 
	 * @return
	 */
	public int getVisible() {
		return visible;
	}

	/**
	 * Get (Optional) unique id of ical events
	 * 
	 * @return
	 */
	public String getUuid() {
		return uuid;
	}

	/**
	 * Get sequence
	 * 
	 * @return
	 */
	public int getSequence() {
		return sequence;
	}

	/**
	 * Get time modified
	 * 
	 * @return
	 */
	public int getTimemodified() {
		return timemodified;
	}

	/**
	 * Get (Optional) Subscription id
	 * 
	 * @return
	 */
	public int getSubscriptionid() {
		return subscriptionid;
	}

}
