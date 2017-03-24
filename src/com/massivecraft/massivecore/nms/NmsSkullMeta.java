package com.massivecraft.massivecore.nms;

import com.massivecraft.massivecore.Couple;
import com.massivecraft.massivecore.mixin.Mixin;
import com.massivecraft.massivecore.util.IdData;
import com.massivecraft.massivecore.util.IdUtil;
import com.massivecraft.massivecore.util.MUtil;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.UUID;

public class NmsSkullMeta extends Mixin
{
	// -------------------------------------------- //
	// DEFAULT
	// -------------------------------------------- //
	
	private static NmsSkullMeta d = new NmsSkullMeta().setAlternatives(
		NmsSkullMeta18R1P.class,
		NmsSkullMeta17R4.class,
		NmsSkullMetaFallback.class
	);
	
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static NmsSkullMeta i = d;
	public static NmsSkullMeta get() { return i; }
	
	// -------------------------------------------- //
	// RAW
	// -------------------------------------------- //
	
	public String getName(SkullMeta meta)
	{
		// throw notImplemented();
		return meta.getOwner();
		// NOTE: This one is actually this simple.
		// Bukkit does all the work for us.
	}
	
	public UUID getId(SkullMeta meta)
	{
		throw notImplemented();
	}
	
	public void set(SkullMeta meta, String name, UUID id)
	{
		throw notImplemented();
	}
	
	// -------------------------------------------- //
	// RESOLVE
	// -------------------------------------------- //
	// We resolve the locally best possible information using IdUtil.
	
	public Couple<String, UUID> resolve(SkullMeta meta)
	{
		String name = this.getName(meta);
		UUID id = this.getId(meta);
		return this.resolve(name, id);
	}
	
	public Couple<String, UUID> resolve(String name, UUID id)
	{
		// Create Ret
		// We default to the input.
		String retName = name;
		UUID retId = id;
		
		// Fetch IdData
		// First by name then id. 
		IdData data = null;
		if (name != null) data = IdUtil.getNameToData().get(name);
		if (data == null && id != null) data = IdUtil.getIdToData().get(id.toString());
		
		// Use that data if found
		if (data != null)
		{
			retName = data.getName();
			retId = MUtil.asUuid(data.getId());
		}
		
		// Return Ret
		return new Couple<>(retName, retId);
	}
	
}
