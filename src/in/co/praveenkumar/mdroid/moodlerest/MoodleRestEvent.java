package in.co.praveenkumar.mdroid.moodlerest;

import in.co.praveenkumar.mdroid.helper.GsonExclude;
import in.co.praveenkumar.mdroid.moodlemodel.MoodleEvents;

import java.io.Reader;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class MoodleRestEvent {
	public static final int ID_TYPE_COURSE = 0;
	public static final int ID_TYPE_EVENT = 1;
	public static final int ID_TYPE_GROUP = 2;
	private final String DEBUG_TAG = "MoodleRestEvent";
	private String mUrl;
	private String token;

	public MoodleRestEvent(String mUrl, String token) {
		this.mUrl = mUrl;
		this.token = token;
	}

	/**
	 * Get all Calendar events of given ids in current Moodle site. Allowed list
	 * of ids are:
	 * <ul>
	 * <li>courseids (ID_TYPE_COURSE)</li>
	 * <li>eventids (ID_TYPE_EVENT)</li>
	 * <li>groupids (ID_TYPE_GROUP)</li>
	 * </ul>
	 * 
	 * @param ids
	 *            List of ids.
	 * @param userEvents
	 *            Set if user events are to be included
	 * @param siteEvents
	 *            Set if site events are to be included
	 * @param idType
	 *            Type of the ids given. Ex.: use ID_TYPE_COURSE if requesting
	 *            for course events
	 * 
	 * <br/>
	 *            <b>Note:</b> If only site and user events are required
	 *            (excluding course or group events), pass null for ids
	 * 
	 * @return MoodleEvents
	 * 
	 * @author Praveen Kumar Pendyala (praveen@praveenkumar.co.in)
	 */
	public MoodleEvents getEventsForIds(List<String> ids, int idType,
			Boolean userEvents, Boolean siteEvents) {
		MoodleEvents mEvents = null;
		String format = MoodleRestOption.RESPONSE_FORMAT;
		String function = MoodleRestOption.FUNCTION_GET_EVENTS;

		try {
			// Adding all parameters.
			String params = "";

			// Moodle default endtime is today. Setting to year 2200
			params += "&options[timeend]="
					+ URLEncoder.encode("7258098600", "UTF-8");

			// Hide user events if set to
			if (!userEvents)
				params += "&options[userevents]="
						+ URLEncoder.encode("0", "UTF-8");

			// Hide site events if set to
			if (!siteEvents)
				params += "&options[siteevents]="
						+ URLEncoder.encode("0", "UTF-8");

			if (ids == null)
				ids = new ArrayList<String>();

			for (int i = 0; i < ids.size(); i++) {
				switch (idType) {
				case ID_TYPE_COURSE:
					params += "&events[courseids][" + i + "]="
							+ URLEncoder.encode(ids.get(i), "UTF-8");
					break;

				case ID_TYPE_EVENT:
					params += "&events[eventids][" + i + "]="
							+ URLEncoder.encode(ids.get(i), "UTF-8");
					break;

				case ID_TYPE_GROUP:
					params += "&events[groupids][" + i + "]="
							+ URLEncoder.encode(ids.get(i), "UTF-8");
					break;
				}
			}

			// Build a REST call url to make a call.
			String restUrl = mUrl + "/webservice/rest/server.php" + "?wstoken="
					+ token + "&wsfunction=" + function
					+ "&moodlewsrestformat=" + format;

			// Fetch content now.
			MoodleRestCall mrc = new MoodleRestCall();
			Reader reader = mrc.fetchContent(restUrl, params);
			GsonExclude ex = new GsonExclude();
			Gson gson = new GsonBuilder()
					.addDeserializationExclusionStrategy(ex)
					.addSerializationExclusionStrategy(ex).create();
			mEvents = gson.fromJson(reader, MoodleEvents.class);
			reader.close();
		} catch (Exception e) {
			Log.d(DEBUG_TAG, "URL encoding failed");
			e.printStackTrace();
		}

		return mEvents;
	}
}
