package in.co.praveenkumar.mdroid.model;

import com.google.gson.annotations.SerializedName;
import com.orm.SugarRecord;

public class MoodlePost extends SugarRecord<MoodlePost> {

	// since id is a reserved field in SugarRecord
	@SerializedName("id")
	int postid;

	@SerializedName("discussion")
	int discussionid;

	@SerializedName("parent")
	int parentid;

	@SerializedName("userid")
	int userid;

	@SerializedName("created")
	int created;

	@SerializedName("modified")
	int modified;

	@SerializedName("mailed")
	int mailed;

	@SerializedName("subject")
	String subject;

	@SerializedName("message")
	String message;

	@SerializedName("messageformat")
	int messageformat;

	@SerializedName("messagetrust")
	int messagetrust;

	@SerializedName("attachment")
	String attachment;

	@SerializedName("totalscore")
	int totalscore;

	@SerializedName("mailnow")
	int mailnow;

	/*
	 * The below two fields - canreply and postread are indicated as int in the
	 * moodle Web services API Documentation but however the json response is
	 * given as Boolean for both.
	 */
	@SerializedName("canreply")
	Boolean canreply;

	@SerializedName("postread")
	Boolean postread;

	@SerializedName("userfullname")
	String userfullname;

	// Relational and other fields
	long siteid;

	/**
	 * Post id
	 * 
	 * @return
	 */
	public int getPostid() {
		return postid;
	}

	/**
	 * Discussion id
	 * 
	 * @return
	 */
	public int getDiscussionid() {
		return discussionid;
	}

	/**
	 * Parent id
	 * 
	 * @return
	 */
	public int getParentid() {
		return parentid;
	}

	/**
	 * User id
	 * 
	 * @return
	 */
	public int getUserid() {
		return userid;
	}

	/**
	 * Creation time
	 * 
	 * @return
	 */
	public int getCreated() {
		return created;
	}

	/**
	 * Time modified
	 * 
	 * @return
	 */
	public int getModified() {
		return modified;
	}

	/**
	 * Mailed?
	 * 
	 * @return
	 */
	public int getMailed() {
		return mailed;
	}

	/**
	 * The post subject
	 * 
	 * @return
	 */
	public String getSubject() {
		return subject;
	}

	/**
	 * The post message
	 * 
	 * @return
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * The post message format
	 * 
	 * @return
	 */
	public int getMessageformat() {
		return messageformat;
	}

	/**
	 * Can we trust?
	 * 
	 * @return
	 */
	public int getMessagetrust() {
		return messagetrust;
	}

	/**
	 * Attachments
	 * 
	 * @return
	 */
	public String getAttachment() {
		return attachment;
	}

	/**
	 * The post message total score
	 * 
	 * @return
	 */
	public int getTotalscore() {
		return totalscore;
	}

	/**
	 * Mail now?
	 * 
	 * @return
	 */
	public int getMailnow() {
		return mailnow;
	}

	/**
	 * The user can reply to posts?
	 * 
	 * @return
	 */
	public Boolean getCanreply() {
		return canreply;
	}

	/**
	 * The post was read
	 * 
	 * @return
	 */
	public Boolean getPostread() {
		return postread;
	}

	/**
	 * Post author full name
	 * 
	 * @return
	 */
	public String getUserfullname() {
		return userfullname;
	}

	/**
	 * Get siteid
	 * 
	 * @return
	 */
	public long getSiteid() {
		return siteid;
	}

	/**
	 * Set siteid
	 * 
	 * @return
	 */
	public void setSiteid(long siteid) {
		this.siteid = siteid;
	}

}
