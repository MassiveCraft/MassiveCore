package com.massivecraft.mcore.store.accessor;

import java.lang.reflect.Field;

public class PropertyGetterFieldReflection implements PropertyGetter
{
	private final Field field;
	
	public PropertyGetterFieldReflection(Field field)
	{
		AccessorUtil.makeAccessible(field);
		this.field = field;
	}
	
	@Override
	public Object get(Object entity)
	{
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

}
