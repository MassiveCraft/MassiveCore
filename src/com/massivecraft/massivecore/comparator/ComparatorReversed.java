package com.massivecraft.massivecore.comparator;

import java.util.Comparator;

public class ComparatorReversed<T> extends ComparatorAbstractWrapper<T, T>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	public static <T> ComparatorReversed<T> get(Comparator<T> comparator) { return new ComparatorReversed<>(comparator); }
	public ComparatorReversed(Comparator<T> comparator)
	{
		super(comparator);
		this.setReversed(true);
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public int compareInner(T type1, T type2)
	{
		return - this.getComparator().compare(type1, type2);
	}
	
}
