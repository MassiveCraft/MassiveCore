package com.massivecraft.massivecore.command.type.enumeration;

import org.bukkit.Difficulty;

public class TypeDifficulty extends TypeEnum<Difficulty>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static TypeDifficulty i = new TypeDifficulty();
	public static TypeDifficulty get() { return i; }
	public TypeDifficulty()
	{
		super(Difficulty.class);
	}

}
