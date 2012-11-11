package com.massivecraft.mcore5.store.accessor;

import java.lang.reflect.Method;

public class PropertyGetterMethodReflection implements PropertyGetter
{
	private final Method method;
	
	public PropertyGetterMethodReflection(Method method)
	{
		AccessorUtil.makeAccessible(method);
		this.method = method;
	}
	
	@Override
	public Object get(Object entity)
	{
		try
		{
			return this.method.invoke(entity, new Object[0]);			
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}

}
