package com.massivecraft.mcore5.cmd.arg;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;

public class ARWorld extends ARAbstractSelect<World>
{
	@Override
	public String typename()
	{
		return "world";
	}

	@Override
	public World select(String str, CommandSender sender)
	{
		return Bukkit.getWorld(str);
	}

	@Override
	public List<String> altNames(CommandSender sender)
	{
		List<String> ret = new ArrayList<String>();
		for (World world : Bukkit.getWorlds())
		{
			ret.add(world.getName());
		}
		return ret;
	}
	
	// -------------------------------------------- //
	// INSTANCE
	// -------------------------------------------- //
	
	private static ARWorld i = new ARWorld();
	public static ARWorld get() { return i; }
	
}
