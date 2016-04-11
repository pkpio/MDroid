package in.co.praveenkumar.mdroid.model;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;
import com.orm.SugarRecord;
import com.orm.dsl.Ignore;

public class MoodleModule extends SugarRecord<MoodleModule> {
	/**
	 * Note: The name Module is interchangeably used as Activity
	 */

	@SerializedName("id")
	int moduleid;

	@SerializedName("url")
	String url;

	@SerializedName("name")
	String name;

	@SerializedName("description")
	String description;

	@SerializedName("visible")
	int visible;

	@SerializedName("modicon")
	String modicon;

	@SerializedName("modname")
	String modname;

	@SerializedName("modplural")
	String modplural;

	@SerializedName("availablefrom")
	int availablefrom;

	@SerializedName("availableuntil")
	int availableuntil;

	@SerializedName("indent")
	int indent;

	@Ignore
	@SerializedName("contents")
	ArrayList<MoodleModuleContent> contents;

	// Relational parameters
	Long parentid;
	int sectionid;
	int courseid;
	Long siteid;

	/**
	 * module or activity id
	 * 
	 * @return
	 */
	public int getModuleid() {
		return moduleid;
	}

	/**
	 * module or activity url
	 * 
	 * @return
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * activity module name
	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * activity description
	 * 
	 * @return
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * is the module visible
	 * 
	 * @return
	 */
	public int getVisible() {
		return visible;
	}

	/**
	 * activity icon url
	 * 
	 * @return
	 */
	public String getModicon() {
		return modicon;
	}

	/**
	 * activity module type
	 * 
	 * @return
	 */
	public String getModname() {
		return modname;
	}

	/**
	 * activity module plural name
	 * 
	 * @return
	 */
	public String getModplural() {
		return modplural;
	}

	/**
	 * module availability start date
	 * 
	 * @return
	 */
	public int getAvailablefrom() {
		return availablefrom;
	}

	/**
	 * module availability end date
	 * 
	 * @return
	 */
	public int getAvailableuntil() {
		return availableuntil;
	}

	/**
	 * number of identation in the site
	 * 
	 * @return
	 */
	public int getIndent() {
		return indent;
	}

	/**
	 * List of contents
	 * 
	 * @return
	 */
	public ArrayList<MoodleModuleContent> getContents() {
		return contents;
	}

	/**
	 * Set list of contents <br/>
	 * Used when getting course contents from database
	 * 
	 * @return
	 */
	public void setContents(List<MoodleModuleContent> contents) {
		this.contents = new ArrayList<>(contents);
	}

	/**
	 * Get the database id of the parent section. Not to be confused with actual
	 * sectionid given to a section from Moodle site. This id is given by Sugar
	 * db while saving the parent section
	 * 
	 * @return
	 */
	public Long getParentid() {
		return parentid;
	}

	/**
	 * sectionid of the section to which this Module belongs to. This id is
	 * given to a section by Moodle site.
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
	public Long getSiteid() {
		return siteid;
	}

	/**
	 * Set the module parent section db id
	 * 
	 * @param parentid
	 */
	public void setParentid(Long parentid) {
		this.parentid = parentid;
	}

	/**
	 * Set the module course Moodle id
	 * 
	 * @param courseid
	 */
	public void setCourseid(int courseid) {
		this.courseid = courseid;
	}

	/**
	 * Set the module parent section Moodle id
	 * 
	 * @param section
	 *            .id
	 */
	public void setSectionid(int sectionid) {
		this.sectionid = sectionid;
	}

	/**
	 * Set the siteid to which this module belong to.
	 * 
	 * @param siteid
	 */
	public void setSiteid(Long siteid) {
		this.siteid = siteid;
	}

}
