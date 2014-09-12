package com.massivecraft.massivecore.cmd.arg;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;

public class AREntityType extends ARAbstractSelect<EntityType>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static AREntityType i = new AREntityType();
	public static AREntityType get() { return i; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public String typename()
	{
		return "entity type";
	}

	@Override
	public EntityType select(String arg, CommandSender sender)
	{
		arg = getComparable(arg);
		
		// Custom Detection
		if (arg.contains("pig") && (arg.contains("man") || arg.contains("zombie"))) return EntityType.PIG_ZOMBIE;
		
		// Algorithmic General Detection
		for (EntityType entityType : EntityType.values())
		{
			if (getComparable(entityType.toString()).equals(arg)) return entityType;
		}
		
		// Nothing found
		return null;
	}

	@Override
	public Collection<String> altNames(CommandSender sender)
	{
		List<String> ret = new ArrayList<String>();
		for (EntityType entityType : EntityType.values())
		{
			ret.add(getComparable(entityType.toString()));
		}
		return ret;
	}
	
	public static String getComparable(String string)
	{
		return string.toLowerCase().replaceAll("[_\\-\\s]+", "");
	}
	
}
