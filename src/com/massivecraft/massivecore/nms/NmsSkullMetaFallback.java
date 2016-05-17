package com.massivecraft.massivecore.nms;

import java.util.UUID;

import org.bukkit.inventory.meta.SkullMeta;

public class NmsSkullMetaFallback extends NmsSkullMeta
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static NmsSkullMetaFallback i = new NmsSkullMetaFallback();
	public static NmsSkullMetaFallback get () { return i; }
	
	// -------------------------------------------- //
	// RAW
	// -------------------------------------------- //
	
	@Override
	public UUID getId(SkullMeta meta)
	{
		return null;
	}
	
	@Override
	public void set(SkullMeta meta, String name, UUID id)
	{
		meta.setOwner(name);
	}
	
}
