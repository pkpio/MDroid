package in.co.praveenkumar.mdroid.helper;

public class AppInterface {

	/**
	 * Gives an interface for changing the state of the app drawers
	 * 
	 * @author Praveen Kumar Pendyala (praveen@praveenkumar.co.in)
	 * 
	 */
	public interface DrawerStateInterface {
		/**
		 * Set navigation drawers state
		 * 
		 * @param state
		 *            True: open. False: close
		 */
		public void setDrawerState(Boolean state);
	}

	/**
	 * Gives an interface for passing forumid between activity and fragments.
	 * 
	 * Useful in Discussion fragment and discussion activity
	 * 
	 * @author Praveen Kumar Pendyala (praveen@praveenkumar.co.in)
	 * 
	 */
	public interface ForumIdInterface {
		/**
		 * Get the forumid
		 * 
		 * @return forumid
		 */
		public int getForumId();

	}

	/**
	 * Gives an interface for passing discussionid between activity and
	 * fragments.
	 * 
	 * Useful in Post fragment and post activity
	 * 
	 * @author Praveen Kumar Pendyala (praveen@praveenkumar.co.in)
	 * 
	 */
	public interface DiscussionIdInterface {
		/**
		 * Get the discussionid
		 * 
		 * @return discussionid
		 */
		public int getDiscussionId();

	}

	/**
	 * Gives an interface for passing userid between activity and fragments.
	 * 
	 * Useful in Messaging fragment, MessageListing fragment and Messaging
	 * activity
	 * 
	 * @author Praveen Kumar Pendyala (praveen@praveenkumar.co.in)
	 * 
	 */
	public interface UserIdInterface {
		/**
		 * Get the userid of current selected user
		 * 
		 * @return userid
		 */
		public int getUserId();

		/**
		 * Set the userid of current selected user
		 * 
		 * @param userid
		 */
		public void setUserId(int userid);

	}

	/**
	 * Gives an interface for passing actionbar title between activity and
	 * fragments.
	 * 
	 * @author Praveen Kumar Pendyala (praveen@praveenkumar.co.in)
	 * 
	 */
	public interface TitleInterface {
		/**
		 * Get the activity title
		 * 
		 * @return title
		 */
		public String getTitle();

	}

}
