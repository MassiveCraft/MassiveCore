package com.massivecraft.massivecore.comparator;

import java.util.Comparator;

public class ComparatorReversed<T> extends ComparatorAbstractWrapper<T, T>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	public static <T> ComparatorReversed<T> get(Comparator<T> comparator) { return new ComparatorReversed<T>(comparator); }
	public ComparatorReversed(Comparator<T> comparator)
	{
		super(comparator);
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public int compare(T type1, T type2)
	{
		return - this.getComparator().compare(type1, type2);
	}
	
	@Override
	public ComparatorAbstract<T> getReversed()
	{
		Comparator<T> comparator = this.getComparator();
		if (comparator instanceof ComparatorAbstract<?>) return (ComparatorAbstract<T>) comparator;
		return super.getReversed();
	}
	
}
