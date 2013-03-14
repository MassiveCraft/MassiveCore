package com.massivecraft.mcore.cmd.arg;

import java.util.Collection;

import org.bukkit.command.CommandSender;

import com.massivecraft.mcore.Permission;
import com.massivecraft.mcore.usys.Multiverse;
import com.massivecraft.mcore.usys.MultiverseColl;

public class ARMultiverse extends ARAbstractSelect<Multiverse>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static ARMultiverse i = new ARMultiverse();
	public static ARMultiverse get() { return i; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public String typename()
	{
		return "multiverse";
	}

	@Override
	public Multiverse select(String arg, CommandSender sender)
	{
		return MultiverseColl.i.get(arg);
	}
	
	@Override
	public boolean canList(CommandSender sender)
	{
		return Permission.CMD_USYS_MULTIVERSE_LIST.has(sender, false);
	}

	@Override
	public Collection<String> altNames(CommandSender sender)
	{
		return MultiverseColl.i.getIds();
	}
	
}
