package in.co.praveenkumar.mdroid.task;

import in.co.praveenkumar.mdroid.model.MDroidNotification;
import in.co.praveenkumar.mdroid.model.MoodleDiscussion;
import in.co.praveenkumar.mdroid.model.MoodlePost;
import in.co.praveenkumar.mdroid.model.MoodlePosts;
import in.co.praveenkumar.mdroid.moodlerest.MoodleRestPost;

import java.util.ArrayList;
import java.util.List;

public class PostSyncTask {
	String mUrl;
	String token;
	long siteid;

	String error;
	Boolean notification;
	int notificationcount;

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
		this.notification = false;
		this.notificationcount = 0;
	}

	/**
	 * 
	 * @param mUrl
	 * @param token
	 * @param siteid
	 * @param notification
	 *            If true, sets notifications for new contents
	 * 
	 * @author Praveen Kumar Pendyala (praveen@praveenkumar.co.in)
	 */
	public PostSyncTask(String mUrl, String token, long siteid,
			Boolean notification) {
		this.mUrl = mUrl;
		this.token = token;
		this.siteid = siteid;
		this.notification = notification;
		this.notificationcount = 0;
	}

	/**
	 * Get the notifications count. Notifications should be enabled during
	 * Object instantiation.
	 * 
	 * @return notificationcount
	 */
	public int getNotificationcount() {
		return notificationcount;
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

				dbPosts = MoodlePost.find(MoodlePost.class,
						"postid = ? and siteid = ?", post.getPostid() + "",
						siteid + "");
				if (dbPosts.size() > 0)
					post.setId(dbPosts.get(0).getId());

				// set notifications if enabled
				else if (notification) {
					List<MoodleDiscussion> dbDiscussions = MoodleDiscussion
							.find(MoodleDiscussion.class,
									"discussionid = ? and siteid = ?", siteid
											+ "", discussionid + "");
					MoodleDiscussion discussion = (dbDiscussions != null && dbDiscussions
							.size() > 0) ? dbDiscussions.get(0) : null;

					if (discussion != null) {
						new MDroidNotification(siteid,
								MDroidNotification.TYPE_FORUM_REPLY,
								"New forum reply from "
										+ post.getUserfullname(),
								"New reply in " + discussion.getName()).save();
						notificationcount++;
					}
				}
				post.save();
			}

		return true;
	}

	/**
	 * Sync all topics in the list of discussions.
	 * 
	 * Note: Moodle doesn't support fetching of posts from more than one
	 * discussion at a time so, this is realized using multiple calls - one per
	 * discussionid.
	 * 
	 * @return syncStatus
	 * 
	 * @author Praveen Kumar Pendyala (praveen@praveenkumar.co.in)
	 */
	public Boolean syncPosts(ArrayList<Integer> discussionids) {
		Boolean status = true;

		if (discussionids == null || discussionids.size() == 0)
			return false;

		for (int i = 0; i < discussionids.size(); i++)
			status = status & syncPosts(discussionids.get(i));

		return status;
	}
}
