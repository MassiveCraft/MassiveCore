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
		
		return Integer.valueOf(two.getPriority()).compareTo(one.getPriority());
	}
	
}
