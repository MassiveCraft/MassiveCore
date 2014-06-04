package com.massivecraft.massivecore.cmd.arg;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.bukkit.block.Biome;
import org.bukkit.command.CommandSender;

public class ARBiome extends ARAbstractSelect<Biome>
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
	public String typename()
	{
		return "biome";
	}

	@Override
	public Biome select(String arg, CommandSender sender)
	{
		arg = getComparable(arg);
		
		String biomestr;
		for (Biome biome : Biome.values())
		{
			biomestr = getComparable(biome.name());
			if (biomestr.equals(arg))
			{
				return biome;
			}
			
			biomestr = String.valueOf(biome.ordinal());
			if (biomestr.equals(arg))
			{
				return biome;
			}
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
	
	// -------------------------------------------- //
	// UTIL
	// -------------------------------------------- //
	
	public static String getComparable(String str)
	{
		str = str.toLowerCase();
		str = str.replace("_", "");
		str = str.replace(" ", "");
		return str;
	}
	
}
