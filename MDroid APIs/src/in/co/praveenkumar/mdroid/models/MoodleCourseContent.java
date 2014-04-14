/**************************************************************
 * Author: 	Praveen Kumar Pendyala
 * Project: MDroid
 * Created: 31st May, 2014.
 * License: http://creativecommons.org/licenses/by-nc-sa/3.0/
 **************************************************************/

package in.co.praveenkumar.mdroid.models;

import java.util.ArrayList;

public class MoodleCourseContent {
	ArrayList<MoodleCourseSection> sections = new ArrayList<MoodleCourseSection>();

	public ArrayList<MoodleCourseSection> getSections() {
		return sections;
	}

	public void setSections(ArrayList<MoodleCourseSection> sections) {
		this.sections = sections;
	}

	public void addSection(MoodleCourseSection section) {
		sections.add(section);
	}

	public MoodleCourseSection getSection(int index) {
		if (index < sections.size())
			return sections.get(index);
		else
			return null;
	}

}
