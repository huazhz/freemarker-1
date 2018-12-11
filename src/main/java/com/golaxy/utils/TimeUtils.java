package com.golaxy.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtils {
	
	public static String getDateStr(Long timeStamp) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
		String dateStr = sdf.format(new Date(timeStamp));
		return dateStr;
	}

	public static String getQuery(Long timeStamp) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateStr = sdf.format(new Date(timeStamp));
		return dateStr;
	}

}
