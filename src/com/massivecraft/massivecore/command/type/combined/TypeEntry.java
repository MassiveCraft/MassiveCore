package com.massivecraft.massivecore.command.type.combined;

import java.util.List;
import java.util.Map.Entry;

import java.util.AbstractMap.SimpleEntry;

import com.massivecraft.massivecore.collections.MassiveList;
import com.massivecraft.massivecore.command.type.Type;

public class TypeEntry<K, V> extends TypeCombined<Entry<K, V>>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	public static <K, V> TypeEntry<K, V> get(Type<K> keyType, Type<V> valueType) { return new TypeEntry<K, V>(keyType, valueType); }
	public TypeEntry(Type<K> keyType, Type<V> valueType)
	{
		super(keyType, valueType);
	}
	
	// -------------------------------------------- //
	// INNER TYPES
	// -------------------------------------------- //
	
	public Type<K> getKeyType() { return this.getInnerType(0); }
	public Type<V> getValueType() { return this.getInnerType(1); }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public String getTypeName()
	{
		return this.getKeyType().getTypeName() + " and " + this.getValueType().getTypeName();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Entry<K, V> combine(List<Object> parts)
	{
		// Create
		K key = null;
		V value = null;

		// Fill
		for (int i = 0 ; i < parts.size() ; i++)
		{
			Object part = parts.get(i);
			
			if (i == 0)
			{
				key = (K)part;
			}
			else if (i == 1)
			{
				value = (V)part;
			}
		}
		
		// Return
		return new SimpleEntry<K, V>(key, value);
	}

	@Override
	public List<Object> split(Entry<K, V> entry)
	{
		return new MassiveList<Object>(
			entry.getKey(),
			entry.getValue()
		);
	}
	
	@Override
	public boolean equalsInner(Entry<K, V> type1, Entry<K, V> type2, boolean strict)
	{
		// Compare Keys
		K key1 = type1.getKey();
		K key2 = type2.getKey();
		if ( ! this.getKeyType().equals(key1, key2, strict)) return false;
		
		// Strict
		if ( ! strict) return true;
		
		// Compare Values
		V value1 = type1.getValue();
		V value2 = type2.getValue();
		if ( ! this.getValueType().equals(value1, value2, strict)) return false;
		
		// Done
		return true;
	}

}
