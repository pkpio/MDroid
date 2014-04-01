package in.co.praveenkumar.mdroid.models;

import java.util.ArrayList;

public class MoodleCourseContent {
	ArrayList<MoodleCourseSection> sections;

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
