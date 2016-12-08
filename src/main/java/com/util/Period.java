package com.util;

public class Period {
	
	private Integer dd = 0;
	private Integer hh = 0;
	private Integer mm = 0;
	
	public Period(int minutes){
		dd = minutes / AppConstants.ONE_DAY_MINUTES; 
		hh = (minutes - dd * AppConstants.ONE_DAY_MINUTES) / AppConstants.ONE_HOUR_MINUTES;
		mm = minutes - dd * AppConstants.ONE_DAY_MINUTES - hh * AppConstants.ONE_HOUR_MINUTES;
	}
}
