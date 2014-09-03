package in.co.praveenkumar.mdroid.moodlemodel;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

/**
 * Moodle returns contacts in 3 objects - Online, Offline and Strangers with
 * contacts filled in each section. We won't be using this model for db
 * operations but instead only easy contact retrieval from json. Status of the
 * contact will be saved in the db using additional field instead.
 * 
 * @author Praveen Kumar Pendyala (praveen@praveenkumar.co.in)
 * 
 */
public class MoodleContacts {
	@SerializedName("online")
	ContactStatus online;

	@SerializedName("offline")
	ContactStatus offline;

	@SerializedName("strangers")
	ContactStatus strangers;

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
	 * Get ArrayList of online MoodleContact
	 * 
	 * @return online contacts
	 */
	public ArrayList<MoodleContact> getOnline() {
		if (online == null)
			return null;
		return online.getContacts();
	}

	/**
	 * Get ArrayList of offline MoodleContact
	 * 
	 * @return offline contacts
	 */
	public ArrayList<MoodleContact> getOffline() {
		if (offline == null)
			return null;
		return offline.getContacts();
	}

	/**
	 * Get ArrayList of stranger MoodleContact
	 * 
	 * @return stranger contacts
	 */
	public ArrayList<MoodleContact> getStrangers() {
		if (strangers == null)
			return null;
		return strangers.getContacts();
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
	 * Helper class for Moodle contacts decoding
	 * 
	 * @author praveen
	 * 
	 */
	public class ContactStatus {
		ArrayList<MoodleContact> contacts;

		public ArrayList<MoodleContact> getContacts() {
			return this.contacts;
		}

	}

}
