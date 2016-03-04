package com.massivecraft.massivecore.comparator;

import java.util.Comparator;

import com.massivecraft.massivecore.Named;
import com.massivecraft.massivecore.Prioritized;
import com.massivecraft.massivecore.store.Entity;
import com.massivecraft.massivecore.util.MUtil;

public class ComparatorAbstract<T> implements Comparator<T>
{
	// -------------------------------------------- //
	// REVERSED
	// -------------------------------------------- //
	
	private ComparatorReversed<T> reversed = null;
	public ComparatorAbstract<T> getReversed()
	{
		if (this.reversed == null) this.reversed = ComparatorReversed.get(this);
		return this.reversed;
	}
	
	// -------------------------------------------- //
	// LENIENT
	// -------------------------------------------- //
	
	private ComparatorLenient<T> lenient = null;
	public ComparatorAbstract<T> getLenient()
	{
		if (this.lenient == null) this.lenient = ComparatorLenient.get(this);
		return this.lenient;
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public int compare(T type1, T type2)
	{
		Integer ret;
		
		// Null
		ret = MUtil.compareNulls(type1, type2);
		if (ret != null) return ret;
		
		// Inner
		ret = this.compareInner(type1, type2);
		if (ret != null) return ret;
		
		// Prioritized
		if (type1 instanceof Prioritized)
		{
			Prioritized prioritized1 = (Prioritized)type1;
			Prioritized prioritized2 = (Prioritized)type2;
			
			ret = Integer.compare(prioritized1.getPriority(), prioritized2.getPriority());
			if (ret != null) return ret;
		}
		
		// Named
		if (type1 instanceof Named)
		{
			Named named1 = (Named)type1;
			Named named2 = (Named)type2;
			
			ret = ComparatorNaturalOrder.get().compare(named1.getName(), named2.getName());
			if (ret != null) return ret;
		}
		
		// EntityId
		if (type1 instanceof Entity<?>)
		{
			Entity<?> entity1 = (Entity<?>)type1;
			Entity<?> entity2 = (Entity<?>)type2;
			ret = MUtil.compare(entity1.getId(), entity2.getId());
			if (ret != null) return ret;
		}
		
		// Identity
		return ComparatorIdentity.get().compare(type1, type2);
	}
	
	// -------------------------------------------- //
	// INNER
	// -------------------------------------------- //
	
	public Integer compareInner(T type1, T type2)
	{
		return null;
	}
	
}
