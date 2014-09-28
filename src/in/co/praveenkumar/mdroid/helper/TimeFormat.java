/**
 * Developed as a part of the MDroid project. Check project repository for License.
 * 
 * Link: https://github.com/praveendath92/MDroid
 */

package in.co.praveenkumar.mdroid.helper;

import java.util.Calendar;

/**
 * Time formatter.
 * 
 * Offers time formats for section title and other methods
 * 
 * @author Praveen Kumar Pendyala(praveen@praveenkumar.co.in)
 * 
 */
public class TimeFormat {
	/**
	 * Evaluates a section title suitable for the given time stamp. For use in
	 * Calender fragment.
	 * 
	 * @param time
	 *            in seconds
	 * @return section title
	 */
	public static String getSectionTitle(int time) {
		long ltime = ((long) time) * 1000;
		Calendar c = Calendar.getInstance();
		int nowDay = c.get(Calendar.DATE);
		int nowMonth = c.get(Calendar.MONTH);
		int nowYear = c.get(Calendar.YEAR);

		c.setTimeInMillis(ltime);
		int givenDay = c.get(Calendar.DATE);
		int givenMonth = c.get(Calendar.MONTH);
		int givenYear = c.get(Calendar.YEAR);

		// Past
		if (time * 1000 < System.currentTimeMillis()) {
			if (nowYear - givenYear > 1)
				return (nowYear - givenYear) + " years ago";
			else if (nowYear - givenYear == 1)
				return "1 year ago";
			else if (nowMonth - givenMonth > 1)
				return (nowMonth - givenMonth) + " months ago";
			else if (nowMonth - givenMonth == 1)
				return "1 month ago";
			else if (nowDay - givenDay > 1)
				return (nowDay - givenDay) + " days ago";
			else if (nowDay - givenDay == 1)
				return "Yesterday";
			else
				return "Today";
		}

		// Future
		else {
			if (givenYear - nowYear > 1)
				return "In " + (givenYear - nowYear) + " years";
			else if (givenYear - nowYear == 1)
				return "In 1 year";
			else if (givenMonth - nowMonth > 1)
				return "In " + (givenMonth - nowMonth) + " months";
			else if (givenMonth - nowMonth == 1)
				return "In 1 month";
			else if (givenDay - nowDay > 1)
				return "In " + (givenDay - nowDay) + " days";
			else if (givenDay - nowDay == 1)
				return "Tomorrow";
			else
				return "Today";
		}
	}

	/**
	 * Evaluates relative time for given time stamp
	 * 
	 * @param time
	 *            in seconds
	 * @return section title
	 */
	public static String getRelTime(int time) {
		long ltime = ((long) time) * 1000;
		Calendar c = Calendar.getInstance();
		int nowMin = c.get(Calendar.MINUTE);
		int nowHour = c.get(Calendar.HOUR_OF_DAY);
		int nowDay = c.get(Calendar.DATE);
		int nowMonth = c.get(Calendar.MONTH);
		int nowYear = c.get(Calendar.YEAR);

		c.setTimeInMillis(ltime);
		int givenMin = c.get(Calendar.MINUTE);
		int givenHour = c.get(Calendar.HOUR_OF_DAY);
		int givenDay = c.get(Calendar.DATE);
		int givenMonth = c.get(Calendar.MONTH);
		int givenYear = c.get(Calendar.YEAR);

		// Past
		if (time * 1000 < System.currentTimeMillis()) {
			if (nowYear - givenYear > 1)
				return (nowYear - givenYear) + " years ago";
			else if (nowYear - givenYear == 1)
				return "1 year ago";

			else if (nowMonth - givenMonth > 1)
				return (nowMonth - givenMonth) + " months ago";
			else if (nowMonth - givenMonth == 1)
				return "1 month ago";

			else if (nowDay - givenDay > 1)
				return (nowDay - givenDay) + " days ago";
			else if (nowDay - givenDay == 1)
				return "Yesterday";

			else if (nowHour - givenHour > 1)
				return (nowHour - givenHour) + " hours ago";
			else if (nowHour - givenHour == 1)
				return "1 hour ago";

			else if (nowMin - givenMin > 1)
				return (nowMin - givenMin) + " minutes ago";
			else if (nowMin - givenMin == 1)
				return "1 minute ago";

			else
				return "A moment ago";
		}

		// Future
		else {
			if (givenYear - nowYear > 1)
				return "In " + (givenYear - nowYear) + " years";
			else if (givenYear - nowYear == 1)
				return "In 1 year";

			else if (givenMonth - nowMonth > 1)
				return "In " + (givenMonth - nowMonth) + " months";
			else if (givenMonth - nowMonth == 1)
				return "In 1 month";

			else if (givenDay - nowDay > 1)
				return "In " + (givenDay - nowDay) + " days";
			else if (givenDay - nowDay == 1)
				return "Tomorrow";

			else if (givenHour - nowHour > 1)
				return "In " + (givenHour - nowHour) + " hours";
			else if (givenHour - nowHour == 1)
				return "In 1 hour";

			else if (givenMin - nowMin > 1)
				return "In " + (givenMin - nowMin) + " minutes";
			else if (givenMin - nowMin == 1)
				return "In 1 minute";

			else
				return "In a moment";
		}
	}

	/**
	 * Evaluates a simple more reader friendly time format like 23 Feb 12:23 AM
	 * 
	 * If the year in the time stamp is not the current year then the format
	 * would look like 23 Feb '13 12:23 AM
	 * 
	 * @param time
	 *            in seconds
	 * @return formatted time
	 */
	public static String getReadableTime(int time) {
		long ltime = ((long) time) * 1000;
		Calendar c = Calendar.getInstance();
		int yearnow = c.get(Calendar.YEAR);

		c.setTimeInMillis(ltime);
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DAY_OF_MONTH);
		int hour = c.get(Calendar.HOUR);
		int minute = c.get(Calendar.MINUTE);
		String AmPm = (c.get(Calendar.AM_PM) == Calendar.AM) ? "AM" : "PM";

		// Because 12:07 AM better than 12:7 AM
		String mins = (minute > 9) ? minute + "" : "0" + minute;

		if (year == yearnow || String.valueOf(year).length() != 4)
			return day + " " + getMonthName(month) + " " + hour + ":" + mins
					+ " " + AmPm;

		String yearString = String.valueOf(year);
		yearString = yearString.substring(yearString.length(),
				yearString.length() - 2);
		return day + " " + getMonthName(month) + " '" + yearString + " " + hour
				+ ":" + mins + " " + AmPm;
	}

	private static String getMonthName(int month) {
		// Month name
		switch (month) {
		case 1:
			return "Jan";
		case 2:
			return "Feb";
		case 3:
			return "Mar";
		case 4:
			return "Apr";
		case 5:
			return "May";
		case 6:
			return "Jun";
		case 7:
			return "Jul";
		case 8:
			return "Aug";
		case 9:
			return "Sep";
		case 10:
			return "Oct";
		case 11:
			return "Nov";
		case 12:
			return "Dec";
		default:
			return "Err";
		}
	}
}
