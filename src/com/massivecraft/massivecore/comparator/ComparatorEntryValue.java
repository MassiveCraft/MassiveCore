package com.massivecraft.massivecore.comparator;

import java.util.Comparator;
import java.util.Map.Entry;

public class ComparatorEntryValue<K, V> extends ComparatorAbstractTransformer<Entry<K, V>, V>
{	
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	public static <K, V> ComparatorEntryValue<K, V> get(Comparator<V> comparator) { return new ComparatorEntryValue<>(comparator); }
	public ComparatorEntryValue(Comparator<V> comparator)
	{
		super(comparator);
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //

	@Override
	public V transform(Entry<K, V> type)
	{
		return type.getValue();
	}
	
}
