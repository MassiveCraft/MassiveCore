package com.massivecraft.mcore.cmd.arg;

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

	@SuppressWarnings("deprecation")
	@Override
	public EntityType select(String arg, CommandSender sender)
	{
		return EntityType.fromName(arg);
	}

	@SuppressWarnings("deprecation")
	@Override
	public Collection<String> altNames(CommandSender sender)
	{
		List<String> ret = new ArrayList<String>();
		for (EntityType entityType : EntityType.values())
		{
			ret.add(entityType.getName());
		}
		return ret;
	}
	
}
