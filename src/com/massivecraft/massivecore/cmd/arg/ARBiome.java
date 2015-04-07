package com.massivecraft.massivecore.cmd.arg;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.bukkit.block.Biome;
import org.bukkit.command.CommandSender;

public class ARBiome extends ARAbstractSelect<Biome> implements ARAllAble<Biome>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static ARBiome i = new ARBiome();
	public static ARBiome get() { return i; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //

	@Override
	public Biome select(String arg, CommandSender sender)
	{
		arg = getComparable(arg);
		
		for (Biome biome : Biome.values())
		{
			String biomestr = getComparable(biome);
			if (biomestr.equals(arg)) return biome;
			
			biomestr = String.valueOf(biome.ordinal());
			if (biomestr.equals(arg)) return biome;
		}
		
		return null;
	}

	@Override
	public Collection<String> altNames(CommandSender sender)
	{
		List<String> ret = new ArrayList<String>();
		
		for (Biome biome : Biome.values())
		{
			ret.add(String.valueOf(biome.ordinal()));
			ret.add(biome.toString());
		}
		
		return ret;
	}

	@Override
	public Collection<String> getTabList(CommandSender sender, String arg)
	{
		List<String> ret = new ArrayList<String>();
		
		for (Biome biome : Biome.values())
		{
			ret.add(getComparable(biome.name()));
		}
		
		return ret;
	}
	
	
	@Override
	public Collection<Biome> getAll(CommandSender sender)
	{
		return Arrays.asList(Biome.values());
	}
	
	// -------------------------------------------- //
	// UTIL
	// -------------------------------------------- //
	
	public static String getComparable(Biome biome)
	{
		if (biome == null) return null;
		return getComparable(biome.name());
	}
	
	public static String getComparable(String string)
	{
		if (string == null) return null;
		string = string.toLowerCase();
		string = string.replace("_", "");
		string = string.replace(" ", "");
		return string;
	}
	
}
