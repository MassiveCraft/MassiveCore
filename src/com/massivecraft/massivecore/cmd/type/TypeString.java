package com.massivecraft.massivecore.cmd.type;

import java.util.Collection;
import java.util.Collections;

import org.bukkit.command.CommandSender;

public class TypeString extends TypeAbstract<String>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static TypeString i = new TypeString();
	public static TypeString get() { return i; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //

	@Override
	public String getTypeName()
	{
		return "text";
	}
	
	@Override
	public String read(String arg, CommandSender sender)
	{
		return arg;
	}
	
	@Override
	public Collection<String> getTabList(CommandSender sender, String arg)
	{
		return Collections.emptySet();
	}

}
