package com.massivecraft.massivecore.comparator;

import java.util.Objects;

public class ComparatorHashCode extends ComparatorAbstract<Object>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static transient ComparatorHashCode i = new ComparatorHashCode();
	public static ComparatorHashCode get() { return i; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //

	@Override
	public int compareInner(Object object1, Object object2)
	{
		int ret;
		
		ret = Integer.compare(Objects.hashCode(object1), Objects.hashCode(object2));
		if (ret != 0) return ret;
		
		ret = Integer.compare(System.identityHashCode(object1), System.identityHashCode(object2));
		if (ret != 0) return ret;
		
		return ret; 
	}

}
