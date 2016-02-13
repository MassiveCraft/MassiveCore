package com.massivecraft.massivecore.command.type.container;

import java.util.Map;
import java.util.Map.Entry;

import com.massivecraft.massivecore.collections.MassiveMap;
import com.massivecraft.massivecore.command.type.Type;
import com.massivecraft.massivecore.command.type.combined.TypeEntry;

public class TypeMap<K, V> extends TypeContainer<Map<K, V>, Entry<K, V>>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	public static <K, V> TypeMap<K, V> get(TypeEntry<K, V> entryType)
	{
		return new TypeMap<K, V>(entryType);
	}
	
	public TypeMap(TypeEntry<K, V> entryType)
	{
		super(entryType);
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
		return new MassiveMap<K, V>();
	}

}
