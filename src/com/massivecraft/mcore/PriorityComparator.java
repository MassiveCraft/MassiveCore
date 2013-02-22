package com.massivecraft.mcore;

import java.util.Comparator;

public class PriorityComparator implements Comparator<Prioritized>
{
	@Override
	public int compare(Prioritized one, Prioritized two)
	{
		if (one == null && two == null) return 0;
		if (two == null) return 1;
		if (one == null) return -1;
		
		return Integer.valueOf(one.getPriority()).compareTo(two.getPriority());
	}
	
	// -------------------------------------------- //
	// INSTANCE
	// -------------------------------------------- //
	
	private static PriorityComparator i = new PriorityComparator();
	public static PriorityComparator get() { return i; }
	
}
