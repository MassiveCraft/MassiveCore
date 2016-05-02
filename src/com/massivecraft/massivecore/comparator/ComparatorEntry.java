package com.massivecraft.massivecore.comparator;

import java.util.Map.Entry;

public class ComparatorEntry extends ComparatorAbstract<Object>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static ComparatorEntry i = new ComparatorEntry();
	public static ComparatorEntry get() { return i; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //

	@SuppressWarnings("unchecked")
	@Override
	public int compareInner(Object object1, Object object2)
	{
		// Create
		int ret = 0;
		
		// Instance Of
		Entry<Object, Object> entry1 = null;
		Entry<Object, Object> entry2 = null;
		if (object1 instanceof Entry<?, ?>) entry1 = (Entry<Object, Object>)object1;
		if (object2 instanceof Entry<?, ?>) entry2 = (Entry<Object, Object>)object2;
		ret = ComparatorNull.get().compare(entry1, entry2);
		if (ret != 0) return ret;
		if (entry1 == null && entry2 == null) return ret;
		
		// Keys
		Object key1 = entry1.getKey();
		Object key2 = entry2.getKey();
		ret = ComparatorSmart.get().compare(key1, key2);
		if (ret != 0) return ret;
		
		// Values
		Object value1 = entry1.getValue();
		Object value2 = entry2.getValue();
		ret = ComparatorSmart.get().compare(value1, value2);
		if (ret != 0) return ret;
		
		// Return
		return ret;
	}

}
