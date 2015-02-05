package in.co.praveenkumar.mdroid.helper;

/**
 * Hold all the common params for the application
 * 
 * @author Praveen Kumar Pendyala <praveen@praveenkumar.co.in>
 * 
 */
public class Param {
	public static final String DEFAULT_MSG_SIGN = "Sent from MDroid";
	public static final String STARTAPP_DEV_ID = "112268240";
	public static final String STARTAPP_APP_ID = "212832712";
	/**
	 * The maximum frequency at which startApp interstitials appear. The time
	 * difference (in milliseconds) between two successful ads will definitely
	 * be less than this.
	 */
	public static final long STARTAPP_INTERSTITIAL_MAX_FREQ = 50 * 1000;
	public static final Boolean STARTAPP_INTERSTITIAL_ADS = true;
	public static final Boolean STARTAPP_EXIT_ADS = true;

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
	// GA Screens
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
	public static final String GA_SCREEN_NOTIFICATION = "Notification";
	public static final String GA_SCREEN_SETTING = "Setting";

	/** GA Event categories **/
	public static final String GA_EVENT_CAT_LOGIN = "LoginScreen";
	public static final String GA_EVENT_CAT_SETTING = "SettingScreen";
	public static final String GA_EVENT_CAT_DONATION = "DonationScreen";
	public static final String GA_EVENT_CAT_ADS = "Ads";

	/** GA Events **/
	// Login screen events
	public static final String GA_EVENT_LOGIN_DEMO_NORMAL = "DemoLoginNormal";
	public static final String GA_EVENT_LOGIN_DEMO_PARANOID = "DemoLoginParanoid";
	public static final String GA_EVENT_LOGIN_NORMAL = "LoginNormal";
	public static final String GA_EVENT_LOGIN_PARANOID = "LoginParanoid";
	public static final String GA_EVENT_LOGIN_PARANOID_HELP = "ParanoidHelp";

	// Settings screen events
	public static final String GA_EVENT_SETTING_HIDEADS = "HideAds";

	// Donation screen events
	public static final String GA_EVENT_DONATION_BUTTON = "DonateButtonPress";

	// Ads related events
	public static final String GA_EVENT_ADS_STARTAPP_INTERSTITIAL_SHOW = "StartAppInterstitialShow";
	public static final String GA_EVENT_ADS_STARTAPP_INTERSTITIAL_HIDE = "StartAppInterstitialHide";
	public static final String GA_EVENT_ADS_STARTAPP_INTERSTITIAL_CLICK = "StartAppInterstitialClick";
	public static final String GA_EVENT_ADS_STARTAPP_EXITAD = "StartAppExitAd";

}
