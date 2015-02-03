package com.massivecraft.massivecore.cmd.arg;

import java.util.Collection;
import java.util.Collections;

import org.bukkit.command.CommandSender;

public class ARString extends ARAbstract<String>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static ARString i = new ARString();
	public static ARString get() { return i; }
	
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
