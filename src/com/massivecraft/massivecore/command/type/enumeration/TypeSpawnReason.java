package com.massivecraft.massivecore.command.type.enumeration;

import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

public class TypeSpawnReason extends TypeEnum<SpawnReason>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static TypeSpawnReason i = new TypeSpawnReason();
	public static TypeSpawnReason get() { return i; }
	public TypeSpawnReason()
	{
		super(SpawnReason.class);
	}
	
}
