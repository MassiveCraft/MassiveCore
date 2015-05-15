package com.massivecraft.massivecore.store;

import java.util.Comparator;

import com.massivecraft.massivecore.NaturalOrderComparator;

public class ComparatorEntityId implements Comparator<Entity<?>>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //

	private static ComparatorEntityId i = new ComparatorEntityId();
	public static ComparatorEntityId get() { return i; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //

	@Override
	public int compare(Entity<?> e1, Entity<?> e2)
	{	
		if (e1 == null && e2 == null) return 0;
		if (e1 == null) return -1;
		if (e2 == null) return +1;
		
		if (e1.equals(e2)) return 0;
		
		String id1 = e1.getId();
		String id2 = e2.getId();
		
		int ret = NaturalOrderComparator.get().compare(id1, id2);
		
		// Only return 0 if they are the same.
		// We avoid that with this ugly solution.
		if (ret == 0) ret = -1;
		
		return ret;
	}
	
}
