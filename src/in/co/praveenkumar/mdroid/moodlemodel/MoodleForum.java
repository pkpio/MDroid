package in.co.praveenkumar.mdroid.moodlemodel;

import com.google.gson.annotations.SerializedName;
import com.orm.SugarRecord;
import com.orm.dsl.Ignore;

public class MoodleForum extends SugarRecord<MoodleForum> {

	// since id is a reserved field in SugarRecord
	@SerializedName("id")
	int forumid;

	@SerializedName("course")
	String courseid;

	@SerializedName("name")
	String name;

	@SerializedName("intro")
	String intro;

	@SerializedName("introformat")
	String introformat;

	@SerializedName("assessed")
	int assessed;

	@SerializedName("assesstimestart")
	int assesstimestart;

	@SerializedName("assesstimefinish")
	int assesstimefinish;

	@SerializedName("scale")
	int scale;

	@SerializedName("maxbytes")
	int maxbytes;

	@SerializedName("maxattachments")
	int maxattachments;

	@SerializedName("forcesubscribe")
	int forcesubscribe;

	@SerializedName("trackingtype")
	int trackingtype;

	@SerializedName("rsstype")
	int rsstype;

	@SerializedName("rssarticles")
	int rssarticles;

	@SerializedName("timemodified")
	int timemodified;

	@SerializedName("warnafter")
	int warnafter;

	@SerializedName("blockafter")
	int blockafter;

	@SerializedName("blockperiod")
	int blockperiod;

	@SerializedName("completiondiscussions")
	int completiondiscussions;

	@SerializedName("completionreplies")
	int completionreplies;

	@SerializedName("completionposts")
	int completionposts;

	@SerializedName("cmid")
	int cmid;

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

	// Relational and other fields
	long siteid;
	String coursename;

	/**
	 * Get coursename of the forum
	 * 
	 * @return
	 */
	public String getCoursename() {
		return coursename;
	}

	/**
	 * Set coursename of the forum
	 * 
	 * @param coursename
	 */
	public void setCoursename(String coursename) {
		this.coursename = coursename;
	}

	/**
	 * Get Forum id
	 * 
	 * @return
	 */
	public int getForumid() {
		return forumid;
	}

	/**
	 * Get The forum type
	 * 
	 * @return
	 */
	public String getCourseid() {
		return courseid;
	}

	/**
	 * Get Forum name
	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * Get Forum intro
	 * 
	 * @return
	 */
	public String getIntro() {
		return intro;
	}

	/**
	 * Get Forum intro format (1 = HTML, 0 = MOODLE, 2 = PLAIN or 4 = MARKDOWN)
	 * 
	 * @return
	 */
	public String getIntroformat() {
		return introformat;
	}

	/**
	 * Get Aggregate type
	 * 
	 * @return
	 */
	public int getAssessed() {
		return assessed;
	}

	/**
	 * Get Assess start time
	 * 
	 * @return
	 */
	public int getAssesstimestart() {
		return assesstimestart;
	}

	/**
	 * Get Assess finish time
	 * 
	 * @return
	 */
	public int getAssesstimefinish() {
		return assesstimefinish;
	}

	/**
	 * Get Scale
	 * 
	 * @return
	 */
	public int getScale() {
		return scale;
	}

	/**
	 * Get Maximum attachment size
	 * 
	 * @return
	 */
	public int getMaxbytes() {
		return maxbytes;
	}

	/**
	 * Get Maximum number of attachments
	 * 
	 * @return
	 */
	public int getMaxattachments() {
		return maxattachments;
	}

	/**
	 * Get Force users to subscribe
	 * 
	 * @return
	 */
	public int getForcesubscribe() {
		return forcesubscribe;
	}

	/**
	 * Get Subscription mode
	 * 
	 * @return
	 */
	public int getTrackingtype() {
		return trackingtype;
	}

	/**
	 * Get RSS feed for this activity
	 * 
	 * @return
	 */
	public int getRsstype() {
		return rsstype;
	}

	/**
	 * Get Number of RSS recent articles
	 * 
	 * @return
	 */
	public int getRssarticles() {
		return rssarticles;
	}

	/**
	 * Get Time modified
	 * 
	 * @return
	 */
	public int getTimemodified() {
		return timemodified;
	}

	/**
	 * Get Post threshold for warning
	 * 
	 * @return
	 */
	public int getWarnafter() {
		return warnafter;
	}

	/**
	 * Get Post threshold for blocking
	 * 
	 * @return
	 */
	public int getBlockafter() {
		return blockafter;
	}

	/**
	 * Get Time period for blocking
	 * 
	 * @return
	 */
	public int getBlockperiod() {
		return blockperiod;
	}

	/**
	 * Get Student must create discussions
	 * 
	 * @return
	 */
	public int getCompletiondiscussions() {
		return completiondiscussions;
	}

	/**
	 * Get Student must post replies
	 * 
	 * @return
	 */
	public int getCompletionreplies() {
		return completionreplies;
	}

	/**
	 * Get Student must post discussions or replies
	 * 
	 * @return
	 */
	public int getCompletionposts() {
		return completionposts;
	}

	/**
	 * Get Course module id
	 * 
	 * @return
	 */
	public int getCmid() {
		return cmid;
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
	 * Get siteid
	 * 
	 * @return
	 */
	public long getSiteid() {
		return siteid;
	}

	/**
	 * Set siteid
	 * 
	 * @return
	 */
	public void setSiteid(long siteid) {
		this.siteid = siteid;
	}

}
