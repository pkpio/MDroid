package in.co.praveenkumar.mdroid.moodlemodel;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;
import com.orm.SugarRecord;
import com.orm.dsl.Ignore;

public class MoodleCourse extends SugarRecord<MoodleCourse> {

	// since id is a reserved field in SugarRecord
	@SerializedName("id")
	int courseid;

	@SerializedName("shortname")
	String shortname;

	@SerializedName("categoryid")
	int categoryid;

	@SerializedName("categorysortorder")
	int categorysortorder;

	@SerializedName("fullname")
	String fullname;

	@SerializedName("idnumber")
	String idnumber;

	@SerializedName("summary")
	String summary;

	@SerializedName("summaryformat")
	int summaryformat;

	@SerializedName("format")
	String format;

	@SerializedName("showgrades")
	int showgrades;

	@SerializedName("newsitems")
	int newsitems;

	@SerializedName("startdate")
	int startdate;

	@SerializedName("numsections")
	int numsections;

	@SerializedName("maxbytes")
	int maxbytes;

	@SerializedName("showreports")
	int showreports;

	@SerializedName("visible")
	int visible;

	@SerializedName("hiddensections")
	int hiddensections;

	@SerializedName("groupmode")
	int groupmode;

	@SerializedName("groupmodeforce")
	int groupmodeforce;

	@SerializedName("defaultgroupingid")
	int defaultgroupingid;

	@SerializedName("timecreated")
	int timecreated;

	@SerializedName("timemodified")
	int timemodified;

	@SerializedName("enablecompletion")
	int enablecompletion;

	@SerializedName("completionnotify")
	int completionnotify;

	@SerializedName("lang")
	String lang;

	@SerializedName("forcetheme")
	String forcetheme;

	@Ignore
	@SerializedName("courseformatoptions")
	ArrayList<MoodleCourseFormatOption> courseformatoptions;

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
	Boolean isUserCourse;

	public MoodleCourse() {

	}

	/**
	 * siteid is unique for a moodle address + user combination
	 * 
	 * @param siteid
	 *            siteid of the Moodle account to which this course belongs to.
	 */
	public MoodleCourse(long siteid) {
		this.siteid = siteid;
	}

	public int getCourseid() {
		return courseid;
	}

	public String getShortname() {
		return shortname;
	}

	public int getCategoryid() {
		return categoryid;
	}

	/**
	 * sort order into the category
	 * 
	 * @return
	 */
	public int getCategorysortorder() {
		return categorysortorder;
	}

	public String getFullname() {
		return fullname;
	}

	public String getIdnumber() {
		return idnumber;
	}

	public String getSummary() {
		return summary;
	}

	/**
	 * summary format (1 = HTML, 0 = MOODLE, 2 = PLAIN or 4 = MARKDOWN)
	 * 
	 * @return
	 */
	public int getSummaryformat() {
		return summaryformat;
	}

	/**
	 * course format: weeks, topics, social, site,..
	 * 
	 * @return
	 */
	public String getFormat() {
		return format;
	}

	/**
	 * 1 if grades are shown, otherwise 0
	 * 
	 * @return
	 */
	public int getShowgrades() {
		return showgrades;
	}

	/**
	 * number of recent items appearing on the course page
	 * 
	 * @return
	 */
	public int getNewsitems() {
		return newsitems;
	}

	/**
	 * timestamp when the course start
	 * 
	 * @return
	 */
	public int getStartdate() {
		return startdate;
	}

	/**
	 * (deprecated, use courseformatoptions) number of weeks/topics
	 * 
	 * @return
	 */
	public int getNumsections() {
		return numsections;
	}

	/**
	 * largest size of file that can be uploaded into the course
	 * 
	 * @return
	 */
	public int getMaxbytes() {
		return maxbytes;
	}

	/**
	 * are activity report shown (yes = 1, no =0)
	 * 
	 * @return
	 */
	public int getShowreports() {
		return showreports;
	}

	/**
	 * 1: available to student, 0:not available
	 * 
	 * @return
	 */
	public int getVisible() {
		return visible;
	}

	/**
	 * (deprecated, use courseformatoptions) How the hidden sections in the
	 * course are displayed to students
	 * 
	 * @return
	 */
	public int getHiddensections() {
		return hiddensections;
	}

	/**
	 * no group, separate, visible
	 * 
	 * @return
	 */
	public int getGroupmode() {
		return groupmode;
	}

	/**
	 * 1: yes, 0: no
	 * 
	 * @return
	 */
	public int getGroupmodeforce() {
		return groupmodeforce;
	}

	/**
	 * default grouping id
	 * 
	 * @return
	 */
	public int getDefaultgroupingid() {
		return defaultgroupingid;
	}

	/**
	 * timestamp when the course have been created
	 * 
	 * @return
	 */
	public int getTimecreated() {
		return timecreated;
	}

	/**
	 * timestamp when the course have been modified
	 * 
	 * @return
	 */
	public int getTimemodified() {
		return timemodified;
	}

	/**
	 * Enabled, control via completion and activity settings. Disbaled, not
	 * shown in activity settings.
	 * 
	 * @return
	 */
	public int getEnablecompletion() {
		return enablecompletion;
	}

	/**
	 * 1: yes 0: no
	 * 
	 * @return
	 */
	public int getCompletionnotify() {
		return completionnotify;
	}

	/**
	 * forced course language
	 * 
	 * @return
	 */
	public String getLang() {
		return lang;
	}

	/**
	 * name of the force theme
	 * 
	 * @return
	 */
	public String getForcetheme() {
		return forcetheme;
	}

	/**
	 * additional options for particular course format
	 * 
	 * @return
	 */
	public ArrayList<MoodleCourseFormatOption> getCourseformatoptions() {
		return courseformatoptions;
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
	 * Get if this course is enrolled by current user
	 * 
	 * @return
	 */
	public Boolean getIsUserCourse() {
		return isUserCourse;
	}

	/**
	 * Set if this course is enrolled by current user
	 * 
	 * @return
	 */
	public void setIsUserCourse(Boolean isUserCourse) {
		this.isUserCourse = isUserCourse;
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
	 * set the course id of this course
	 * 
	 * @param siteid
	 */
	public void setSiteid(long siteid) {
		this.siteid = siteid;
	}

}
