package com.massivecraft.massivecore.store.accessor;

import com.massivecraft.massivecore.util.ReflectionUtil;

import java.lang.reflect.Field;

public class FieldAccessorSimple implements FieldAccessor
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	private final Field field;
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public FieldAccessorSimple(Field field)
	{
		ReflectionUtil.makeAccessible(field);
		this.field = field;
	}
	
	// -------------------------------------------- //
	// CORE
	// -------------------------------------------- //
	
	public Object get(Object entity)
	{
		if (!field.getDeclaringClass().isAssignableFrom(entity.getClass())) throw new IllegalArgumentException(field.getDeclaringClass() + " : " + entity.getClass());
		
		try
		{
			return this.field.get(entity);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	public void set(Object entity, Object val)
	{
		try
		{
			this.field.set(entity, val);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

}
