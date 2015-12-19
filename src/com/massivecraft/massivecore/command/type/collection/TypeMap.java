package com.massivecraft.massivecore.command.type.collection;

import java.util.HashMap;
import java.util.Map;

import com.massivecraft.massivecore.collections.MassiveMap;
import com.massivecraft.massivecore.command.type.Type;

public class TypeMap<K,V> extends TypeMapAbstract<Map<K, V>, K, V>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //

	public static <K,V> TypeMap<K,V> get(Type<K> keyType, Type<V> valueType)
	{
		return new TypeMap<K,V>(keyType, valueType);
	}

	public TypeMap(Type<K> keyType, Type<V> valueType)
	{
		super(keyType, valueType);
	}

	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //

	@Override
	public HashMap<K,V> createNewInstance()
	{
		return new MassiveMap<K,V>();
	}

}
