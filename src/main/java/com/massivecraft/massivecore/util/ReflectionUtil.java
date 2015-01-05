package com.massivecraft.massivecore.util;

import java.lang.reflect.Field;

public class ReflectionUtil
{
	public static Object getField(Class<?> clazz, String fieldName, Object object)
	{
		try
		{
			Field field = clazz.getDeclaredField(fieldName);
			field.setAccessible(true);
			return field.get(object);
		}
		catch (Exception e)
		{
			return null;
		}
	}
	
	public static boolean setField(Class<?> clazz, String fieldName, Object object, Object value)
	{
		try
		{
			Field field = clazz.getDeclaredField(fieldName);
			field.setAccessible(true);
			field.set(object, value);
			return true;
		}
		catch (Exception e)
		{
			return false;
		}
	}
}
