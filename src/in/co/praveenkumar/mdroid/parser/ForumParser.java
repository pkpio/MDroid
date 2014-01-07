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

public class ForumParser {
	private ArrayList<String> forumThreadIds = new ArrayList<String>();
	private ArrayList<String> forumThreadSubs = new ArrayList<String>();
	private ArrayList<String> forumThreadReplyCounts = new ArrayList<String>();
	private ArrayList<String> forumThreadAuthors = new ArrayList<String>();
	private ArrayList<String> forumThreadDates = new ArrayList<String>();
	private int nForumThreads = 0;

	public String getForumId(String html) {
		String fId = "";
		int prevIndex = 0;
		int endIndex = 0;

		while (true) {
			prevIndex = html.indexOf("<a href=\"view.php?f=", prevIndex);
			if (prevIndex == -1)
				break;
			prevIndex += 20;
			endIndex = html.indexOf("\"", prevIndex);
			fId = html.substring(prevIndex, endIndex);
		}

		return fId;
	}

	public void ParseForumForData(String html) {
		int prevIndex = 0;
		int endIndex = 0;

		while (true) {
			prevIndex = html.indexOf("class=\"topic starter\"><a href=\"",
					prevIndex);
			if (prevIndex == -1)
				break;

			// for Post ID
			prevIndex += 31;
			endIndex = html.indexOf("\"", prevIndex);
			forumThreadIds.add(html.substring(prevIndex, endIndex));

			// for post subject
			prevIndex = endIndex + 2;
			endIndex = html.indexOf("</a>", prevIndex);
			String textConvertedhtml = html.substring(prevIndex, endIndex);
			textConvertedhtml = android.text.Html.fromHtml(textConvertedhtml)
					.toString();
			forumThreadSubs.add(textConvertedhtml);

			// for post Author
			prevIndex = endIndex;
			prevIndex = html.indexOf("<td class=\"author\"><a href=\"",
					prevIndex) + 28;
			prevIndex = html.indexOf("\">", prevIndex) + 2;
			endIndex = html.indexOf("</a>", prevIndex);
			forumThreadAuthors.add(html.substring(prevIndex, endIndex));

			// for post replies count
			prevIndex = endIndex;
			prevIndex = html.indexOf("<td class=\"replies\"><a href=\"",
					prevIndex) + 29;
			prevIndex = html.indexOf("\">", prevIndex) + 2;
			endIndex = html.indexOf("</a>", prevIndex);
			forumThreadReplyCounts.add(html.substring(prevIndex, endIndex));

			// for post last reply time
			prevIndex = endIndex;
			prevIndex = html.indexOf("<td class=\"lastpost\"><a href=\"",
					prevIndex) + 30;
			prevIndex = html.indexOf("\">", prevIndex) + 2;
			prevIndex = html.indexOf("\">", prevIndex) + 2;
			prevIndex = html.indexOf(",", prevIndex) + 2;
			endIndex = html.indexOf("</a>", prevIndex);
			forumThreadDates.add(html.substring(prevIndex, endIndex));

			// Increase count
			nForumThreads++;
		}
	}

	public ArrayList<String> getThreadSubjects() {
		return forumThreadSubs;
	}

	public ArrayList<String> getThreadAuthors() {
		return forumThreadAuthors;
	}

	public ArrayList<String> getThreadIds() {
		return forumThreadIds;
	}

	public ArrayList<String> getThreadReplyCounts() {
		return forumThreadReplyCounts;
	}

	public ArrayList<String> getThreadTimes() {
		return forumThreadDates;
	}

	public int getThreadsCount() {
		return nForumThreads;
	}
}
