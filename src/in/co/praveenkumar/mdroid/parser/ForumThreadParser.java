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

import in.co.praveenkumar.mdroid.models.Reply;

import java.util.ArrayList;

public class ForumThreadParser {
	private ArrayList<Reply> replies = new ArrayList<Reply>();
	private String threadReplyId = "";

	public ForumThreadParser(String html) {
		int prevIndex = 0;
		int endIndex = 0;

		while (true) {
			Reply reply = new Reply();
			prevIndex = html.indexOf(" class=\"subject\">", prevIndex);
			if (prevIndex == -1)
				break;

			// for reply subject
			prevIndex += 17;
			endIndex = html.indexOf("</div>", prevIndex);
			reply.setSubject(html.substring(prevIndex, endIndex));

			// for reply author name
			prevIndex = endIndex;
			prevIndex = html.indexOf("<div class=\"author\">", prevIndex) + 22;
			prevIndex = html.indexOf("\">", prevIndex) + 2;
			endIndex = html.indexOf("</a>", prevIndex);
			reply.setAuthor(html.substring(prevIndex, endIndex));

			// for reply time
			prevIndex = endIndex;
			prevIndex = html.indexOf(", ", prevIndex) + 3;
			endIndex = html.indexOf("</div>", prevIndex);
			reply.setDate(html.substring(prevIndex, endIndex));

			// for reply content
			prevIndex = endIndex;
			prevIndex = html.indexOf("<div class=\"posting", prevIndex) + 19;
			prevIndex = html.indexOf("\">", prevIndex) + 2;
			endIndex = html.indexOf("</div><div class=\"", prevIndex);
			String tempThreadReplyContent = (html
					.substring(prevIndex, endIndex));
			tempThreadReplyContent = android.text.Html.fromHtml(
					tempThreadReplyContent).toString();
			reply.setContent(tempThreadReplyContent);

			replies.add(reply);
		}

		// Reply Id for user to reply. Id will be last post reply Id.
		prevIndex = html.lastIndexOf("forum/post.php?reply=") + 21;

		if (prevIndex != 20) {
			endIndex = html.indexOf("\"", prevIndex);
			threadReplyId = html.substring(prevIndex, endIndex);
		}

	}

	public ArrayList<Reply> getReplies() {
		return replies;
	}

	public String getReplyId() {
		return threadReplyId;
	}

}
