package com.massivecraft.mcore5.store.accessor;

import java.lang.reflect.Method;

public class PropertySetterMethodReflection implements PropertySetter
{
	private final Method method;
	
	public PropertySetterMethodReflection(Method method)
	{
		AccessorUtil.makeAccessible(method);
		this.method = method;
	}
	
	@Override
	public void set(Object entity, Object val)
	{
		try
		{
			this.method.invoke(entity, val);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

}
