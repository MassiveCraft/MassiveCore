package com.massivecraft.massivecore;

import java.util.*;

public class HashCodeComparator implements Comparator<Object>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static transient HashCodeComparator i = new HashCodeComparator();
	public static HashCodeComparator get() { return i; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //

	@Override
	public int compare(Object o1, Object o2)
	{
		return o2.hashCode() - o1.hashCode();
	}

}
