/*
 * Author: 	Praveen Kumar Pendyala
 * Project: MDroid
 * Created:	28-12-2013
 * 
 * © 2013, Praveen Kumar Pendyala. 
 * Licensed under the Creative Commons Attribution-NonCommercial-ShareAlike 
 * 3.0 Unported license, http://creativecommons.org/licenses/by-nc-sa/3.0/ 
 * 
 * This is a part of the MDroid project. You may use the contents of this file
 * or the project only if you comply with license of the project, available at the 
 * Github repo: https://github.com/praveendath92/MDroid/blob/master/README.md
 * 
 */

package in.co.praveenkumar.mdroid.helpers;

import java.util.Date;

public class RelativeLastModified {
	private static final int SECOND_MILLIS = 1000;
	private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
	private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
	private static final int DAY_MILLIS = 24 * HOUR_MILLIS;

	public RelativeLastModified() {

	}

	public String getRelativeTime(Date rDate) {
		long time = rDate.getTime();

		return getRelativeTimeFromMilliSec(time);

	}

	public String getRelativeTimeFromMilliSec(long time) {
		long now = System.currentTimeMillis();

		if (time > now || time <= 0) {
			return "NaN";
		}

		final long diff = now - time;
		if (diff < MINUTE_MILLIS) {
			return "Just now";
		} else if (diff < 2 * MINUTE_MILLIS) {
			return "A minute ago";
		} else if (diff < 50 * MINUTE_MILLIS) {
			return diff / MINUTE_MILLIS + " minutes ago";
		} else if (diff < 90 * MINUTE_MILLIS) {
			return "An hour ago";
		} else if (diff < 24 * HOUR_MILLIS) {
			return diff / HOUR_MILLIS + " hours ago";
		} else if (diff < 48 * HOUR_MILLIS) {
			return "Yesterday";
		} else {
			return diff / DAY_MILLIS + " days ago";
		}
	}

}
