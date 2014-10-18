package com.massivecraft.massivecore.collections;

import java.util.Comparator;
import java.util.Map;

/**
 * This subclass does nothing new except implementing the Def interface.
 * Def is short for "Default" and means GSON should handle "null" as "empty".
 */
public class MassiveTreeMapDef<K, V, C extends Comparator<? super K>> extends MassiveTreeMap<K, V, C> implements Def
{
	// -------------------------------------------- //
	// CONSTANTS
	// -------------------------------------------- //
	
	private static final long serialVersionUID = 1L;
	
	// -------------------------------------------- //
	// CONSTRUCT: SUPER
	// -------------------------------------------- //
	
	public MassiveTreeMapDef(Object comparator)
	{
		super(comparator);
	}
	
	public MassiveTreeMapDef(Object comparator, Map<? extends K, ? extends V> map)
	{
		super(comparator, map);
	}

	public MassiveTreeMapDef(Object comparator, K key1, V value1, Object... objects)
	{
		super(comparator, key1, value1, objects);
	}
	
}
