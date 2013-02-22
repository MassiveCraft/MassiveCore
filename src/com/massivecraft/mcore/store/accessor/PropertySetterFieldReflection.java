package com.massivecraft.mcore.store.accessor;

import java.lang.reflect.Field;

public class PropertySetterFieldReflection implements PropertySetter
{
	private final Field field;
	
	public PropertySetterFieldReflection(Field field)
	{
		AccessorUtil.makeAccessible(field);
		this.field = field;
	}
	
	@Override
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
