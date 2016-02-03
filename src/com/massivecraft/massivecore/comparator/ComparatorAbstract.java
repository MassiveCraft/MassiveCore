package com.massivecraft.massivecore.comparator;

import java.util.Comparator;

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
		// Create
		Integer ret;
		
		// Null
		ret = MUtil.compareNulls(type1, type2);
		if (ret != null) return ret;
		
		// Inner
		ret = this.compareInner(type1, type2);
		
		// Return
		return ret;
	}
	
	// -------------------------------------------- //
	// INNER
	// -------------------------------------------- //
	
	public int compareInner(T type1, T type2)
	{
		throw new UnsupportedOperationException("not implemented");
	}
	
}
