package in.co.praveenkumar.mdroid.moodlemodel;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;
import com.orm.SugarRecord;

public class MoodleCourse extends SugarRecord<MoodleCourse> {
	/**
	 * Note: id is a reserved field in SugarRecord so, we use courseid and use
	 * Serialize in Gson for proper object conversion
	 */
	@SerializedName("id")
	int courseid;
	String shortname;
	int categoryid;
	int categorysortorder;
	String fullname;
	String idnumber;
	String summary;
	int summaryformat;
	String format;
	int showgrades;
	int newsitems;
	int startdate;
	int numsections;
	int maxbytes;
	int showreports;
	int visible;
	int hiddensections;
	int groupmode;
	int groupmodeforce;
	int defaultgroupingid;
	int timecreated;
	int timemodified;
	int enablecompletion;
	int completionnotify;
	String lang;
	String forcetheme;
	ArrayList<MoodleCourseFormatOption> courseformatoptions;

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

}
