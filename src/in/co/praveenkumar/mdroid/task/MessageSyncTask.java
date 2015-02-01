package in.co.praveenkumar.mdroid.task;

import in.co.praveenkumar.mdroid.model.MDroidNotification;
import in.co.praveenkumar.mdroid.model.MoodleMessage;
import in.co.praveenkumar.mdroid.model.MoodleMessages;
import in.co.praveenkumar.mdroid.moodlerest.MoodleRestMessage;

import java.util.List;

public class MessageSyncTask {
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
	public MessageSyncTask(String mUrl, String token, long siteid) {
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
	public MessageSyncTask(String mUrl, String token, long siteid,
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
	 * Sync all messages sent / received by a user (current user typically)
	 * 
	 * @param userid
	 *            userid of the current site user.
	 * @return syncStatus
	 * 
	 * @author Praveen Kumar Pendyala (praveen@praveenkumar.co.in)
	 */
	public Boolean syncMessages(int userid) {
		MoodleRestMessage mrm = new MoodleRestMessage(mUrl, token);
		return saveMessages(mrm.getMessages(userid, 0, 0))
				&& saveMessages(mrm.getMessages(userid, 0, 1))
				&& saveMessages(mrm.getMessages(0, userid, 0))
				&& saveMessages(mrm.getMessages(0, userid, 1));
	}

	private Boolean saveMessages(MoodleMessages moodleMessages) {

		/** Error checking **/
		// Some network or encoding issue.
		if (moodleMessages == null) {
			error = "Network issue!";
			return false;
		}

		// Moodle exception
		if (moodleMessages.getErrorcode() != null) {
			error = moodleMessages.getErrorcode();
			// No additional debug info as that needs context
			return false;
		}

		List<MoodleMessage> mMessages = moodleMessages.getMessages();
		// Warnings are not being handled
		List<MoodleMessage> dbMessages;
		MoodleMessage message = new MoodleMessage();

		if (mMessages != null)
			for (int i = 0; i < mMessages.size(); i++) {
				message = mMessages.get(i);
				message.setSiteid(siteid);
				/*
				 * -TODO- Improve this search with only Sql operation
				 */
				dbMessages = MoodleMessage.find(MoodleMessage.class,
						"messageid = ? and siteid = ?", message.getMessageid()
								+ "", siteid + "");
				if (dbMessages.size() > 0)
					message.setId(dbMessages.get(0).getId());
				// set notifications if enabled
				else if (notification) {
					new MDroidNotification(
							siteid,
							MDroidNotification.TYPE_MESSAGE,
							"New message from " + message.getUserfromfullname(),
							message.getText(), 1, message.getUseridfrom())
							.save();
					notificationcount++;
				}
				message.save();
			}

		return true;
	}

}
