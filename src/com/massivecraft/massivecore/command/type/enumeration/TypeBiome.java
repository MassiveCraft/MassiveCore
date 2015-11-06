package com.massivecraft.massivecore.command.type.enumeration;

import org.bukkit.block.Biome;

public class TypeBiome extends TypeEnum<Biome>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static TypeBiome i = new TypeBiome();
	public static TypeBiome get() { return i; }
	public TypeBiome()
	{
		super(Biome.class);
	}

}
