package com.massivecraft.massivecore.command.type.primitive;

import org.bukkit.command.CommandSender;

import com.massivecraft.massivecore.util.Txt;

public class TypeStringParsed extends TypeString
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static TypeStringParsed i = new TypeStringParsed();
	public static TypeStringParsed get() { return i; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //

	@Override
	public String getName()
	{
		return "colored text";
	}
	
	@Override
	public String read(String arg, CommandSender sender)
	{
		return Txt.parse(super.read(arg, sender));
	}

}
