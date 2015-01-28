package in.co.praveenkumar.mdroid.model;

import com.orm.SugarRecord;
import com.orm.dsl.Ignore;

public class MDroidNotification extends SugarRecord<MDroidNotification> {

	@Ignore
	public static final int TYPE_COURSE_CONTENT = 1;

	@Ignore
	public static final int TYPE_FORUM = 2;

	@Ignore
	public static final int TYPE_FORUM_TOPIC = 3;

	@Ignore
	public static final int TYPE_FORUM_REPLY = 4;

	int type;
	String title;
	String content;
	int count;
	Boolean read;

	// relational fields
	long siteid;

	/**
	 * Get the type of notification. Possible types can be check from TYPE_ om
	 * this class.
	 * 
	 * @return type
	 */
	public int getType() {
		return type;
	}

	/**
	 * Set the notification type. Possible types can be check from TYPE_ om this
	 * class.
	 * 
	 * @param type
	 */
	public void setType(int type) {
		this.type = type;
	}

	/**
	 * Get notification title
	 * 
	 * @return
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Set notification title
	 * 
	 * @param title
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Get notification content
	 * 
	 * @return
	 */
	public String getContent() {
		return content;
	}

	/**
	 * Set notification content
	 * 
	 * @param content
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * Get notification count (if any)
	 * 
	 * @return
	 */
	public int getCount() {
		return count;
	}

	/**
	 * Set notification count
	 * 
	 * @param count
	 */
	public void setCount(int count) {
		this.count = count;
	}

	/**
	 * Get if notification is read
	 * 
	 * @return true if read
	 */
	public Boolean getRead() {
		return read;
	}

	/**
	 * Set read status of notification
	 * 
	 * @param read
	 */
	public void setRead(Boolean read) {
		this.read = read;
	}

	/**
	 * Get siteid of the notification
	 * 
	 * @return
	 */
	public long getSiteid() {
		return siteid;
	}

	/**
	 * Set siteid of the notification
	 * 
	 * @param siteid
	 */
	public void setSiteid(long siteid) {
		this.siteid = siteid;
	}

}
