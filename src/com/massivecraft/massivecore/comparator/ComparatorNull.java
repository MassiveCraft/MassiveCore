package com.massivecraft.massivecore.comparator;

public class ComparatorNull extends ComparatorAbstract<Object>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static ComparatorNull i = new ComparatorNull();
	public static ComparatorNull get() { return i; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //

	@Override
	public int compareInner(Object object1, Object object2)
	{
		if (object1 == null && object2 == null) return 0;
		if (object1 == null) return -1;
		if (object2 == null) return +1;
		return 0;
	}

}
