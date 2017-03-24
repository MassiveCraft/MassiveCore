package com.massivecraft.massivecore.comparator;

import java.util.Comparator;
import java.util.Map.Entry;

public class ComparatorEntryKey<K, V> extends ComparatorAbstractTransformer<Entry<K, V>, K>
{	
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	public static <K, V> ComparatorEntryKey<K, V> get(Comparator<K> comparator) { return new ComparatorEntryKey<>(comparator); }
	public ComparatorEntryKey(Comparator<K> comparator)
	{
		super(comparator);
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //

	@Override
	public K transform(Entry<K, V> type)
	{
		return type.getKey();
	}
	
}
