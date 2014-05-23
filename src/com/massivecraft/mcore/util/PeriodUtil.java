package com.massivecraft.mcore.util;

import java.util.HashMap;
import java.util.Map;

public class PeriodUtil
{
	// -------------------------------------------- //
	// MILLIS STORE
	// -------------------------------------------- //
	
	private static Map<Object, Long> objectToMillis = new HashMap<Object, Long>();
	
	public static long getMillis(Object object)
	{
		Long ret = objectToMillis.get(object);
		if (ret == null) ret = 0L;
		return ret;
	}
	
	public static void setMillis(Object object, Long millis)
	{
		if (millis == null || millis == 0)
		{
			objectToMillis.remove(object);
		}
		else
		{
			objectToMillis.put(object, millis);
		}
	}
	
	// -------------------------------------------- //
	// RANDOM SIMPLE
	// -------------------------------------------- //
	
	public static long getPeriod(long length, long now)
	{
		return now / length;
	}
	
	public static long getPeriod(long length)
	{
		return getPeriod(length, System.currentTimeMillis());
	}
	
	public static long getLastPeriod(Object object, long length)
	{
		return getPeriod(length, getMillis(object));
	}
	
	public static boolean isNewPeriod(Object object, long length, long now)
	{
		long currentPeriod = getPeriod(length, now);
		long lastPeriod = getLastPeriod(object, length);
		
		if (currentPeriod == lastPeriod) return false;
		
		setMillis(object, now);
		
		return true;
	}
	
	public static boolean isNewPeriod(Object object, long length)
	{
		return isNewPeriod(object, length, System.currentTimeMillis());
	}
	
}
