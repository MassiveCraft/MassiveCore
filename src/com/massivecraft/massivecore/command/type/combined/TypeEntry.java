package com.massivecraft.massivecore.command.type.combined;

import com.massivecraft.massivecore.command.type.Type;

import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;

public class TypeEntry<K, V> extends TypeCombined<Entry<K, V>>
{
	// -------------------------------------------- //
	// CONSTANTS
	// -------------------------------------------- //
	
	private static final Entry<?, ?> ENTRY_EMPTY = new SimpleImmutableEntry<>(null, null);
	@SuppressWarnings("unchecked") public static <K, V> Entry<K, V> getEntryEmpty() { return (Entry<K, V>) ENTRY_EMPTY; }
	
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	public static <K, V> TypeEntry<K, V> get(Type<K> keyType, Type<V> valueType) { return new TypeEntry<>(keyType, valueType); }
	public TypeEntry(Type<K> keyType, Type<V> valueType)
	{
		super(Entry.class, keyType, valueType);
		this.setTypeNameSeparator(" and ");
		this.setVisualSeparator(COLONSPACE);
		this.setVisualMsonSeparator(MSON_COLONSPACE);
	}
	
	// -------------------------------------------- //
	// INNER TYPES
	// -------------------------------------------- //
	
	public Type<K> getKeyType() { return this.getInnerType(0); }
	public Type<V> getValueType() { return this.getInnerType(1); }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //

	@SuppressWarnings("unchecked")
	@Override
	public Entry<K, V> combine(List<Object> parts)
	{
		if (parts.isEmpty()) return getEntryEmpty();
		if (parts.size() == 1) return new SimpleImmutableEntry<>((K)parts.get(0), null);
		if (parts.size() == 2) return new SimpleImmutableEntry<>((K)parts.get(0), (V) parts.get(1));
		
		throw new RuntimeException(parts.size() + " parts");
	}

	@Override
	public List<Object> split(Entry<K, V> entry)
	{
		return Arrays.asList(entry.getKey(), entry.getValue());
	}
	
	@Override
	public boolean equalsInner(Entry<K, V> type1, Entry<K, V> type2)
	{
		// Compare Keys
		K key1 = type1.getKey();
		K key2 = type2.getKey();
		if ( ! this.getKeyType().equals(key1, key2)) return false;
		
		// Compare Values
		V value1 = type1.getValue();
		V value2 = type2.getValue();
		if ( ! this.getValueType().equals(value1, value2)) return false;
		
		// Done
		return true;
	}

}
