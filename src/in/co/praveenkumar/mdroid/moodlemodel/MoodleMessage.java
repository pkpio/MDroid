package in.co.praveenkumar.mdroid.moodlemodel;

import com.google.gson.annotations.SerializedName;

public class MoodleMessage {
	public static final int MESSAGE_FORMAT_HTML = 1;
	public static final int MESSAGE_FORMAT_MOODLE = 0;
	public static final int MESSAGE_FORMAT_PLAIN = 2;
	public static final int MESSAGE_FORMAT_MARKDOWN = 4;

	/*
	 * Fields common to fetch and send
	 */
	@SerializedName("touserid")
	int touserid;

	@SerializedName("text")
	String text;

	/*
	 * Fields specific to send
	 */
	@SerializedName("textformat")
	int textformat;

	@SerializedName("clientmsgid")
	String clientmsgid;

	/*
	 * Fields specific to fetch
	 */
	@SerializedName("id")
	int messageid;

	@SerializedName("useridfrom")
	int useridfrom;

	@SerializedName("subject")
	String subject;

	@SerializedName("fullmessage")
	String fullmessage;

	@SerializedName("fullmessageformat")
	int fullmessageformat;

	@SerializedName("fullmessagehtml")
	String fullmessagehtml;

	@SerializedName("smallmessage")
	String smallmessage;

	@SerializedName("notification")
	int notification;

	@SerializedName("contexturl")
	String contexturl;

	@SerializedName("contexturlname")
	String contexturlname;

	@SerializedName("timecreated")
	int timecreated;

	@SerializedName("timeread")
	int timeread;

	@SerializedName("usertofullname")
	String usertofullname;

	@SerializedName("userfromfullname")
	String userfromfullname;

	/*
	 * Fields for response
	 */
	@SerializedName("msgid")
	int msgid;

	@SerializedName("errormessage")
	String errormessage;

	/**
	 * Default message with no params set.
	 */
	public MoodleMessage() {
	}

	/**
	 * Uses plaintext as the message format.
	 * 
	 * @param touserid
	 *            Moodle userid of the user
	 * @param text
	 *            message content
	 */
	public MoodleMessage(int touserid, String text) {
		this.touserid = touserid;
		this.text = text;
		this.textformat = MESSAGE_FORMAT_PLAIN;
		this.clientmsgid = "";
	}

	/**
	 * @param touserid
	 *            Moodle userid of the user
	 * @param text
	 *            message content
	 * @param textformat
	 *            message format. Use contants MESSAGE_FORMAT_
	 */
	public MoodleMessage(int touserid, String text, int textformat) {
		this.touserid = touserid;
		this.text = text;
		this.textformat = textformat;
		this.clientmsgid = "";
	}

	/**
	 * @param touserid
	 *            Moodle userid of the user
	 * @param text
	 *            message content
	 * @param textformat
	 *            message format. Use contants MESSAGE_FORMAT_
	 * @param clientmsgid
	 *            your own client id for the message. If this id is provided,
	 *            the fail message id will be returned to you
	 */
	public MoodleMessage(int touserid, String text, int textformat,
			String clientmsgid) {
		this.touserid = touserid;
		this.text = text;
		this.textformat = textformat;
		this.clientmsgid = clientmsgid;
	}

	/**
	 * id of the user to send the private message
	 * 
	 * @return
	 */
	public int getTouserid() {
		return touserid;
	}

	/**
	 * id of the user to send the private message
	 * 
	 * @param touserid
	 */
	public void setTouserid(int touserid) {
		this.touserid = touserid;
	}

	/**
	 * the text of the message
	 * 
	 * @return
	 */
	public String getText() {
		return text;
	}

	/**
	 * the text of the message
	 * 
	 * @param text
	 */
	public void setText(String text) {
		this.text = text;
	}

	/**
	 * Default to "1" (1 = HTML, 0 = MOODLE, 2 = PLAIN or 4 = MARKDOWN)
	 * 
	 * @return
	 */
	public int getTextformat() {
		return textformat;
	}

	/**
	 * Default to "1" (1 = HTML, 0 = MOODLE, 2 = PLAIN or 4 = MARKDOWN)
	 * 
	 * @return
	 */
	public void setTextformat(int textformat) {
		this.textformat = textformat;
	}

	/**
	 * (Optional) your own client id for the message. If this id is provided,
	 * the fail message id will be returned to you
	 * 
	 * @return
	 */
	public String getClientmsgid() {
		return clientmsgid;
	}

	/**
	 * (Optional) your own client id for the message. If this id is provided,
	 * the fail message id will be returned to you
	 * 
	 * @return
	 */
	public void setClientmsgid(String clientmsgid) {
		this.clientmsgid = clientmsgid;
	}

	/**
	 * test this to know if it succeeds: id of the created message if it
	 * succeeded, -1 when failed
	 * 
	 * @return
	 */
	public int getMsgid() {
		return msgid;
	}

	/**
	 * (Optional) error message - if it failed
	 * 
	 * @return
	 */
	public String getErrormessage() {
		return errormessage;
	}

	/**
	 * Moodle site id for message
	 * 
	 * @return
	 */
	public int getMessageid() {
		return messageid;
	}

	/**
	 * User from id
	 * 
	 * @return
	 */
	public int getUseridfrom() {
		return useridfrom;
	}

	/**
	 * The message subject
	 * 
	 * @return
	 */
	public String getSubject() {
		return subject;
	}

	/**
	 * The message
	 * 
	 * @return
	 */
	public String getFullmessage() {
		return fullmessage;
	}

	/**
	 * The message message format
	 * 
	 * @return
	 */
	public int getFullmessageformat() {
		return fullmessageformat;
	}

	/**
	 * The message in html
	 * 
	 * @return
	 */
	public String getFullmessagehtml() {
		return fullmessagehtml;
	}

	/**
	 * The shorten message
	 * 
	 * @return
	 */
	public String getSmallmessage() {
		return smallmessage;
	}

	/**
	 * Is a notification?
	 * 
	 * @return
	 */
	public int getNotification() {
		return notification;
	}

	/**
	 * Context URL
	 * 
	 * @return
	 */
	public String getContexturl() {
		return contexturl;
	}

	/**
	 * Context URL link name
	 * 
	 * @return
	 */
	public String getContexturlname() {
		return contexturlname;
	}

	/**
	 * Time created
	 * 
	 * @return
	 */
	public int getTimecreated() {
		return timecreated;
	}

	/**
	 * Time read
	 * 
	 * @return
	 */
	public int getTimeread() {
		return timeread;
	}

	/**
	 * User to full name
	 * 
	 * @return
	 */
	public String getUsertofullname() {
		return usertofullname;
	}

	/**
	 * User from full name
	 */
	public String getUserfromfullname() {
		return userfromfullname;
	}

}
