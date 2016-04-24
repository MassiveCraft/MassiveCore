package com.massivecraft.massivecore.comparator;

public class ComparatorComparable extends ComparatorAbstract<Object>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static transient ComparatorComparable i = new ComparatorComparable();
	public static ComparatorComparable get() { return i; }
	
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
		Comparable<Object> comparable1 = null;
		Comparable<Object> comparable2 = null;
		if (object1 instanceof Comparable) comparable1 = (Comparable<Object>)object1;
		if (object2 instanceof Comparable) comparable2 = (Comparable<Object>)object2;
		ret = ComparatorNull.get().compare(comparable1, comparable2);
		if (ret != 0) return ret;
		if (comparable1 == null && comparable2 == null) return ret;
		
		// Compare
		ret = comparable1.compareTo(comparable2);
		if (ret != 0) return ret;
		
		// Return
		return ret;
	}
	
}
