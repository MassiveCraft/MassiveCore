package com.massivecraft.massivecore.comparator;

import com.massivecraft.massivecore.Prioritized;

public class ComparatorPrioritized extends ComparatorAbstract<Object>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static ComparatorPrioritized i = new ComparatorPrioritized();
	public static ComparatorPrioritized get() { return i; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //

	@Override
	public int compareInner(Object object1, Object object2)
	{
		// Create
		int ret = 0;
		
		// Instance Of
		Prioritized prioritized1 = null;
		Prioritized prioritized2 = null;
		if (object1 instanceof Prioritized) prioritized1 = (Prioritized)object1;
		if (object2 instanceof Prioritized) prioritized2 = (Prioritized)object2;
		ret = ComparatorNull.get().compare(prioritized1, prioritized2);
		if (ret != 0) return ret;
		if (prioritized1 == null && prioritized2 == null) return ret;
		
		// Priority
		int priority1 = prioritized1.getPriority();
		int priority2 = prioritized2.getPriority();
		
		ret = Integer.compare(priority1, priority2);
		if (ret != 0) return ret;
		
		// Return
		return ret;
	}

}
