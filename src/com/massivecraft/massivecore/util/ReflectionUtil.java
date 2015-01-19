package com.massivecraft.massivecore.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

public class ReflectionUtil
{
	// -------------------------------------------- //
	// CONSTANTS
	// -------------------------------------------- //
	
	private static Field FIELD_DOT_MODIFIERS;
	
	static
	{
		try
		{
			FIELD_DOT_MODIFIERS = Field.class.getDeclaredField("modifiers");
			FIELD_DOT_MODIFIERS.setAccessible(true);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	// -------------------------------------------- //
	// MAKE ACCESSIBLE
	// -------------------------------------------- //
	
	public static boolean makeAccessible(Field field)
	{
		try
		{
			// Mark the field as accessible using reflection.
			field.setAccessible(true);
			
			// Remove the final modifier from the field.
			// http://stackoverflow.com/questions/2474017/using-reflection-to-change-static-final-file-separatorchar-for-unit-testing
			FIELD_DOT_MODIFIERS.setInt(field, field.getModifiers() & ~Modifier.FINAL);
			
			return true;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}
	
	// -------------------------------------------- //
	// FIELD GET
	// -------------------------------------------- //
	
	public static Field getField(Class<?> clazz, String fieldName)
	{
		try
		{
			Field field = clazz.getDeclaredField(fieldName);
			makeAccessible(field);
			return field;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	public static Object getField(Field field, Object object)
	{
		try
		{
			return field.get(object);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	// -------------------------------------------- //
	// FIELD SET
	// -------------------------------------------- //
	
	public static void setField(Field field, Object object, Object value)
	{
		try
		{
			field.set(object, value);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	// -------------------------------------------- //
	// FIELD SIMPLE: GET & SET & TRANSFER
	// -------------------------------------------- //
	
	public static Object getField(Class<?> clazz, String fieldName, Object object)
	{
		try
		{
			Field field = clazz.getDeclaredField(fieldName);
			makeAccessible(field);
			return field.get(object);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	public static boolean setField(Class<?> clazz, String fieldName, Object object, Object value)
	{
		try
		{
			Field field = clazz.getDeclaredField(fieldName);
			makeAccessible(field);
			field.set(object, value);
			return true;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}
	
	public static boolean transferField(Class<?> clazz, Object from, Object to, String fieldName)
	{
		try
		{
			Field field = clazz.getDeclaredField(fieldName);
			makeAccessible(field);
			Object value = field.get(from);
			field.set(to, value);
			return true;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}
	
	public static boolean transferFields(Class<?> clazz, Object from, Object to, List<String> fieldNames)
	{
		if (fieldNames == null)
		{
			fieldNames = new ArrayList<String>();
			for (Field field : clazz.getDeclaredFields())
			{
				fieldNames.add(field.getName());
			}
		}
		
		for (String fieldName : fieldNames)
		{
			if ( ! transferField(clazz, from, to, fieldName)) return false;
		}
		
		return true;
	}
	
	public static boolean transferFields(Class<?> clazz, Object from, Object to)
	{
		return transferFields(clazz, from, to, null);
	}
	

	
}
