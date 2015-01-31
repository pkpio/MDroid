package in.co.praveenkumar.mdroid.model;

import com.google.gson.annotations.SerializedName;
import com.orm.SugarRecord;

public class MoodleDiscussion extends SugarRecord<MoodleDiscussion> {

	// since id is a reserved field in SugarRecord
	@SerializedName("id")
	int discussionid;

	@SerializedName("course")
	int courseid;

	@SerializedName("forum")
	int forumid;

	@SerializedName("name")
	String name;

	@SerializedName("userid")
	int userid;

	@SerializedName("groupid")
	int groupid;

	@SerializedName("assessed")
	int assessed;

	@SerializedName("timemodified")
	int timemodified;

	@SerializedName("usermodified")
	int usermodified;

	@SerializedName("timestart")
	int timestart;

	@SerializedName("timeend")
	int timeend;

	@SerializedName("firstpost")
	int firstpost;

	@SerializedName("firstuserfullname")
	String firstuserfullname;

	@SerializedName("firstuserimagealt")
	String firstuserimagealt;

	@SerializedName("firstuserpicture")
	int firstuserpicture;

	@SerializedName("firstuseremail")
	String firstuseremail;

	@SerializedName("subject")
	String subject;

	@SerializedName("numreplies")
	String numreplies;

	@SerializedName("numunread")
	String numunread;

	@SerializedName("lastpost")
	int lastpost;

	@SerializedName("lastuserid")
	int lastuserid;

	@SerializedName("lastuserfullname")
	String lastuserfullname;

	@SerializedName("lastuserimagealt")
	String lastuserimagealt;

	@SerializedName("lastuserpicture")
	int lastuserpicture;

	@SerializedName("lastuseremail")
	String lastuseremail;

	// Relational and other fields
	long siteid;

	/**
	 * Forum id
	 * 
	 * @return
	 */
	public int getDiscussionid() {
		return discussionid;
	}

	/**
	 * Course id
	 * 
	 * @return
	 */
	public int getCourseid() {
		return courseid;
	}

	/**
	 * The forum id. Note sure how this is different from forum id
	 * 
	 * @return
	 */
	public int getForumid() {
		return forumid;
	}

	/**
	 * Discussion name
	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * 
	 * @return User id
	 */
	public int getUserid() {
		return userid;
	}

	/**
	 * 
	 * @return Group id
	 */
	public int getGroupid() {
		return groupid;
	}

	/**
	 * Is this assessed?
	 * 
	 * @return
	 */
	public int getAssessed() {
		return assessed;
	}

	/**
	 * Time modified
	 * 
	 * @return
	 */
	public int getTimemodified() {
		return timemodified;
	}

	/**
	 * The id of the user who last modified
	 * 
	 * @return
	 */
	public int getUsermodified() {
		return usermodified;
	}

	/**
	 * Time discussion can start
	 * 
	 * @return
	 */
	public int getTimestart() {
		return timestart;
	}

	/**
	 * Time discussion ends
	 * 
	 * @return
	 */
	public int getTimeend() {
		return timeend;
	}

	/**
	 * The first post in the discussion
	 * 
	 * @return
	 */
	public int getFirstpost() {
		return firstpost;
	}

	/**
	 * The discussion creators fullname
	 * 
	 * @return
	 */
	public String getFirstuserfullname() {
		return firstuserfullname;
	}

	/**
	 * The discussion creators image alt
	 * 
	 * @return
	 */
	public String getFirstuserimagealt() {
		return firstuserimagealt;
	}

	/**
	 * The discussion creators profile picture
	 * 
	 * @return
	 */
	public int getFirstuserpicture() {
		return firstuserpicture;
	}

	/**
	 * The discussion creators email
	 * 
	 * @return
	 */
	public String getFirstuseremail() {
		return firstuseremail;
	}

	/**
	 * The discussion subject
	 * 
	 * @return
	 */
	public String getSubject() {
		return subject;
	}

	/**
	 * The number of replies in the discussion
	 * 
	 * @return
	 */
	public String getNumreplies() {
		return numreplies;
	}

	/**
	 * The number of unread posts, blank if this value is not available due to
	 * forum settings.
	 * 
	 * @return
	 */
	public String getNumunread() {
		return numunread;
	}

	/**
	 * The id of the last post in the discussion
	 * 
	 * @return
	 */
	public int getLastpost() {
		return lastpost;
	}

	/**
	 * The id of the user who made the last post
	 * 
	 * @return
	 */
	public int getLastuserid() {
		return lastuserid;
	}

	/**
	 * The last person to posts fullname
	 * 
	 * @return
	 */
	public String getLastuserfullname() {
		return lastuserfullname;
	}

	/**
	 * The last person to posts image alt
	 * 
	 * @return
	 */
	public String getLastuserimagealt() {
		return lastuserimagealt;
	}

	/**
	 * The last person to posts profile picture
	 * 
	 * @return
	 */
	public int getLastuserpicture() {
		return lastuserpicture;
	}

	/**
	 * The last person to posts email
	 * 
	 * @return
	 */
	public String getLastuseremail() {
		return lastuseremail;
	}

	/**
	 * get the site id of this discussion
	 * 
	 * @param siteid
	 */
	public long getSiteid() {
		return siteid;
	}

	/**
	 * set the site id
	 * 
	 * @param siteid
	 */
	public void setSiteid(long siteid) {
		this.siteid = siteid;
	}

}
