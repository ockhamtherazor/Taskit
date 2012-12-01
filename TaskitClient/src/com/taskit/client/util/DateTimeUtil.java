/**
 * DateTimeUtil.java
 * 2012-11-30 OckhamTheRazor
 */
package com.taskit.client.util;

import java.util.Calendar;

public class DateTimeUtil {
	
	public String getDateString(Calendar calendar) {
		
		String dateString = String.valueOf(calendar.get(Calendar.MONTH) + 1)
				+ "/"
				+ String.valueOf(calendar.get(Calendar.DAY_OF_MONTH))
				+ "/"
				+ String.valueOf(calendar.get(Calendar.YEAR));
		
		return dateString;
		
	}
	
	public String getTimeString(Calendar calendar) {
		
		String timeString = String.valueOf(calendar.get(Calendar.HOUR_OF_DAY))
				+ ":"
				+ String.valueOf(calendar.get(Calendar.MINUTE));
		
		return timeString;
		
	}
	
}
