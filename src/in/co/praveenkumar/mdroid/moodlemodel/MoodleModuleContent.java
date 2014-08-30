package in.co.praveenkumar.mdroid.moodlemodel;

import com.google.gson.annotations.SerializedName;
import com.orm.SugarRecord;

public class MoodleModuleContent extends SugarRecord<MoodleModuleContent> {

	@SerializedName("type")
	String type;

	@SerializedName("filename")
	String filename;

	@SerializedName("filepath")
	String filepath;

	@SerializedName("filesize")
	int filesize;

	@SerializedName("fileurl")
	String fileurl;

	@SerializedName("content")
	String content;

	@SerializedName("timecreated")
	int timecreated;

	@SerializedName("timemodified")
	int timemodified;

	@SerializedName("sortorder")
	int sortorder;

	@SerializedName("userid")
	int userid;

	@SerializedName("author")
	String author;

	@SerializedName("license")
	String license;

	// Relational parameters
	Long parentid;
	int moduleid;
	int sectionid;
	int courseid;
	int siteid;

	/**
	 * Get the database id of the parent module. Not to be confused with actual
	 * moduleid given to a module by Moodle site. This id is given by Sugar db
	 * while saving the parent module
	 * 
	 * @return
	 */
	public Long getParentid() {
		return parentid;
	}

	/**
	 * moduleid of the module to which this module content belongs to. This id
	 * is given to a module by Moodle site.
	 * 
	 * @return
	 */
	public int getModuleid() {
		return moduleid;
	}

	/**
	 * sectionid of the section to which this module content content belongs to.
	 * This id is given to a section by Moodle site.
	 * 
	 * @return
	 */
	public int getSectionid() {
		return sectionid;
	}

	/**
	 * courseid of the course to which this section belongs to. This id is given
	 * to a course by Moodle site
	 * 
	 * @return
	 */
	public int getCourseid() {
		return courseid;
	}

	/**
	 * Get the siteid of the Moodle site to which this section belong to. siteid
	 * is given to an user account by sugar db on successful login
	 * 
	 * @return
	 */
	public int getSiteid() {
		return siteid;
	}

}
