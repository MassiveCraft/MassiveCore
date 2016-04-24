package com.massivecraft.massivecore.comparator;

import java.util.Objects;

public class ComparatorHashCode extends ComparatorAbstract<Object>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static ComparatorHashCode i = new ComparatorHashCode();
	public static ComparatorHashCode get() { return i; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //

	@Override
	public int compareInner(Object object1, Object object2)
	{
		return Integer.compare(Objects.hashCode(object1), Objects.hashCode(object2));
	}

}
