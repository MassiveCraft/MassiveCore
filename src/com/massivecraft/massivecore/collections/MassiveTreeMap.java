package com.massivecraft.massivecore.collections;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

/**
 * This subclass adds better constructors.
 * It also includes the comparator as a Generic for automatic use with GSON. 
 */
public class MassiveTreeMap<K, V, C extends Comparator<? super K>> extends TreeMap<K, V>
{
	// -------------------------------------------- //
	// CONSTANTS
	// -------------------------------------------- //
	
	private static final long serialVersionUID = 1L;
	
	// -------------------------------------------- //
	// CONSTRUCT: BASE
	// -------------------------------------------- //
	
	@SuppressWarnings("unchecked")
	public MassiveTreeMap(Object comparator)
	{
		super((comparator instanceof Comparator) ? (C)comparator : null);
	}
	
	public MassiveTreeMap(Object comparator, Map<? extends K, ? extends V> map)
	{
		// Support Null & this(comparator)
		this(comparator);
		if (map != null) putAll(map);
	}
	
	// -------------------------------------------- //
	// CONSTRUCT: EXTRA
	// -------------------------------------------- //
	
	public MassiveTreeMap(Object comparator, K key1, V value1, Object... objects)
	{
		this(comparator, MassiveMap.varargCreate(key1, value1, objects));
	}

}
