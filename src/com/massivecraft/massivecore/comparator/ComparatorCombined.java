package com.massivecraft.massivecore.comparator;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class ComparatorCombined<T> extends ComparatorAbstract<T>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	@SafeVarargs
	public static <T> ComparatorCombined<T> get(Comparator<? super T>... comparators) { return new ComparatorCombined<>(comparators); }
	public static <T> ComparatorCombined<T> get(List<Comparator<? super T>> comparators) { return new ComparatorCombined<>(comparators); }
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	private List<Comparator<? super T>> comparators = null;
	public List<Comparator<? super T>> getComparators() { return this.comparators; }
	public ComparatorCombined<T> setComparators(List<Comparator<? super T>> comparators) { this.comparators = comparators; return this; }
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	@SafeVarargs
	public ComparatorCombined(Comparator<? super T>... comparators)
	{
		this(Arrays.asList(comparators));
	}
	
	public ComparatorCombined(List<Comparator<? super T>> comparators)
	{
		this.comparators = comparators;
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //

	@Override
	public int compareInner(T object1, T object2)
	{
		for (Comparator<? super T> comparator : this.getComparators())
		{
			int ret = comparator.compare(object1, object2);
			if (ret != 0) return ret;
		}
		return 0;
	}

}
