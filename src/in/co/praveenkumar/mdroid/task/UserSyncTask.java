package in.co.praveenkumar.mdroid.task;

import in.co.praveenkumar.mdroid.model.MoodleUser;
import in.co.praveenkumar.mdroid.moodlerest.MoodleRestUser;

import java.util.List;

public class UserSyncTask {
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
	public UserSyncTask(String mUrl, String token, long siteid) {
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
	public Boolean syncUsers(int courseid) {
		MoodleRestUser mru = new MoodleRestUser(mUrl, token);
		List<MoodleUser> mUsers = mru.getUsers(courseid);

		/** Error checking **/
		// Some network or encoding issue.
		if (mUsers == null || mUsers.size() == 0) {
			error = "No users found!";
			return false;
		}

		List<MoodleUser> dbUsers;
		MoodleUser mUser = new MoodleUser();
		for (int i = 0; i < mUsers.size(); i++) {
			mUser = mUsers.get(i);
			mUser.setSiteid(siteid);
			mUser.setCourseid(courseid);

			dbUsers = MoodleUser.find(MoodleUser.class,
					"userid = ? and siteid = ? and courseid = ?",
					mUser.getUserid() + "", siteid + "", courseid + "");
			if (dbUsers.size() > 0)
				mUser.setId(dbUsers.get(0).getId());
			mUser.save();
		}

		return true;
	}
}
