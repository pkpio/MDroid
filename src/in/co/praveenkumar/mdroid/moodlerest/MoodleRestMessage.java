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
	private final String DEBUG_TAG = "MoodleRestMessage";
	private String mUrl;
	private String token;
	private String error;

	public MoodleRestMessage(String mUrl, String token) {
		this.mUrl = mUrl;
		this.token = token;
	}

	/**
	 * Sends a message to a Moodle user.
	 * 
	 * @param message
	 *            Message to send with the required params (touserid, text at
	 *            the least) set
	 * 
	 * @return status - true if success. false if failed.
	 */
	public Boolean sendMessage(MoodleMessage message) {
		String format = MoodleRestOption.RESPONSE_FORMAT;
		String function = MoodleRestOption.FUNCTION_SEND_MESSAGE;
		ArrayList<MoodleMessage> mMessages = new ArrayList<MoodleMessage>();

		try {
			// Adding all parameters.
			String params = "" + URLEncoder.encode("", "UTF-8");

			params += "&messages[0][touserid]="
					+ URLEncoder.encode(String.valueOf(message.getTouserid()),
							"UTF-8");
			params += "&messages[0][text]="
					+ URLEncoder.encode(message.getText(), "UTF-8");
			params += "&messages[0][textformat]="
					+ URLEncoder.encode(
							String.valueOf(message.getTextformat()), "UTF-8");
			if (message.getClientmsgid() != null)
				params += "&messages[0][clientmsgid]="
						+ URLEncoder.encode(message.getClientmsgid(), "UTF-8");

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
