/**************************************************************
 * Author: 	Praveen Kumar Pendyala
 * Project: MDroid
 * Created: 31st May, 2014.
 * License: http://creativecommons.org/licenses/by-nc-sa/3.0/
 **************************************************************/

package in.co.praveenkumar.mdroid.models;

import java.util.ArrayList;

public class MoodleCourseSection {
	String id;
	String name;
	String summary;
	String summaryformat;
	ArrayList<MoodleCourseModule> modules;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getSummaryformat() {
		return summaryformat;
	}

	public void setSummaryformat(String summaryformat) {
		this.summaryformat = summaryformat;
	}

	public ArrayList<MoodleCourseModule> getModules() {
		return modules;
	}

	public void setModules(ArrayList<MoodleCourseModule> modules) {
		this.modules = modules;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
