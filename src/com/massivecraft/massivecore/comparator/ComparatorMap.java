package com.massivecraft.massivecore.comparator;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class ComparatorMap extends ComparatorAbstract<Object>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static ComparatorMap i = new ComparatorMap();
	public static ComparatorMap get() { return i; }
	
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
		Map<Object, Object> map1 = null;
		Map<Object, Object> map2 = null;
		if (object1 instanceof Map<?, ?>) map1 = (Map<Object, Object>)object1;
		if (object2 instanceof Map<?, ?>) map2 = (Map<Object, Object>)object2;
		ret = ComparatorNull.get().compare(map1, map2);
		if (ret != 0) return ret;
		if (map1 == null && map2 == null) return ret;
		
		// Entries
		Set<Entry<Object, Object>> entries1 = map1.entrySet();
		Set<Entry<Object, Object>> entries2 = map2.entrySet();
		ret = ComparatorCollection.get().compare(entries1, entries2);
		if (ret != 0) return ret;
		
		// Return
		return ret;
	}

}
