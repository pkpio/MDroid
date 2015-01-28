package in.co.praveenkumar.mdroid.model;

import java.util.List;

import com.google.gson.annotations.SerializedName;
import com.orm.SugarRecord;
import com.orm.dsl.Ignore;

public class MoodleUser extends SugarRecord<MoodleUser> {

	// since id is a reserved field in SugarRecord
	@SerializedName("id")
	int userid;

	@SerializedName("username")
	String username;

	@SerializedName("firstname")
	String firstname;

	@SerializedName("lastname")
	String lastname;

	@SerializedName("fullname")
	String fullname;

	@SerializedName("email")
	String email;

	@SerializedName("address")
	String address;

	@SerializedName("phone1")
	String phone1;

	@SerializedName("phone2")
	String phone2;

	@SerializedName("icq")
	String icq;

	@SerializedName("skype")
	String skype;

	@SerializedName("yahoo")
	String yahoo;

	@SerializedName("aim")
	String aim;

	@SerializedName("msn")
	String msn;

	@SerializedName("department")
	String department;

	@SerializedName("institution")
	String institution;

	@SerializedName("idnumber")
	String idnumber;

	@SerializedName("interests")
	String interests;

	@SerializedName("firstaccess")
	int firstaccess;

	@SerializedName("lastaccess")
	int lastaccess;

	@SerializedName("auth")
	String auth;

	@SerializedName("confirmed")
	int confirmed;

	@SerializedName("lang")
	String lang;

	@SerializedName("calendartype")
	String calendartype;

	@SerializedName("theme")
	String theme;

	@SerializedName("timezone")
	String timezone;

	@SerializedName("mailformat")
	int mailformat;

	@SerializedName("description")
	String description;

	@SerializedName("descriptionformat")
	String descriptionformat;

	@SerializedName("city")
	String city;

	@SerializedName("url")
	String url;

	@SerializedName("country")
	String country;

	@SerializedName("profileimageurlsmall")
	String profileimageurlsmall;

	@SerializedName("profileimageurl")
	String profileimageurl;

	@Ignore
	@SerializedName("enrolledcourses")
	List<MoodleUserCourse> enrolledcourses;

	// No support for custom fields and preferences yet.

	// Relational fields
	long siteid;
	int courseid;

	/**
	 * Overridden / rewritten methods from sugar super class
	 */

	/**
	 * Supports saving list of enrolled courses of the user as well.
	 */
	@Override
	public void save() {
		super.save();

		// Save users' enrolled courses
		if (enrolledcourses == null || enrolledcourses.size() == 0)
			return;

		MoodleUserCourse mUserCourse;
		List<MoodleUserCourse> dbUserCourses;
		for (int i = 0; i < enrolledcourses.size(); i++) {
			mUserCourse = enrolledcourses.get(i);
			mUserCourse.setSiteid(this.siteid);
			mUserCourse.setUserid(this.userid);
			dbUserCourses = MoodleUserCourse.find(MoodleUserCourse.class,
					"userid = ? and siteid = ? and courseid = ?", userid + "",
					siteid + "", mUserCourse.getCourseid() + "");
			if (dbUserCourses != null && dbUserCourses.size() > 0)
				mUserCourse.setId(dbUserCourses.get(0).getId());
			mUserCourse.save();
		}
	}

	public int getUserid() {
		return userid;
	}

	/**
	 * Get username
	 * 
	 * @return
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Get The first name(s) of the user
	 * 
	 * @return
	 */
	public String getFirstname() {
		return firstname;
	}

	/**
	 * Get The family name of the user
	 * 
	 * @return
	 */
	public String getLastname() {
		return lastname;
	}

	/**
	 * Get The fullname of the user
	 * 
	 * @return
	 */
	public String getFullname() {
		return fullname;
	}

	/**
	 * Get email
	 * 
	 * @return
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Get address
	 * 
	 * @return
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * Get phone1
	 * 
	 * @return
	 */
	public String getPhone1() {
		return phone1;
	}

	/**
	 * Get phone2
	 * 
	 * @return
	 */
	public String getPhone2() {
		return phone2;
	}

	/**
	 * Get icq id
	 * 
	 * @return
	 */
	public String getIcq() {
		return icq;
	}

	/**
	 * Get skype id
	 * 
	 * @return
	 */
	public String getSkype() {
		return skype;
	}

