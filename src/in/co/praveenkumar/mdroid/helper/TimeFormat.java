package in.co.praveenkumar.mdroid.helper;

import java.util.Calendar;

public class TimeFormat {
	static int SECONDS = 1000; // milliseconds
	static int MINUTES = SECONDS * 60;
	static int HOURS = MINUTES * 60;
	static int DAYS = HOURS * 24;
	static int MONTHS = DAYS * 30;
	static int YEARS = MONTHS * 12;

	public static String getSection(long time) {
		Calendar c = Calendar.getInstance();
		int nowHour = c.get(Calendar.HOUR_OF_DAY);
		int nowDay = c.get(Calendar.DATE);
		int nowMonth = c.get(Calendar.MONTH);
		int nowYear = c.get(Calendar.YEAR);

		c.setTimeInMillis(time);
		int givenHour = c.get(Calendar.HOUR_OF_DAY);
		int givenDay = c.get(Calendar.DATE);
		int givenMonth = c.get(Calendar.MONTH);
		int givenYear = c.get(Calendar.YEAR);

		// Past
		if (time < System.currentTimeMillis()) {
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
			else if (nowDay - givenDay == 1 && (24 - givenHour) + nowHour > 3)
				return "Yesterday";
			else
				return "Now";
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
			else if (givenDay - nowDay == 1 && (24 - nowHour) + givenDay > 3)
				return "Today";
			else
				return "Now";
		}
	}
}
