package in.co.praveenkumar.mdroid.parser;

import java.util.ArrayList;

public class ForumThreadParser {
	private ArrayList<String> threadPostSubs = new ArrayList<String>();
	private ArrayList<String> threadPostAuthors = new ArrayList<String>();
	private ArrayList<String> threadPostDates = new ArrayList<String>();
	private ArrayList<String> threadPostContent = new ArrayList<String>();
	private int nThreadPosts = 0;

	public ForumThreadParser(String html) {
		int prevIndex = 0;
		int endIndex = 0;
		while (true) {
			prevIndex = html.indexOf(" class=\"subject\">", prevIndex);
			if (prevIndex == -1)
				break;

			// for reply subject
			prevIndex += 17;
			endIndex = html.indexOf("</div>", prevIndex);
			threadPostSubs.add(html.substring(prevIndex, endIndex));

			// for reply author name
			prevIndex = endIndex;
			prevIndex = html.indexOf("<div class=\"author\">", prevIndex) + 22;
			prevIndex = html.indexOf("\">", prevIndex) + 2;
			endIndex = html.indexOf("</a>", prevIndex);
			threadPostAuthors.add(html.substring(prevIndex, endIndex));

			// for reply time
			prevIndex = endIndex;
			prevIndex = html.indexOf(", ", prevIndex) + 3;
			endIndex = html.indexOf("</div>", prevIndex);
			threadPostDates.add(html.substring(prevIndex, endIndex));

			// for reply content
			prevIndex = endIndex;
			prevIndex = html.indexOf("<div class=\"posting", prevIndex) + 19;
			prevIndex = html.indexOf("\">", prevIndex) + 2;
			endIndex = html.indexOf("</div><div class=\"", prevIndex);
			String tempThreadReplyContent = (html
					.substring(prevIndex, endIndex));
			tempThreadReplyContent = android.text.Html.fromHtml(
					tempThreadReplyContent).toString();
			threadPostContent.add(tempThreadReplyContent);

			nThreadPosts++;
		}
	}

	public ArrayList<String> getPostSubjects() {
		return threadPostSubs;
	}

	public ArrayList<String> getPostAuthors() {
		return threadPostAuthors;
	}

	public ArrayList<String> getPostDates() {
		return threadPostDates;
	}

	public ArrayList<String> getPostContents() {
		return threadPostContent;
	}

	public int getPostsCount() {
		return nThreadPosts;
	}

}
