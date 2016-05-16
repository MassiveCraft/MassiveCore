package com.massivecraft.massivecore.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import com.massivecraft.massivecore.predicate.Predicate;

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
	
	private static final Class<?>[] EMPTY_ARRAY_OF_CLASS = {};
	private static final Object[] EMPTY_ARRAY_OF_OBJECT = {};
	
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
			throw asRuntimeException(e);
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
			throw asRuntimeException(e);
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
			throw asRuntimeException(e);
		}
	}
	
	// -------------------------------------------- //
	// METHOD
	// -------------------------------------------- //
	
	public static Method getMethod(Class<?> clazz, String name, Class<?>... parameterTypes)
	{
		try
		{
			Method ret = clazz.getDeclaredMethod(name, parameterTypes);
			makeAccessible(ret);
			return ret;
		}
		catch (Exception e)
		{
			throw asRuntimeException(e);
		}
	}
	
	public static boolean hasMethod(Class<?> clazz, String name)
	{
		return hasMethod(clazz, name, EMPTY_ARRAY_OF_CLASS);
	}
	
	public static boolean hasMethod(Class<?> clazz, String name, Class<?>... parameterTypes)
	{
		try
		{
			getMethod(clazz, name, parameterTypes);
			return true;
		}
		catch (Exception e)
		{
			return false;
		}
	}
	
	public static Method getMethod(Class<?> clazz, String name)
	{
		return getMethod(clazz, name, EMPTY_ARRAY_OF_CLASS);
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
			throw asRuntimeException(e);
		}
	}
	
	public static <T> T invokeMethod(Method method, Object target, Object argument)
	{
		return invokeMethod(method, target, new Object[]{argument});
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T invokeMethod(Method method, Object target)
	{
		return (T) invokeMethod(method, target, EMPTY_ARRAY_OF_OBJECT);
	}
	
	// -------------------------------------------- //
	// CONSTRUCTOR
	// -------------------------------------------- //
	
	@SuppressWarnings("unchecked")
	public static <T> Constructor<T> getConstructor(Class<?> clazz, Class<?>... parameterTypes)
	{
		try
		{
			Constructor<T> ret = (Constructor<T>) clazz.getDeclaredConstructor(parameterTypes);
			makeAccessible(ret);
			return ret;
		}
		catch (Exception e)
		{
			throw asRuntimeException(e);
		}
	}
	
	public static <T> Constructor<T> getConstructor(Class<?> clazz)
	{
		return getConstructor(clazz, EMPTY_ARRAY_OF_CLASS);
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T invokeConstructor(Constructor<?> constructor, Object... arguments)
	{
		try
		{
			return (T) constructor.newInstance(arguments);
		}
		catch (Exception e)
		{
			throw asRuntimeException(e);
		}
	}
	
	public static <T> T invokeConstructor(Constructor<?> constructor, Object argument)
	{
		return invokeConstructor(constructor, new Object[]{argument});
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T invokeConstructor(Constructor<?> constructor)
	{
		return (T) invokeConstructor(constructor, EMPTY_ARRAY_OF_OBJECT);
	}
	
	// -------------------------------------------- //
	// SINGLETON INSTANCE
	// -------------------------------------------- //
	
	public static <T> T getSingletonInstance(Class<?> clazz)
	{
		Method get = getMethod(clazz, "get");
		T ret = invokeMethod(get, null);
		if (ret == null) throw new NullPointerException("Singleton instance was null for: " + clazz);
		if ( ! clazz.isAssignableFrom(ret.getClass())) throw new IllegalStateException("Singleton instance was not of same or subclass for: " + clazz);
		return ret;
	}
	
	public static <T> T getSingletonInstanceFirstCombatible(Iterable<Class<?>> classes, T fallback)
	{
		for (Class<?> c : classes)
		{
			try
			{
				return ReflectionUtil.getSingletonInstance(c);
			}
			catch (Throwable t)
			{
				// Not Compatible
			}
		}
		return fallback;
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
			throw asRuntimeException(e);
		}
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
			throw asRuntimeException(e);
		}
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
			throw asRuntimeException(e);
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
	
	public static Class<?> getSuperclassDeclaringField(Class<?> clazz, boolean includeSelf, final String fieldName)
	{
		return getSuperclassPredicate(clazz, includeSelf, new Predicate<Class<?>>()
		{
			@Override
			public boolean apply(Class<?> clazz)
			{
				for (Field field : clazz.getDeclaredFields())
				{
					if (field.getName().equals(fieldName)) return true;
				}
				return false;
			}
		});
	}
	
	// -------------------------------------------- //
	// AS RUNTIME EXCEPTION
	// -------------------------------------------- //
	
	public static RuntimeException asRuntimeException(Throwable t)
	{
		// Runtime
		if (t instanceof RuntimeException) return (RuntimeException) t;
		
		// Invocation
		if (t instanceof InvocationTargetException) return asRuntimeException(((InvocationTargetException)t).getCause());
		
		// Rest
		return new IllegalStateException(t.getClass().getSimpleName() + ": " + t.getMessage());
	}
	
}
