package in.co.praveenkumar.mdroid.moodlemodel;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;
import com.orm.SugarRecord;
import com.orm.dsl.Ignore;

public class MoodleSection extends SugarRecord<MoodleSection> {
	/**
	 * A section can be a week of contents or whatever the admin sets it.
	 */

	// since id is a reserved field in SugarRecord
	@SerializedName("id")
	int sectionid;

	@SerializedName("name")
	String name;

	@SerializedName("visible")
	int visible;

	@SerializedName("summary")
	String summary;

	@SerializedName("summaryformat")
	int summaryformat;

	@Ignore
	@SerializedName("modules")
	ArrayList<MoodleModule> modules;

	/**
	 * Section ID
	 * 
	 * @return
	 */
	public int getSectionid() {
		return sectionid;
	}

	/**
	 * Section name
	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * Is the section visible
	 * 
	 * @return
	 */
	public int getVisible() {
		return visible;
	}

	/**
	 * Section description
	 * 
	 * @return
	 */
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
	 * list of modules in this section
	 * 
	 * @return
	 */
	public ArrayList<MoodleModule> getModules() {
		return modules;
	}

}
