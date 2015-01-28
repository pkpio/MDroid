package in.co.praveenkumar.mdroid.model;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

/**
 * Moodle returns events in 2 objects - Events list and list of warnings We
 * won't be using this model for db operations but instead only easy event
 * retrieval from json.
 * 
 * @author Praveen Kumar Pendyala (praveen@praveenkumar.co.in)
 * 
 */
public class MoodleEvents {

	@SerializedName("events")
	ArrayList<MoodleEvent> events;

	@SerializedName("warnings")
	ArrayList<MoodleEventWarning> warnings;

	// Errors. Not to be stored in sql db.
	@SerializedName("exception")
	String exception;

	@SerializedName("errorcode")
	String errorcode;

	@SerializedName("message")
	String message;

	@SerializedName("debuginfo")
	String debuginfo;

	/**
	 * Get ArrayList of Events
	 * 
	 * @return events
	 */
	public ArrayList<MoodleEvent> getEvents() {
		return events;
	}

	/**
	 * Get ArrayList of Event warnings
	 * 
	 * @return warnings
	 */
	public ArrayList<MoodleEventWarning> getWarnings() {
		return warnings;
	}

	/**
	 * Exception occurred while retrieving
	 * 
	 * @return
	 */
	public String getException() {
		return exception;
	}

	/**
	 * Errorcode of error occurred while retrieving
	 * 
	 * @return
	 */
	public String getErrorcode() {
		return errorcode;
	}

	/**
	 * Message of error occurred while retrieving
	 * 
	 * @return
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Debug info on the error occurred
	 * 
	 * @return
	 */
	public String getDebuginfo() {
		return debuginfo;
	}

	/**
	 * Represents warnings that come along with a Events call. <br/>
	 * <br/>
	 * This is not so important to be implemented as main model I guess
	 * 
	 * @author Praveen Kumar Pendyala (praveen@praveenkumar.co.in)
	 */
	public class MoodleEventWarning {
		String item;
		int itemid;
		String warningcode;
		String message;

		/**
		 * Get item
		 * 
		 * @return
		 */
		public String getItem() {
			return item;
		}

		/**
		 * Get item id
		 * 
		 * @return
		 */
		public int getItemid() {
			return itemid;
		}

		/**
		 * Get the warning code can be used by the client app to implement
		 * specific behaviour
		 * 
		 * @return
		 */
		public String getWarningcode() {
			return warningcode;
		}

		/**
		 * Get untranslated english message to explain the warning
		 * 
		 * @return
		 */
		public String getMessage() {
			return message;
		}
	}
}
