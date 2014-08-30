package in.co.praveenkumar.mdroid.moodlemodel;

import java.util.ArrayList;

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
	
}
