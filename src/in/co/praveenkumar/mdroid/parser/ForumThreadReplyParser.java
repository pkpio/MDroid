/*
 * Author: 	Praveen Kumar Pendyala
 * Project: MDroid
 * Created:	14-01-2014
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

public class ForumThreadReplyParser {
	private String subscribe = "1";
	private String timestart = "0";
	private String timeend = "0";
	private String course = ""; // To be found
	private String forum = "0";
	private String discussion = ""; // To be found
	private String parent = ""; // To be found
	private String userid = ""; // To be found
	private String groupid = "0";
	private String edit = "0";
	private String reply = ""; // To be found
	private String sesskey = ""; // To be found
	private String _qf__mod_forum_post_form = "1";

	private String subject = ""; // To be found
	private String messageFormat = "1";
	private String messageItemId = ""; // To be found
	private String attachments = ""; // To be found

	public ForumThreadReplyParser(String html) {
		int prevIndex = 0;
		int endIndex = 0;

		// Course
		prevIndex = html.indexOf("<input name=\"course\"") + 20;
		prevIndex = html.indexOf("value=\"", prevIndex) + 7;
		endIndex = html.indexOf("\"", prevIndex);
		course = html.substring(prevIndex, endIndex);

		// Discussion
		prevIndex = html.indexOf("<input name=\"discussion\"") + 24;
		prevIndex = html.indexOf("value=\"", prevIndex) + 7;
		endIndex = html.indexOf("\"", prevIndex);
		discussion = html.substring(prevIndex, endIndex);

		// Parent
		prevIndex = html.indexOf("<input name=\"parent\"") + 20;
		prevIndex = html.indexOf("value=\"", prevIndex) + 7;
		endIndex = html.indexOf("\"", prevIndex);
		parent = html.substring(prevIndex, endIndex);

		// userid
		prevIndex = html.indexOf("<input name=\"userid\"") + 20;
		prevIndex = html.indexOf("value=\"", prevIndex) + 7;
		endIndex = html.indexOf("\"", prevIndex);
		userid = html.substring(prevIndex, endIndex);

		// reply
		prevIndex = html.indexOf("<input name=\"reply\"") + 19;
		prevIndex = html.indexOf("value=\"", prevIndex) + 7;
		endIndex = html.indexOf("\"", prevIndex);
		reply = html.substring(prevIndex, endIndex);

		// sesskey
		prevIndex = html.indexOf("<input name=\"sesskey\"") + 21;
		prevIndex = html.indexOf("value=\"", prevIndex) + 7;
		endIndex = html.indexOf("\"", prevIndex);
		sesskey = html.substring(prevIndex, endIndex);

		// subject
		prevIndex = html.indexOf("name=\"subject\"") + 14;
		prevIndex = html.indexOf("value=\"", prevIndex) + 7;
		endIndex = html.indexOf("\"", prevIndex);
		subject = html.substring(prevIndex, endIndex);

		// messageItemId
		prevIndex = html.indexOf("name=\"message[itemid]\"") + 22;
		prevIndex = html.indexOf("value=\"", prevIndex) + 7;
		endIndex = html.indexOf("\"", prevIndex);
		messageItemId = html.substring(prevIndex, endIndex);

		// attachments
		prevIndex = html.indexOf("<input value=", prevIndex) + 13;
		endIndex = html.indexOf("\"", prevIndex);
		attachments = html.substring(prevIndex, endIndex);
		int i =2;
	}

}
