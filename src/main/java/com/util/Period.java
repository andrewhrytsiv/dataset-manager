package com.util;

public class Period {
	
	private int dd;
	private int hh;
	private int mm;
	
	public Period(int minutes){
		dd = minutes / AppConstants.ONE_DAY_MINUTES; 
		hh = (minutes - dd * AppConstants.ONE_DAY_MINUTES) / AppConstants.ONE_HOUR_MINUTES;
		mm = minutes - dd * AppConstants.ONE_DAY_MINUTES - hh * AppConstants.ONE_HOUR_MINUTES;
	}
}
