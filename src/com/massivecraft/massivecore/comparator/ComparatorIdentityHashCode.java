package com.massivecraft.massivecore.comparator;

public class ComparatorIdentityHashCode extends ComparatorAbstract<Object>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static transient ComparatorIdentityHashCode i = new ComparatorIdentityHashCode();
	public static ComparatorIdentityHashCode get() { return i; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //

	@Override
	public int compareInner(Object object1, Object object2)
	{
		int ret;
		
		ret = Integer.compare(System.identityHashCode(object1), System.identityHashCode(object2));
		if (ret != 0) return ret;
		
		return ret; 
	}

}
