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

import android.annotation.SuppressLint;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RelativeLastModified {
	public RelativeLastModified() {

	}

	@SuppressLint("SimpleDateFormat")
	@SuppressWarnings("deprecation")
	public String getRelativeTime(Date rDate) {
		// Replace with SimpleDateFormat if worried about deprecation
		// Default shall be used when there is an error. System date wrong
		SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
		String defaultRelTime = format.format(rDate);

		int rYear = rDate.getYear();
		int rMonth = rDate.getMonth();
		int rDay = rDate.getDay();
		int rHrs = rDate.getHours();
		int rMins = rDate.getMinutes();
		int rSecs = rDate.getSeconds();

		// Current date
		Date cDate = new Date(System.currentTimeMillis());
		int cYear = cDate.getYear();
		int cMonth = cDate.getMonth();
		int cDay = cDate.getDay();
		int cHrs = cDate.getHours();
		int cMins = cDate.getMinutes();
		int cSecs = cDate.getSeconds();

		if (cYear - rYear > 0)
			return (cYear - rYear) + " years ago";

		if (cMonth - rMonth > 0)
			return (cMonth - rMonth) + " months ago";

		if (cDay - rDay > 0)
			return (cDay - rDay) + " days ago";

		if (cHrs - rHrs > 0)
			return (cHrs - rHrs) + " hours ago";

		if (cMins - rMins > 0)
			return (cMins - rMins) + " minutes ago";

		if (cSecs - rSecs > 0)
			return (cSecs - rSecs) + " seconds ago";

		return defaultRelTime;
	}

}
