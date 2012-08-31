package com.massivecraft.mcore4.store.accessor;

import java.util.HashMap;
import java.util.Map;

public final class Accessor
{
	private static Map<Class<?>, EntityAccessor> class2EntityAccessor = new HashMap<Class<?>, EntityAccessor>();

	public static EntityAccessor get(Class<?> clazz)
	{
		EntityAccessor ret = class2EntityAccessor.get(clazz);
		if (ret == null)
		{
			ret = new EntityAccessorPerProperty(clazz);
			class2EntityAccessor.put(clazz, ret);
		}
		return ret;
	}
}
