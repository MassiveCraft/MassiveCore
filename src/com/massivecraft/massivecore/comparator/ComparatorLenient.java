package com.massivecraft.massivecore.comparator;

import java.util.Comparator;

public class ComparatorLenient<T> extends ComparatorAbstractWrapper<T, T>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	public static <T> ComparatorLenient<T> get(Comparator<T> comparator) { return new ComparatorLenient<T>(comparator); }
	public ComparatorLenient(Comparator<T> comparator)
	{
		super(comparator);
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public int compare(T type1, T type2)
	{
		int ret;
		
		ret = this.getComparator().compare(type1, type2);
		if (ret != 0) return ret;
		
		ret = ComparatorHashCode.get().compare(type1, type2);
		if (ret != 0) return ret;
		
		ret = ComparatorIdentityHashCode.get().compare(type1, type2);
		if (ret != 0) return ret;
		
		return 1;
	}
	
	@Override
	public ComparatorAbstract<T> getLenient()
	{
		return this;
	}
	
}
