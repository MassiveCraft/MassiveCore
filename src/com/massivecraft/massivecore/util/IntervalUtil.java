package com.massivecraft.massivecore.util;

import java.util.AbstractMap.SimpleEntry;
import java.util.Map.Entry;

import com.massivecraft.massivecore.MassiveCore;

public class IntervalUtil
{	
	// -------------------------------------------- //
	// PARSING SIMPLE
	// -------------------------------------------- //
	
	public static Double parseDouble(String str, Double def)
	{
		if (str == null) return def;
		try
		{
			return Double.valueOf(str);
		}
		catch (Exception e)
		{
			return def;
		}
	}
	
	public static Integer parseInteger(String str, Integer def)
	{
		if (str == null) return def;
		try
		{
			return Integer.valueOf(str);
		}
		catch (Exception e)
		{
			return def;
		}
	}
	
	public static boolean isValidInterval(String interval)
	{
		return interval.matches("^.+to.+$");
	}
	
	// -------------------------------------------- //
	// PARSING ADVANCED
	// -------------------------------------------- //
	
	public static Entry<Double, Double> parseDoubleInterval(String interval, Double dmin, Double dmax)
	{
		if (interval == null)
		{
			return new SimpleEntry<Double, Double>(dmin, dmax);
		}
		
		if (interval.contains("to"))
		{
			String[] parts = interval.split("to");
			if (parts.length == 2)
			{
				Double min = parseDouble(parts[0], dmin);
				Double max = parseDouble(parts[1], dmax);
				return new SimpleEntry<Double, Double>(min, max);
			}
		}
		Double single = parseDouble(interval, dmin);
		return new SimpleEntry<Double, Double>(single, single);
	}
	
	public static Entry<Integer, Integer> parseIntegerInterval(String interval, Integer dmin, Integer dmax)
	{
		if (interval == null)
		{
			return new SimpleEntry<Integer, Integer>(dmin, dmax);
		}
		
		if (interval.contains("to"))
		{
			String[] parts = interval.split("to");
			if (parts.length == 2)
			{
				Integer min = parseInteger(parts[0], dmin);
				Integer max = parseInteger(parts[1], dmax);
				return new SimpleEntry<Integer, Integer>(min, max);
			}
		}
		Integer single = parseInteger(interval, dmin);
		return new SimpleEntry<Integer, Integer>(single, single);
	}
	
	// -------------------------------------------- //
	// RANDOM SIMPLE
	// -------------------------------------------- //
	
	public static int randomIntegerFromInterval(int min, int max)
	{
		return min+MassiveCore.random.nextInt(max-min+1);
	}
	
	public static int randomIntegerFromInterval(Entry<Integer, Integer> interval)
	{
		int min = interval.getKey();
		int max = interval.getValue();
		return randomIntegerFromInterval(min, max);
	}
	
	public static double randomDoubleFromInterval(double min, double max)
	{
		return min+MassiveCore.random.nextDouble()*(max-min);
	}
	
	public static double randomDoubleFromInterval(Entry<Double, Double> interval)
	{
		double min = interval.getKey();
		double max = interval.getValue();
		return randomDoubleFromInterval(min, max);
	}
	
	// -------------------------------------------- //
	// RANDOM COMBINED
	// -------------------------------------------- //
	
	public static Double randomDoubleFromInterval(String data, Double def)
	{
		if (isValidInterval(data))
		{
			Entry<Double, Double> interval = parseDoubleInterval(data, def, def);
			return randomDoubleFromInterval(interval);
		}
		else
		{
			return parseDouble(data, def);
		}
	}
	
	public static Integer randomIntegerFromInterval(String data, Integer def)
	{
		if (isValidInterval(data))
		{
			Entry<Integer, Integer> interval = parseIntegerInterval(data, def, def);
			return randomIntegerFromInterval(interval);
		}
		else
		{
			return parseInteger(data, def);
		}
	}
	
}
