package com.massivecraft.massivecore.cmd.arg;

import java.util.Arrays;
import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;

import com.massivecraft.massivecore.MassiveException;

public class ARMaterial extends ARAbstract<Material> implements ARAllAble<Material>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static ARMaterial i = new ARMaterial();
	public static ARMaterial get() { return i; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public Material read(String arg, CommandSender sender) throws MassiveException
	{
		Material ret = Material.matchMaterial(arg);
		if (ret == null)
		{
			throw new MassiveException()
			.addMsg("<b>No material matches <h>%s<b>.", arg)
			.addMsg("<i>https://hub.spigotmc.org/stash/projects/SPIGOT/repos/bukkit/browse/src/main/java/org/bukkit/Material.java");
		}
		
		return ret;
	}
	
	@Override
	public Collection<String> getTabList(CommandSender sender, String arg)
	{
		Set<String> ret = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
		
		for (Material material : Material.values())
		{
			ret.add(material.name());
		}
		
		return ret;
	}
	
	@Override
	public Collection<Material> getAll(CommandSender sender)
	{
		return Arrays.asList(Material.values());
	}

}
