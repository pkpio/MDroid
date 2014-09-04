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
	 * Evaluates a section title suitable for the given time stamp
	 * 
	 * @param time
	 *            in seconds
	 * @return section title
	 */
	public static String getSection(int time) {
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
	 * Evaluates a simple time format like 12:23 AM
	 * 
	 * @param time
	 *            in seconds
	 * @return formatted time
	 */
	public static String getMinimalTime(int time) {
		long ltime = ((long) time) * 1000;
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(ltime);
		int hour = c.get(Calendar.HOUR);
		int minute = c.get(Calendar.MINUTE);
		String AmPm = (c.get(Calendar.AM_PM) == Calendar.AM) ? "AM" : "PM";
		// Because 12:07 AM better than 12:7 AM
		String mins = (minute > 9) ? minute + "" : "0" + minute;

		return hour + ":" + mins + " " + AmPm;
	}
}
