package com.massivecraft.mcore5.store.accessor;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AccessorUtil
{

	//----------------------------------------------//
	// MAKE ACCESSIBLE
	//----------------------------------------------//
	
	public static void makeAccessible(Field field)
	{
		if ((!Modifier.isPublic(field.getModifiers()) || !Modifier.isPublic(field.getDeclaringClass().getModifiers()) || Modifier.isFinal(field.getModifiers())) && !field.isAccessible())
		{
			field.setAccessible(true);
		}
	}
	
	public static void makeAccessible(Method method)
	{
		if ((!Modifier.isPublic(method.getModifiers()) || !Modifier.isPublic(method.getDeclaringClass().getModifiers())) && !method.isAccessible())
		{
			method.setAccessible(true);
		}
	}
	
	//----------------------------------------------//
	// FIND
	//----------------------------------------------//
	
	public static List<Method> findMethod(Class<?> clazz, String name)
	{
		List<Method> ret = new ArrayList<Method>();
		for (Class<?> c = clazz; c != null; c = c.getSuperclass())
		{
			Method[] methods = (c.isInterface() ? c.getMethods() : c.getDeclaredMethods());
			for (Method method : methods)
			{
				if (name.equals(method.getName())) ret.add(method);
			}
		}
		return ret;
	}
	
	public static Field findField(Class<?> clazz, String name)
	{
		for (Class<?> c = clazz; c != null && !Object.class.equals(c); c = c.getSuperclass())
		{
			Field[] fields = c.getDeclaredFields();
			for (Field field : fields)
			{
				if (name.equals(field.getName())) return field;
			}
		}
		return null;
	}
	
	public static List<Field> findAllFields(Class<?> clazz)
	{
		List<Field> fields = new ArrayList<Field>();
		for (Class<?> c = clazz; c != null; c = c.getSuperclass())
		{
			fields.addAll(Arrays.asList(c.getDeclaredFields()));
		}
		return fields;
	}
	
	/**
	 * Must be non-transient.
	 * Must be non-final.
	 * Will be set accessible if possible.
	 */
	public static Map<String, Field> getFieldMap(Class<?> clazz)
	{
		Map<String, Field> ret = new HashMap<String, Field>();
		
		for (Field field : findAllFields(clazz))
		{
			if (Modifier.isTransient(field.getModifiers())) continue;
			if (Modifier.isFinal(field.getModifiers())) continue;
			makeAccessible(field);
			ret.put(field.getName(), field);
		}
		
		return ret;
	}
	
	//----------------------------------------------//
	// FIND GETTERS AND SETTERS
	//----------------------------------------------//
	
	public static String ucfirst(String str)
	{
		return Character.toUpperCase(str.charAt(0))+str.substring(1);
	}
	public static String calcGetterNameBool(String fieldName) { return "is"+ucfirst(fieldName); }
	public static String calcGetterName(String fieldName) { return "get"+ucfirst(fieldName); }
	public static String calcSetterName(String fieldName) { return "set"+ucfirst(fieldName); }
	
	// TODO: Use a predictate?
	public static Method findGetter(Class<?> clazz, String fieldName)
	{
		for (Method method : findMethod(clazz, calcGetterName(fieldName)))
		{
			if (method.getParameterTypes().length == 0 && method.getReturnType() != null) return method;
		}
		
		for (Method method : findMethod(clazz, calcGetterNameBool(fieldName)))
		{
			if (method.getParameterTypes().length == 0 && method.getReturnType() == Boolean.class) return method;
		}
		
		return null;
	}
	
	public static Method findSetter(Class<?> clazz, String fieldName)
	{
		List<Method> methods = findMethod(clazz, calcSetterName(fieldName));
		methods.addAll(findMethod(clazz, fieldName));
		
		for (Method method : methods)
		{
			if (method != null && method.getParameterTypes().length == 1) return method;
		}
		
		return null;
	}
	
	//----------------------------------------------//
	// CREATE PROPERTY ACCESS
	//----------------------------------------------//
	
	public static PropertyGetter createPropertyGetter(Class<?> clazz, String name)
	{
		Method method = findGetter(clazz, name);
		if (method != null) return new PropertyGetterMethodReflection(method);
		
		Field field = findField(clazz, name);
		if (field != null) return new PropertyGetterFieldReflection(field);
		
		return null;
	}
	
	public static PropertySetter createPropertySetter(Class<?> clazz, String name)
	{
		Method method = findSetter(clazz, name);
		if (method != null) return new PropertySetterMethodReflection(method);
		
		Field field = findField(clazz, name);
		if (field != null) return new PropertySetterFieldReflection(field);
		
		return null;
	}
	
	public static PropertyAccessor createPropertyAccessor(Class<?> clazz, String name)
	{
		PropertyGetter getter = createPropertyGetter(clazz, name);
		if (getter == null) return null;
		
		PropertySetter setter = createPropertySetter(clazz, name);
		if (setter == null) return null;
		
		return new PropertyAccessorComposite(getter, setter);
	}
	
	
}
