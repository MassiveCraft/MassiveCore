package com.massivecraft.massivecore;

import java.util.Comparator;

public class ReversePriorityComparator implements Comparator<Prioritized>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static ReversePriorityComparator i = new ReversePriorityComparator();
	public static ReversePriorityComparator get() { return i; }
	
	// -------------------------------------------- //
	// OVERRIDE: COMPARATOR
	// -------------------------------------------- //
	
	@Override
	public int compare(Prioritized one, Prioritized two)
	{
		if (one == null && two == null) return 0;
		if (two == null) return -1;
		if (one == null) return 1;
		
		int ret = Integer.valueOf(two.getPriority()).compareTo(one.getPriority());
		
		// We should only return 0 if the items actually are equal.
		if (ret == 0 && ! one.equals(two))
		{
			ret = one.hashCode() - two.hashCode();
		}
		
		return ret;
	}
	
}
