package com.massivecraft.massivecore.comparator;

import com.massivecraft.massivecore.Prioritized;
import com.massivecraft.massivecore.store.Entity;

public class ComparatorPriority extends ComparatorAbstract<Prioritized>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static ComparatorPriority i = new ComparatorPriority();
	public static ComparatorPriority get() { return i; }
	
	// -------------------------------------------- //
	// OVERRIDE: COMPARATOR
	// -------------------------------------------- //
	
	@Override
	public Integer compareInner(Prioritized prioritized1, Prioritized prioritized2)
	{
		// Equals
		if (prioritized1.equals(prioritized2)) return 0;
		
		// Priority
		Integer ret = Integer.compare(prioritized1.getPriority(), prioritized2.getPriority());
		if (ret != 0) return ret;
		
		// Entity Id
		if (prioritized1 instanceof Entity<?> && prioritized2 instanceof Entity<?>)
		{
			Entity<?> entity1 = (Entity<?>)prioritized1;
			Entity<?> entity2 = (Entity<?>)prioritized2;
			return ComparatorEntityId.get().compare(entity1, entity2);
		}
		
		// We should only return 0 if the items actually are equal.
		return ComparatorIdentity.get().compare(prioritized1, prioritized2);
	}
	
}
