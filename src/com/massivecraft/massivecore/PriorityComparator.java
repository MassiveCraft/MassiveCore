package com.massivecraft.massivecore;

import java.util.Comparator;

public class PriorityComparator implements Comparator<Prioritized>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static PriorityComparator i = new PriorityComparator();
	public static PriorityComparator get() { return i; }
	
	// -------------------------------------------- //
	// OVERRIDE: COMPARATOR
	// -------------------------------------------- //
	
	@Override
	public int compare(Prioritized one, Prioritized two)
	{
		if (one == null && two == null) return 0;
		if (two == null) return 1;
		if (one == null) return -1;
		
		int ret = Integer.valueOf(one.getPriority()).compareTo(two.getPriority());
		
		// We should only return 0 if the items actually are equal.
		if (ret == 0 && ! one.equals(two))
		{
			ret = two.hashCode() - one.hashCode();
		}
		
		return ret;
	}
	
}
