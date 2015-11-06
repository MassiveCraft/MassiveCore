package com.massivecraft.massivecore.command.type.enumeration;

import org.bukkit.GameMode;

public class TypeGameMode extends TypeEnum<GameMode>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static TypeGameMode i = new TypeGameMode();
	public static TypeGameMode get() { return i; }
	public TypeGameMode()
	{
		super(GameMode.class);
	}

}
