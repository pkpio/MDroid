package in.co.praveenkumar.mdroid.models;

import java.util.ArrayList;

public class MoodleCourseModule {
	String id;
	String name;
	String url;
	String modname;
	ArrayList<MoodleFile> files;

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

}
