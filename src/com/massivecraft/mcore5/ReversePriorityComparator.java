package com.massivecraft.mcore5;

import java.util.Comparator;

public class ReversePriorityComparator implements Comparator<Prioritized>
{
	@Override
	public int compare(Prioritized one, Prioritized two)
	{
		if (one == null && two == null) return 0;
		if (two == null) return -1;
		if (one == null) return 1;
		
		return Integer.valueOf(two.getPriority()).compareTo(one.getPriority());
	}
	
	// -------------------------------------------- //
	// INSTANCE
	// -------------------------------------------- //
	
	private static ReversePriorityComparator i = new ReversePriorityComparator();
	public static ReversePriorityComparator get() { return i; }
	
}
