package com.massivecraft.massivecore;

import java.util.Comparator;

import com.massivecraft.massivecore.store.ComparatorEntityId;
import com.massivecraft.massivecore.store.Entity;

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
	public int compare(Prioritized p1, Prioritized p2)
	{
		// Null
		if (p1 == null && p2 == null) return 0;
		if (p1 == null) return -1;
		if (p2 == null) return +1;
		
		// Equals
		if (p1.equals(p2)) return 0;
		
		// Priority
		int ret = Integer.compare(p1.getPriority(), p2.getPriority());
		if (ret != 0) return ret;
		
		// Entity Id
		if (p1 instanceof Entity<?> && p2 instanceof Entity<?>)
		{
			Entity<?> e1 = (Entity<?>)p1;
			Entity<?> e2 = (Entity<?>)p2;
			return ComparatorEntityId.get().compare(e1, e2);
		}
		
		// We should only return 0 if the items actually are equal.
		return p2.hashCode() - p1.hashCode();
	}
	
}
