package com.massivecraft.massivecore.cmd.arg;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.CommandSender;

import com.massivecraft.massivecore.mixin.Mixin;

public class ARWorldId extends ARAbstractSelect<String>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static ARWorldId i = new ARWorldId();
	public static ARWorldId get() { return i; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public String typename()
	{
		return "world";
	}

	@Override
	public String select(String arg, CommandSender sender)
	{
		List<String> visibleWorldIds = Mixin.getVisibleWorldIds(sender);
		
		for (String worldId : visibleWorldIds)
		{
			if ( ! Mixin.canSeeWorld(sender, worldId)) continue;
			if (arg.equalsIgnoreCase(worldId)) return worldId;
		}
		
		for (String worldId : visibleWorldIds)
		{
			if ( ! Mixin.canSeeWorld(sender, worldId)) continue;
			for (String worldAlias : Mixin.getWorldAliases(worldId))
			{
				if (arg.equalsIgnoreCase(worldAlias)) return worldId;
			}
		}
		
		return null;
	}

	@Override
	public List<String> altNames(CommandSender sender)
	{
		List<String> ret = new ArrayList<String>();
		for (String worldId : Mixin.getWorldIds())
		{
			if ( ! Mixin.canSeeWorld(sender, worldId)) continue;
			ret.add(Mixin.getWorldDisplayName(worldId));
		}
		return ret;
	}
	
}
