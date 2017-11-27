package com.rayanfadhlaoui.controler.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.log4j.Logger;

public class DateUtils {
	
	private static Logger logger = Logger.getLogger(DateUtils.class);
	
	private final static String DATE_PATTERN = "dd/mm/yyyy";
	private static SimpleDateFormat sdf = new SimpleDateFormat(DATE_PATTERN);
	
	public static Date parse(String dateAsString) {
		try {
			return sdf.parse(dateAsString);
		} catch (ParseException e) {
			logger.warn("unparsable date :" + dateAsString);
			return null;
		}
	}

	public static String display(Date birthdate) {
		return sdf.format(birthdate);
	}
	
}
