package com.massivecraft.massivecore.store.accessor;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Accessor
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	private final Class<?> clazz;
	public Class<?> getClazz() { return this.clazz; }
		
	private Map<String, FieldAccessor> fieldToAccessor = new LinkedHashMap<String, FieldAccessor>();
	public Map<String, FieldAccessor> getFieldToAccessor() {	return this.fieldToAccessor; }
	
	public FieldAccessor getFieldAccessor(String fieldName)
	{
		FieldAccessor ret = this.fieldToAccessor.get(fieldName);
		if (ret == null) throw new IllegalArgumentException("The field \""+fieldName+"\" is not supported.");
		return ret;
	}
	
	public void setFieldAccessor(String fieldName, FieldAccessor fieldAccessor)
	{
		this.fieldToAccessor.put(fieldName, fieldAccessor);
	}
	
	public Collection<String> getFieldNames()
	{
		return this.fieldToAccessor.keySet();
	}
	
	// -------------------------------------------- //
	// CONSTRUCT / FACTORY
	// -------------------------------------------- //
	
	private static Map<Class<?>, Accessor> classToAccessor = new HashMap<Class<?>, Accessor>();
	
	public static Accessor get(Class<?> clazz)
	{
		Accessor ret = classToAccessor.get(clazz);
		if (ret != null) return ret;
		return new Accessor(clazz);
	}
	
	private Accessor(Class<?> clazz)
	{
		this.clazz = clazz;
		this.populate();
		classToAccessor.put(clazz, this);
	}
	
	// -------------------------------------------- //
	// POPULATE: REFLECTION
	// -------------------------------------------- //
	
	public void populate()
	{
		Map<String, Field> map = getFieldMap(this.clazz);
		for (Entry<String, Field> entry : map.entrySet())
		{
			String fieldName = entry.getKey();
			Field field = entry.getValue();
			FieldAccessor fieldAccessor = new FieldAccessor(field);
			this.setFieldAccessor(fieldName, fieldAccessor);
		}
	}
	
	// -------------------------------------------- //
	// GET & SET & COPY
	// -------------------------------------------- //
	
	public Object get(Object object, String fieldName)
	{
		FieldAccessor fieldAccessor = this.getFieldAccessor(fieldName);
		return fieldAccessor.get(object);
	}
	
	public void set(Object object, String fieldName, Object val)
	{
		FieldAccessor fieldAccessor = this.getFieldAccessor(fieldName);
		fieldAccessor.set(object, val);
	}

	// Copy one only!
	public void copy(Object from, Object to, String fieldName)
	{
		FieldAccessor fieldAccessor = this.getFieldAccessor(fieldName);
		Object val = fieldAccessor.get(from);
		fieldAccessor.set(to, val);
	}
	
	// Copy a few!
	public void copy(Object from, Object to, Collection<String> fieldNames)
	{
		for (String fieldName : fieldNames)
		{
			this.copy(from, to, fieldName);
		}
	}
	
	// Copy them all!
	public void copy(Object from, Object to)
	{
		for (FieldAccessor fieldAccessor : this.getFieldToAccessor().values())
		{
			Object val = fieldAccessor.get(from);
			fieldAccessor.set(to, val);
		}
	}
	
	// -------------------------------------------- //
	// UTIL
	// -------------------------------------------- //
	
	public static List<Field> getFieldList(Class<?> clazz)
	{
		List<Field> fields = new ArrayList<Field>();
		
		for (Class<?> c = clazz; c != null; c = c.getSuperclass())
		{
			fields.addAll(Arrays.asList(c.getDeclaredFields()));
		}
		
		return fields;
	}
	
	public static Map<String, Field> getFieldMap(Class<?> clazz)
	{
		Map<String, Field> ret = new LinkedHashMap<String, Field>();
		
		for (Field field : getFieldList(clazz))
		{
			if (Modifier.isTransient(field.getModifiers())) continue;
			if (Modifier.isFinal(field.getModifiers())) continue;
			String fieldName = field.getName();
			if (ret.containsKey(fieldName)) continue;
			field.setAccessible(true);
			ret.put(fieldName, field);
		}
		
		return ret;
	}
	
}
