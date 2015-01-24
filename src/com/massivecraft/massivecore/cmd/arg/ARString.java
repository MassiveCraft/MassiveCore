package com.massivecraft.massivecore.cmd.arg;

import org.bukkit.command.CommandSender;

public class ARString extends ArgReaderAbstract<String>
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
	public String read(String arg, CommandSender sender)
	{
		return arg;
	}
	
}
