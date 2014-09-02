package in.co.praveenkumar.mdroid.moodlemodel;

import java.util.ArrayList;

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
	ArrayList<MoodleContact> online;
	ArrayList<MoodleContact> offline;
	ArrayList<MoodleContact> strangers;

	/**
	 * Get ArrayList of online MoodleContact
	 * 
	 * @return online contacts
	 */
	public ArrayList<MoodleContact> getOnline() {
		return online;
	}

	/**
	 * Get ArrayList of offline MoodleContact
	 * 
	 * @return offline contacts
	 */
	public ArrayList<MoodleContact> getOffline() {
		return offline;
	}

	/**
	 * Get ArrayList of stranger MoodleContact
	 * 
	 * @return stranger contacts
	 */
	public ArrayList<MoodleContact> getStrangers() {
		return strangers;
	}
}
