package com.massivecraft.massivecore.command.type.enumeration;

import org.bukkit.entity.Horse.Color;

public class TypeHorseColor extends TypeEnum<Color>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static TypeHorseColor i = new TypeHorseColor();
	public static TypeHorseColor get() { return i; }
	public TypeHorseColor()
	{
		super(Color.class);
	}

}
