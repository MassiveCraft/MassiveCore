package com.massivecraft.massivecore.comparator;

import java.util.Comparator;

import com.massivecraft.massivecore.store.Entity;

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
		
		int ret = ComparatorNaturalOrder.get().compare(id1, id2);
		if (ret != 0) return ret;
		
		// We should only return 0 if the items actually are equal.
		return e2.hashCode() - e1.hashCode();
	}
	
}
