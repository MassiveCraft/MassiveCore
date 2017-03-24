package com.massivecraft.massivecore.command.type.container;

import com.massivecraft.massivecore.collections.MassiveMap;
import com.massivecraft.massivecore.command.type.Type;
import com.massivecraft.massivecore.command.type.combined.TypeEntry;

import java.util.Map;
import java.util.Map.Entry;

public class TypeMap<K, V> extends TypeContainer<Map<K, V>, Entry<K, V>>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	public static <K, V> TypeMap<K, V> get(Type<K> keyType, Type<V> valueType)
	{
		return get(TypeEntry.get(keyType, valueType));
	}
	
	public static <K, V> TypeMap<K, V> get(TypeEntry<K, V> entryType)
	{
		return new TypeMap<>(entryType);
	}
	
	public TypeMap(TypeEntry<K, V> entryType)
	{
		super(Map.class, entryType);
	}
	
	// -------------------------------------------- //
	// INNER TYPES
	// -------------------------------------------- //
	
	public TypeEntry<K, V> getEntryType() { return this.getInnerType(); }
	public Type<K> getKeyType() { return this.getEntryType().getKeyType(); }
	public Type<V> getValueType() { return this.getEntryType().getValueType(); }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //

	@Override
	public Map<K, V> createNewInstance()
	{
		return new MassiveMap<>();
	}

}
