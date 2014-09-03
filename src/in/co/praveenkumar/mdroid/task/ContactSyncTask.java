package in.co.praveenkumar.mdroid.task;

import in.co.praveenkumar.mdroid.moodlemodel.MoodleContact;
import in.co.praveenkumar.mdroid.moodlemodel.MoodleContacts;
import in.co.praveenkumar.mdroid.moodlerest.MoodleRestContact;

import java.util.ArrayList;
import java.util.List;

public class ContactSyncTask {
	String mUrl;
	String token;
	long siteid;
	String error;

	/**
	 * 
	 * @param mUrl
	 * @param token
	 * @param siteid
	 * 
	 * @author Praveen Kumar Pendyala (praveen@praveenkumar.co.in)
	 */
	public ContactSyncTask(String mUrl, String token, long siteid) {
		this.mUrl = mUrl;
		this.token = token;
		this.siteid = siteid;
	}

	/**
	 * Sync all the user contacts in the current site.
	 * 
	 * @return syncStatus
	 * 
	 * @author Praveen Kumar Pendyala (praveen@praveenkumar.co.in)
	 */
	public Boolean syncAllContacts() {
		MoodleRestContact mrc = new MoodleRestContact(mUrl, token);
		MoodleContacts mContacts = mrc.getAllContacts();

		/** Error checking **/
		// Some network or encoding issue.
		if (mContacts == null) {
			error = "Network issue!";
			return false;
		}

		// Moodle exception
		if (mContacts.getErrorcode() != null) {
			error = mContacts.getErrorcode();
			// No additional debug info as that needs context
			return false;
		}

		// Save all contacts
		ArrayList<MoodleContact> contacts;

		// Online contacts
		contacts = mContacts.getOnline();
		saveToDb(contacts, MoodleContact.STATUS_ONLINE);

		// Offline contacts
		contacts = mContacts.getOffline();
		saveToDb(contacts, MoodleContact.STATUS_OFFLINE);

		// Strangers contacts
		contacts = mContacts.getStrangers();
		saveToDb(contacts, MoodleContact.STATUS_STRANGER);

		return true;
	}

	private void saveToDb(ArrayList<MoodleContact> contacts, int status) {
		List<MoodleContact> dbContacts;
		MoodleContact contact = new MoodleContact();

		if (contacts != null)
			for (int i = 0; i < contacts.size(); i++) {
				contact = contacts.get(i);
				contact.setStatus(status);
				contact.setSiteid(siteid);
				/*
				 * -TODO- Improve this search with only Sql operation
				 */
				dbContacts = MoodleContact.find(MoodleContact.class,
						"contactid = ? and siteid = ?", contacts.get(i)
								.getContactid() + "", siteid + "");
				if (dbContacts.size() > 0)
					contact.setId(dbContacts.get(0).getId());
				contact.save();
			}
	}
}
