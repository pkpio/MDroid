/*
 * Author: 	Praveen Kumar Pendyala
 * Project: MDroid
 * Created:	28-12-2013
 * 
 * © 2013, Praveen Kumar Pendyala. 
 * Licensed under the Creative Commons Attribution-NonCommercial-ShareAlike 
 * 3.0 Unported license, http://creativecommons.org/licenses/by-nc-sa/3.0/ 
 * 
 * This is a part of the MDroid project. You may use the contents of this file
 * or the project only if you comply with license of the project, available at the 
 * Github repo: https://github.com/praveendath92/MDroid/blob/master/README.md
 * 
 */

package in.co.praveenkumar.mdroid.parser;

import in.co.praveenkumar.mdroid.models.Course;

import java.util.ArrayList;

public class CoursesParser {
	private ArrayList<Course> courses = new ArrayList<Course>();
	private String uName = "";

	public CoursesParser(String html) {
		int prevIndex = 0;
		int endIndex = 0;

		// Get user name
		prevIndex = html.indexOf("You are logged in as ", prevIndex);
		prevIndex = html.indexOf("\">", prevIndex) + 2;
		endIndex = html.indexOf("</a>", prevIndex);
		uName = (html.substring(prevIndex, endIndex));

		while (true) {
			Course course = new Course();
			prevIndex = html.indexOf(
					"<a title=\"Click to enter this course\" href=\"",
					prevIndex);
			if (prevIndex == -1)
				break;
			prevIndex += 44;
			prevIndex = html.indexOf("/course/view.php?id=", prevIndex) + 20;
			endIndex = html.indexOf('\"', prevIndex);
			course.setId(html.substring(prevIndex, endIndex));

			prevIndex = endIndex + 2;
			endIndex = html.indexOf("</a>", prevIndex);
			course.setName(html.substring(prevIndex, endIndex));

			courses.add(course);
		}
	}

	public ArrayList<Course> getCourses() {
		return courses;
	}

	public String getUserName() {
		return uName;
	}

}
