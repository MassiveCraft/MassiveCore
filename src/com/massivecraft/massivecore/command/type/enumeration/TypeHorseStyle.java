package com.massivecraft.massivecore.command.type.enumeration;

import org.bukkit.entity.Horse.Style;

public class TypeHorseStyle extends TypeEnum<Style>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static TypeHorseStyle i = new TypeHorseStyle();
	public static TypeHorseStyle get() { return i; }
	public TypeHorseStyle()
	{
		super(Style.class);
	}

}
