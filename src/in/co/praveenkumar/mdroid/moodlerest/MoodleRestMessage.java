package in.co.praveenkumar.mdroid.moodlerest;

import in.co.praveenkumar.mdroid.moodlemodel.MoodleMessage;

import java.io.Reader;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class MoodleRestMessage {
	public static final int MESSAGE_FORMAT_HTML = 1;
	public static final int MESSAGE_FORMAT_MOODLE = 0;
	public static final int MESSAGE_FORMAT_PLAIN = 2;
	public static final int MESSAGE_FORMAT_MARKDOWN = 4;

	private final String DEBUG_TAG = "MoodleRestMessage";
	private String mUrl;
	private String token;
	private String error;

	public MoodleRestMessage(String mUrl, String token) {
		this.mUrl = mUrl;
		this.token = token;
	}

	/**
	 * Sends a message to a Moodle user. Uses plaintext as the message format.
	 * 
	 * @param userid
	 *            Moodle userid of the user
	 * @param message
	 *            message content
	 * 
	 * @return status - true if success. false if failed.
	 */
	public Boolean sendMessage(int userid, String message) {
		return sendMessage(userid, message, MESSAGE_FORMAT_PLAIN);
	}

	/**
	 * Sends a message to a Moodle user.
	 * 
	 * @param userid
	 *            Moodle userid of the user
	 * @param message
	 *            message content
	 * @param messageformat
	 *            message format. Use contants MESSAGE_FORMAT_
	 * 
	 * @return status - true if success. false if failed.
	 */
	public Boolean sendMessage(int userid, String message, int messageformat) {
		return sendMessage(userid, message, MESSAGE_FORMAT_PLAIN, "");
	}

	/**
	 * Sends a message to a Moodle user.
	 * 
	 * @param userid
	 *            Moodle userid of the user
	 * @param message
	 *            message content
	 * @param messageformat
	 *            message format. Use contants MESSAGE_FORMAT_
	 * @param clientmsgid
	 *            your own client id for the message. If this id is provided,
	 *            the fail message id will be returned to you
	 * 
	 * @return status - true if success. false if failed.
	 */
	public Boolean sendMessage(int userid, String message, int messageformat,
			String clientmsgid) {
		String format = MoodleRestOption.RESPONSE_FORMAT;
		String function = MoodleRestOption.FUNCTION_SEND_MESSAGE;
		ArrayList<MoodleMessage> mMessages = new ArrayList<MoodleMessage>();

		try {
			// Adding all parameters.
			String params = "" + URLEncoder.encode("", "UTF-8");

			params += "&messages[0][touserid]="
					+ URLEncoder.encode(String.valueOf(userid), "UTF-8");
			params += "&messages[0][text]="
					+ URLEncoder.encode(message, "UTF-8");
			params += "&messages[0][textformat]="
					+ URLEncoder.encode(String.valueOf(messageformat), "UTF-8");
			if (!clientmsgid.contentEquals(""))
				params += "&messages[0][clientmsgid]="
						+ URLEncoder.encode(clientmsgid, "UTF-8");

			// Build a REST call url to make a call.
			String restUrl = mUrl + "/webservice/rest/server.php" + "?wstoken="
					+ token + "&wsfunction=" + function
					+ "&moodlewsrestformat=" + format;

			// Fetch content now.
			MoodleRestCall mrc = new MoodleRestCall();
			Reader reader = mrc.fetchContent(restUrl, params);
			Gson gson = new GsonBuilder().create();
			mMessages = gson.fromJson(reader,
					new TypeToken<List<MoodleMessage>>() {
					}.getType());
			reader.close();

			if (mMessages == null)
				return false;

			if (mMessages.size() == 0)
				return false;

			MoodleMessage mMessage = mMessages.get(0);
			if (mMessage.getMsgid() == -1) {
				error = mMessage.getErrormessage();
				return false;
			}

		} catch (Exception e) {
			Log.d(DEBUG_TAG, "URL encoding failed");
			error = "URL encoding failed";
			e.printStackTrace();
		}

		return true;
	}

	/**
	 * Error while sending last message.
	 * 
	 * @return error
	 */
	public String getError() {
		return error;
	}
}
