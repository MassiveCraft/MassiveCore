package com.massivecraft.mcore4.util.extractor;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.event.player.PlayerEvent;

public class ExtractorWorldName implements Extractor
{
	@Override
	public Object extract(Object o)
	{
		if (o instanceof String) return o;
		if (o instanceof World) return ((World)o).getName();
		if (o instanceof Block) return ((Block)o).getWorld().getName();
		if (o instanceof Location) return ((Location)o).getWorld().getName();
		if (o instanceof Entity) return ((Entity)o).getWorld().getName();
		if (o instanceof PlayerEvent) return ((PlayerEvent)o).getPlayer().getWorld().getName();
		return null;
	}
}
