package com.massivecraft.massivecore.comparator;

import com.massivecraft.massivecore.Identified;

public class ComparatorIdentified extends ComparatorAbstract<Object>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static ComparatorIdentified i = new ComparatorIdentified();
	public static ComparatorIdentified get() { return i; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //

	@Override
	public int compareInner(Object object1, Object object2)
	{
		// Create
		int ret = 0;
		
		// Instance Of
		Identified identified1 = null;
		Identified identified2 = null;
		if (object1 instanceof Identified) identified1 = (Identified)object1;
		if (object2 instanceof Identified) identified2 = (Identified)object2;
		ret = ComparatorNull.get().compareInner(identified1, identified2);
		if (ret != 0) return ret;
		if (identified1 == null && identified2 == null) return ret;
		
		// Id
		String id1 = identified1.getId();
		String id2 = identified2.getId();
		ret = ComparatorComparable.get().compare(id1, id2);
		if (ret != 0) return ret;
		
		// Return
		return ret;
	}

}
