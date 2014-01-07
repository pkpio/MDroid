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

import java.util.ArrayList;

public class CoursesParser {
	private ArrayList<String> courseNames = new ArrayList<String>();
	private ArrayList<String> courseIds = new ArrayList<String>();
	private int courseCount = 0;
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
			prevIndex = html.indexOf(
					"<a title=\"Click to enter this course\" href=\"",
					prevIndex);
			if (prevIndex == -1)
				break;
			prevIndex += 44;
			prevIndex = html.indexOf("/course/view.php?id=", prevIndex) + 20;
			endIndex = html.indexOf('\"', prevIndex);
			courseIds.add(html.substring(prevIndex, endIndex));

			prevIndex = endIndex + 2;
			endIndex = html.indexOf("</a>", prevIndex);
			courseNames.add(html.substring(prevIndex, endIndex));

			courseCount++;
		}
	}

	public ArrayList<String> getCourseNames() {
		return courseNames;
	}

	public ArrayList<String> getCourseIds() {
		return courseIds;
	}

	public int getCourseCount() {
		return courseCount;
	}

	public String getUserName() {
		return uName;
	}

}
