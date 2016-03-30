package com.massivecraft.massivecore.command.type.enumeration;

import org.bukkit.block.banner.PatternType;

public class TypePatternType extends TypeEnum<PatternType>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static TypePatternType i = new TypePatternType();
	public static TypePatternType get() { return i; }
	public TypePatternType()
	{
		super(PatternType.class);
	}

}
