package com.massivecraft.massivecore;

import java.util.List;

import org.bukkit.metadata.MetadataValue;
import org.bukkit.metadata.MetadataValueAdapter;
import org.bukkit.metadata.Metadatable;
import org.bukkit.plugin.Plugin;

public class MetadataSimple extends MetadataValueAdapter
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	private Object value;
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public MetadataSimple(Plugin plugin, Object value)
	{
		super(plugin);
		this.value = value;
	}

	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	@Override
	public Object value()
	{
		return this.value;
	}

	@Override
	public void invalidate()
	{
		// This can not be invalidated
	}

	// -------------------------------------------- //
	// UTIL
	// -------------------------------------------- //
	
	public static void set(Plugin plugin, Metadatable metadatable, String key, Object value)
	{
		metadatable.setMetadata(key, new MetadataSimple(plugin, value));
	}
	
	public static MetadataValue getMeta(Metadatable metadatable, String key)
	{
		List<MetadataValue> metaValues = metadatable.getMetadata(key);
		if (metaValues == null) return null;
		if (metaValues.size() < 1) return null;
		return metaValues.get(0);
	}
	
	public static Object get(Metadatable metadatable, String key)
	{
		MetadataValue metaValue = getMeta(metadatable, key);
		if (metaValue == null) return null;
		return metaValue.value();
	}
	
}
