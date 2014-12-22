package in.co.praveenkumar.mdroid.helper;

/**
 * Hold all the common params for the application
 * 
 * @author Praveen Kumar Pendyala <praveen@praveenkumar.co.in>
 * 
 */
public class Param {
	public static final String DEFAULT_MSG_SIGN = "Sent from MDroid";

	// Ads hiding
	public static final int maxAdsHideCount = 3;
	public static Boolean hideAdsForSession = false;

	// Billing related
	public static final String BILLING_DONATION_PID = "in.co.praveenkumar.mdroid.donate";
	public static final String BILLING_FEATURE_NOTIFICATIONS_PID = "in.co.praveenkumar.mdroid.donate.features.new_content_notifications";
	public static final String BILLING_FEATURE_SEARCH_PID = "in.co.praveenkumar.mdroid.donate.features.search_option";
	public static final String BILLING_FEATURE_PARTICIPANTS_PID = "in.co.praveenkumar.mdroid.donate.features.course_participants";
	public static final String BILLING_FEATURE_UPLOADS_PID = "in.co.praveenkumar.mdroid.donate.features.uploading_files";
	public static final String BILLING_LICENSE_KEY = "02425045829981832447";

	/**************************************************
	 * Google analytics related
	 **************************************************/
	// Screens
	public static final String GA_SCREEN_TUTORIAL = "Tutorial";
	public static final String GA_SCREEN_LOGIN = "Login";
	public static final String GA_SCREEN_WEBSERVICESS_OFF = "WebServicesOff";
	public static final String GA_SCREEN_COURSE = "Course";
	public static final String GA_SCREEN_CONTENT = "Content";
	public static final String GA_SCREEN_FORUM = "Forum";
	public static final String GA_SCREEN_DISCUSSION = "Discussion";
	public static final String GA_SCREEN_POST = "Posts";
	public static final String GA_SCREEN_BROWSER = "Browser";
	public static final String GA_SCREEN_CALENDAR = "Calendar";
	public static final String GA_SCREEN_CONTACT = "Contact";
	public static final String GA_SCREEN_MESSAGE_LISTING = "MessageListing";
	public static final String GA_SCREEN_MESSAGING = "Messaging";
	public static final String GA_SCREEN_DONATION = "Donation";
	public static final String GA_SCREEN_WORKINPROGRESS = "WorkInProgress";
	public static final String GA_SCREEN_SETTING = "Setting";

}
