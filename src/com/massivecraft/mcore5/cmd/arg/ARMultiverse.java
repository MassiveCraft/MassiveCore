package com.massivecraft.mcore5.cmd.arg;

import java.util.Collection;

import org.bukkit.command.CommandSender;

import com.massivecraft.mcore5.Permission;
import com.massivecraft.mcore5.usys.Multiverse;
import com.massivecraft.mcore5.usys.MultiverseColl;

public class ARMultiverse extends ARAbstractSelect<Multiverse>
{
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
	
	// -------------------------------------------- //
	// INSTANCE
	// -------------------------------------------- //
	
	private static ARMultiverse i = new ARMultiverse();
	public static ARMultiverse get() { return i; }
	
}
