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
		int ret = this.getComparator().compare(type1, type2);
		if (ret != 0) return ret;
		
		int hash1 = type1.hashCode();
		int hash2 = type2.hashCode();
			
		ret = Integer.compare(hash1, hash2);
		if (ret != 0) return ret;
		
		return 1;
	}
	
	@Override
	public ComparatorAbstract<T> getLenient()
	{
		return this;
	}
	
}
