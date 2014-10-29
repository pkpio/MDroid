package in.co.praveenkumar.mdroid.moodlemodel;

import com.google.gson.annotations.SerializedName;

public class MoodleMessage {
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
	int useridfrom; // User from id

	@SerializedName("subject")
	String subject; // The message subject

	@SerializedName("fullmessage")
	String fullmessage; // The message

	@SerializedName("fullmessageformat")
	int fullmessageformat; // The message message format

	@SerializedName("fullmessagehtml")
	String fullmessagehtml; // The message in html

	@SerializedName("smallmessage")
	String smallmessage; // The shorten message

	@SerializedName("notification")
	int notification; // Is a notification?

	@SerializedName("contexturl")
	String contexturl; // Context URL

	@SerializedName("contexturlname")
	String contexturlname; // Context URL link name

	@SerializedName("timecreated")
	int timecreated; // Time created

	@SerializedName("timeread")
	int timeread; // Time read

	@SerializedName("usertofullname")
	String usertofullname;// User to full name

	@SerializedName("userfromfullname")
	String userfromfullname;// User from full name

	/*
	 * Fields for response
	 */
	@SerializedName("msgid")
	int msgid;

	@SerializedName("errormessage")
	String errormessage;

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

	public int getMessageid() {
		return messageid;
	}

	public int getUseridfrom() {
		return useridfrom;
	}

	public String getSubject() {
		return subject;
	}

	public String getFullmessage() {
		return fullmessage;
	}

	public int getFullmessageformat() {
		return fullmessageformat;
	}

	public String getFullmessagehtml() {
		return fullmessagehtml;
	}

	public String getSmallmessage() {
		return smallmessage;
	}

	public int getNotification() {
		return notification;
	}

	public String getContexturl() {
		return contexturl;
	}

	public String getContexturlname() {
		return contexturlname;
	}

	public int getTimecreated() {
		return timecreated;
	}

	public int getTimeread() {
		return timeread;
	}

	public String getUsertofullname() {
		return usertofullname;
	}

	public String getUserfromfullname() {
		return userfromfullname;
	}

}
