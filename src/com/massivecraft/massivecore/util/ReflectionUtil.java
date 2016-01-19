package com.massivecraft.massivecore.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.ArrayList;
import java.util.List;

import com.massivecraft.massivecore.Predicate;

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
	
	public static void makeAccessible(Field field)
	{
		try
		{
			// Mark as accessible using reflection.
			field.setAccessible(true);
			
			// Remove the final modifier from the field.
			// http://stackoverflow.com/questions/2474017/using-reflection-to-change-static-final-file-separatorchar-for-unit-testing
			FIELD_DOT_MODIFIERS.setInt(field, field.getModifiers() & ~Modifier.FINAL);
		}
		catch (Exception e)
		{
			rethrow(e);
		}
	}
	
	public static void makeAccessible(Method method)
	{
		try
		{
			// Mark as accessible using reflection.
			method.setAccessible(true);
		}
		catch (Exception e)
		{
			rethrow(e);
		}
	}
	
	public static void makeAccessible(Constructor<?> constructor)
	{
		try
		{
			// Mark as accessible using reflection.
			constructor.setAccessible(true);
		}
		catch (Exception e)
		{
			rethrow(e);
		}
	}
	
	// -------------------------------------------- //
	// METHOD
	// -------------------------------------------- //
	
	public static Method getMethod(Class<?> clazz, String name, Class<?>... parameterTypes)
	{
		try
		{
			Method ret = clazz.getMethod(name, parameterTypes);
			makeAccessible(ret);
			return ret;
		}
		catch (Exception e)
		{
			rethrow(e);
		}
		throw new RuntimeException();
	}
	public static Method getMethod(Class<?> clazz, String name)
	{
		return getMethod(clazz, name, new Class<?>[0]);
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T invokeMethod(Method method, Object target, Object... arguments)
	{
		try
		{
			return (T) method.invoke(target, arguments);
		}
		catch (Exception e)
		{
			rethrow(e);
		}
		throw new RuntimeException();
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T invokeMethod(Method method, Object target)
	{
		return (T) invokeMethod(method, target, new Object[0]);
	}
	
	// -------------------------------------------- //
	// FIELD > GET
	// -------------------------------------------- //
	
	public static Field getField(Class<?> clazz, String name)
	{
		try
		{
			Field ret = clazz.getDeclaredField(name);
			makeAccessible(ret);
			return ret;
		}
		catch (Exception e)
		{
			rethrow(e);
		}
		throw new RuntimeException();
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T getField(Field field, Object object)
	{
		try
		{
			return (T) field.get(object);
		}
		catch (Exception e)
		{
			rethrow(e);
		}
		throw new RuntimeException();
	}
	
	// -------------------------------------------- //
	// FIELD > SET
	// -------------------------------------------- //
	
	public static void setField(Field field, Object object, Object value)
	{
		try
		{
			field.set(object, value);
		}
		catch (Exception e)
		{
			rethrow(e);
		}
	}
	
	// -------------------------------------------- //
	// FIELD > SIMPLE
	// -------------------------------------------- //
	
	public static <T> T getField(Class<?> clazz, String name, Object object)
	{
		Field field = getField(clazz, name);
		return getField(field, object);
	}
	
	public static void setField(Class<?> clazz, String name, Object object, Object value)
	{
		Field field = getField(clazz, name);
		setField(field, object, value);
	}
	
	// -------------------------------------------- //
	// FIELD > TRANSFER
	// -------------------------------------------- //
	
	public static void transferField(Class<?> clazz, Object from, Object to, String name)
	{
		Field field = getField(clazz, name);
		Object value = getField(field, from);
		setField(field, to, value);
	}
	
	public static void transferFields(Class<?> clazz, Object from, Object to, List<String> fieldNames)
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
			transferField(clazz, from, to, fieldName);
		}
	}
	
	public static void transferFields(Class<?> clazz, Object from, Object to)
	{
		transferFields(clazz, from, to, null);
	}
	
	// -------------------------------------------- //
	// SUPERCLASSES
	// -------------------------------------------- //
	
	public static List<Class<?>> getSuperclasses(Class<?> clazz, boolean includeSelf)
	{
		// Create
		List<Class<?>> ret = new ArrayList<Class<?>>();
		
		// Fill
		if ( ! includeSelf) clazz = clazz.getSuperclass();
		while (clazz != null)
		{
			ret.add(clazz);
			clazz = clazz.getSuperclass();
		}
		
		// Return
		return ret;
	}
	
	public static Class<?> getSuperclassPredicate(Class<?> clazz, boolean includeSelf, Predicate<Class<?>> predicate)
	{
		for (Class<?> superClazz : getSuperclasses(clazz, includeSelf))
		{
			if (predicate.apply(superClazz)) return superClazz;
		}
		return null;
	}
	
	public static Class<?> getSuperclassDeclaringMethod(Class<?> clazz, boolean includeSelf, final String methodName)
	{
		return getSuperclassPredicate(clazz, includeSelf, new Predicate<Class<?>>()
		{
			@Override
			public boolean apply(Class<?> clazz)
			{
				for (Method method : clazz.getDeclaredMethods())
				{
					if (method.getName().equals(methodName)) return true;
				}
				return false;
			}
		});
	}
	
	// -------------------------------------------- //
	// RETHROW
	// -------------------------------------------- //
	// This method is used to convert throwables into nicer runtime versions.
	
	public static void rethrow(Throwable e)
	{
		// Error
		if (e instanceof Error) throw (Error)e;
		
		// Not Found
		if (e instanceof ClassNotFoundException) throw new IllegalStateException("Class not Found: " + e.getMessage());
		if (e instanceof NoSuchMethodException) throw new IllegalStateException("Method not Found: " + e.getMessage());
		if (e instanceof NoSuchFieldException) throw new IllegalStateException("Field not Found: " + e.getMessage());
		
		// Security
		if (e instanceof SecurityException) throw new IllegalStateException("Security was Violated: " + e.getMessage());
		
		// Derp
		if (e instanceof IllegalAccessException) throw new IllegalStateException("Access was Illegal: " + e.getMessage());
		if (e instanceof IllegalArgumentException) throw new IllegalStateException("Inappropriate Arguments: " + e.getMessage());
		if (e instanceof InvocationTargetException) rethrow(((InvocationTargetException)e).getCause());
		
		// Runtime
		if (e instanceof RuntimeException) throw (RuntimeException)e;
		
		// Other
		throw new UndeclaredThrowableException(e);
	}
	
}
