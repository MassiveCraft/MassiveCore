package com.massivecraft.massivecore.collections;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * This subclass adds better constructors. 
 */
public class MassiveMap<K, V> extends LinkedHashMap<K, V>
{
	// -------------------------------------------- //
	// CONSTANTS
	// -------------------------------------------- //
	
	private static final long serialVersionUID = 1L;
	
	// -------------------------------------------- //
	// CONSTRUCT: BASE
	// -------------------------------------------- //
	
	public MassiveMap(int initialCapacity, float loadFactor)
	{
	    super(initialCapacity, loadFactor);
	}

	public MassiveMap(int initialCapacity)
	{
	    super(initialCapacity);
	}

	public MassiveMap()
	{
	    super();
	}

	@SuppressWarnings("unchecked")
	public MassiveMap(Map<? extends K, ? extends V> m)
	{
		// Support Null
		super(m == null ? Collections.EMPTY_MAP : m);
	}

	public MassiveMap(int initialCapacity, float loadFactor, boolean accessOrder)
	{
	    super(initialCapacity, loadFactor, accessOrder);
	}
	
	// -------------------------------------------- //
	// CONSTRUCT: EXTRA
	// -------------------------------------------- //
	
	public MassiveMap(K key1, V value1, Object... objects)
	{
		this(varargCreate(key1, value1, objects));
	}
	
	// -------------------------------------------- //
	// UTIL
	// -------------------------------------------- //
	
	@SuppressWarnings("unchecked")
	public static <K, V> MassiveMap<K, V> varargCreate(K key1, V value1, Object... objects)
	{
		MassiveMap<K, V> ret = new MassiveMap<K, V>();
		
		ret.put(key1, value1);
		
		Iterator<Object> iter = Arrays.asList(objects).iterator();
		while (iter.hasNext())
		{
			K key = (K) iter.next();
			V value = (V) iter.next();
			ret.put(key, value);
		}
		
		return ret;
	}

}
