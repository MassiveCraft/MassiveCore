package com.massivecraft.massivecore.cmd.arg;

import java.util.Collection;

import org.bukkit.command.CommandSender;

import com.massivecraft.massivecore.store.Coll;

public class ARColl extends ARAbstractSelect<Coll<?>> implements ARAllAble<Coll<?>>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static ARColl i = new ARColl();
	public static ARColl get() { return i; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //

	@Override
	public Coll<?> select(String arg, CommandSender sender)
	{
		return Coll.getMap().get(arg);
	}
	
	@Override
	public Collection<String> altNames(CommandSender sender)
	{
		return Coll.getNames();
	}

	@Override
	public Collection<String> getTabList(CommandSender sender, String arg)
	{
		return this.altNames(sender);
	}

	@Override
	public Collection<Coll<?>> getAll(CommandSender sender)
	{
		return Coll.getMap().values();
	}
	
}
