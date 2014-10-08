package in.co.praveenkumar.mdroid.moodlemodel;

public class MoodleMessage {
	// Fields while sending message
	int touserid;
	String text;
	int textformat;
	String clientmsgid;

	// Fields for response
	int msgid;
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

}