	/**
	 * Get yahoo id
	 * 
	 * @return
	 */
	public String getYahoo() {
		return yahoo;
	}

	/**
	 * Get aim id
	 * 
	 * @return
	 */
	public String getAim() {
		return aim;
	}

	/**
	 * Get msn id
	 * 
	 * @return
	 */
	public String getMsn() {
		return msn;
	}

	/**
	 * Get department
	 * 
	 * @return
	 */
	public String getDepartment() {
		return department;
	}

	/**
	 * Get institution
	 * 
	 * @return
	 */
	public String getInstitution() {
		return institution;
	}

	/**
	 * Get An arbitrary ID code number perhaps from the institution
	 * 
	 * @return
	 */
	public String getIdnumber() {
		return idnumber;
	}

	/**
	 * Get user interests (separated by commas)
	 * 
	 * @return
	 */
	public String getInterests() {
		return interests;
	}

	/**
	 * Get first access to the site (0 if never)
	 * 
	 * @return
	 */
	public int getFirstaccess() {
		return firstaccess;
	}

	/**
	 * Get last access to the site (0 if never)
	 * 
	 * @return
	 */
	public int getLastaccess() {
		return lastaccess;
	}

	/**
	 * Get Auth plugins include manual, ldap, imap, etc
	 * 
	 * @return
	 */
	public String getAuth() {
		return auth;
	}

	/**
	 * Get Active user: 1 if confirmed, 0 otherwise
	 * 
	 * @return
	 */
	public int getConfirmed() {
		return confirmed;
	}

	/**
	 * Get Language code such as "en", must exist on server
	 * 
	 * @return
	 */
	public String getLang() {
		return lang;
	}

	/**
	 * Get Calendar type such as "gregorian", must exist on server
	 * 
	 * @return
	 */
	public String getCalendartype() {
		return calendartype;
	}

	/**
	 * Get Theme name such as "standard", must exist on server
	 * 
	 * @return
	 */
	public String getTheme() {
		return theme;
	}

	/**
	 * Get Timezone code such as Australia/Perth, or 99 for default
	 * 
	 * @return
	 */
	public String getTimezone() {
		return timezone;
	}

	/**
	 * Get Mail format code is 0 for plain text, 1 for HTML etc
	 * 
	 * @return
	 */
	public int getMailformat() {
		return mailformat;
	}

	/**
	 * Get User profile description
	 * 
	 * @return
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Get description format (1 = HTML, 0 = MOODLE, 2 = PLAIN or 4 = MARKDOWN)
	 * 
	 * @return
	 */
	public String getDescriptionformat() {
		return descriptionformat;
	}

	/**
	 * Get Home city of the user
	 * 
	 * @return
	 */
	public String getCity() {
		return city;
	}

	/**
	 * Get URL of the user
	 * 
	 * @return
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * Get Home country code of the user, such as AU or CZ
	 * 
	 * @return
	 */
	public String getCountry() {
		return country;
	}

	/**
	 * Get User image profile URL - small version
	 * 
	 * @return
	 */
	public String getProfileimageurlsmall() {
		return profileimageurlsmall;
	}

	/**
	 * Get User image profile URL - big version
	 * 
	 * @return
	 */
	public String getProfileimageurl() {
		return profileimageurl;
	}

	/**
	 * Get the siteid of this user
	 * 
	 * @return
	 */
	public long getSiteid() {
		return siteid;
	}

	/**
	 * set the site id of this user
	 * 
	 * @param siteid
	 */
	public void setSiteid(long siteid) {
		this.siteid = siteid;
	}

	/**
	 * Get the siteid of this user
	 * 
	 * @return
	 */
	public long getCourseid() {
		return courseid;
	}

	/**
	 * set the site id of this user
	 * 
	 * @param siteid
	 */
	public void setCourseid(int courseid) {
		this.courseid = courseid;
	}

	/**
	 * Get list of Enrolled courses of user
	 * 
	 * @return List of MoodleUserCourse
	 */
	public List<MoodleUserCourse> getEnrolledcourses() {
		return enrolledcourses;
	}

	/**
	 * 
	 * Set list of Enrolled courses of user
	 * 
	 * @param enrolledcourses
	 *            List of MoodleUserCourse
	 */
	public void setEnrolledcourses(List<MoodleUserCourse> enrolledcourses) {
		this.enrolledcourses = enrolledcourses;
	}
}
