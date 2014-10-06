package in.co.praveenkumar.mdroid.task;

import in.co.praveenkumar.mdroid.moodlemodel.MoodlePost;
import in.co.praveenkumar.mdroid.moodlemodel.MoodlePosts;
import in.co.praveenkumar.mdroid.moodlerest.MoodleRestPost;

import java.util.ArrayList;
import java.util.List;

public class PostSyncTask {
	String mUrl;
	String token;
	long siteid;
	String error;

	/**
	 * 
	 * @param mUrl
	 * @param token
	 * @param siteid
	 * 
	 * @author Praveen Kumar Pendyala (praveen@praveenkumar.co.in)
	 */
	public PostSyncTask(String mUrl, String token, long siteid) {
		this.mUrl = mUrl;
		this.token = token;
		this.siteid = siteid;
	}

	/**
	 * Sync all topics in a discussion.
	 * 
	 * @return syncStatus
	 * 
	 * @author Praveen Kumar Pendyala (praveen@praveenkumar.co.in)
	 */
	public Boolean syncPosts(int discussionid) {
		MoodleRestPost mrp = new MoodleRestPost(mUrl, token);
		MoodlePosts moodlePosts = mrp.getPosts(discussionid);

		/** Error checking **/
		// Some network or encoding issue.
		if (moodlePosts == null) {
			error = "Network issue!";
			return false;
		}

		// Moodle exception
		if (moodlePosts.getErrorcode() != null) {
			error = moodlePosts.getErrorcode();
			// No additional debug info as that needs context
			return false;
		}

		ArrayList<MoodlePost> mPosts = moodlePosts.getPosts();
		// Warnings are not being handled
		List<MoodlePost> dbPosts;
		MoodlePost post = new MoodlePost();

		if (mPosts != null)
			for (int i = 0; i < mPosts.size(); i++) {
				post = mPosts.get(i);
				post.setSiteid(siteid);
				/*
				 * -TODO- Improve this search with only Sql operation
				 */
				dbPosts = MoodlePost.find(MoodlePost.class,
						"postid = ? and siteid = ?", post.getPostid() + "",
						siteid + "");
				if (dbPosts.size() > 0)
					post.setId(dbPosts.get(0).getId());
				post.save();
			}

		return true;
	}
}
