/**************************************************************
 * Author: 	Praveen Kumar Pendyala
 * Project: MDroid
 * Created: 31st May, 2014.
 * License: http://creativecommons.org/licenses/by-nc-sa/3.0/
 **************************************************************/

package in.co.praveenkumar.mdroid.models;

import java.util.ArrayList;

public class MoodleCourseModule {
	String id;
	String name;
	String url;
	String modname;
	ArrayList<MoodleFile> files;
	String sectionid;
	String sectionname;
	String description;
	Boolean hasFiles;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getModname() {
		return modname;
	}

	public void setModname(String modname) {
		this.modname = modname;
	}

	public ArrayList<MoodleFile> getFiles() {
		return files;
	}

	public void setFiles(ArrayList<MoodleFile> files) {
		this.files = files;
	}

	public String getSectionid() {
		return sectionid;
	}

	public void setSectionid(String sectionid) {
		this.sectionid = sectionid;
	}

	public String getSectionname() {
		return sectionname;
	}

	public void setSectionname(String sectionname) {
		this.sectionname = sectionname;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Boolean getHasFiles() {
		return hasFiles;
	}

	public void setHasFiles(Boolean hasFiles) {
		this.hasFiles = hasFiles;
	}

}
