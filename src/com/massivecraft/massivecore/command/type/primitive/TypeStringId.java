package com.massivecraft.massivecore.command.type.primitive;

import org.bukkit.ChatColor;

public class TypeStringId extends TypeString
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static TypeStringId i = new TypeStringId();
	public static TypeStringId get() { return i; }
	public TypeStringId()
	{
		this.setVisualColor(ChatColor.GRAY);
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //

	@Override
	public String getName()
	{
		return "text id";
	}
	
}
