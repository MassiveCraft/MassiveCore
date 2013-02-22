package com.massivecraft.mcore.mixin;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permissible;

import com.massivecraft.mcore.PS;
import com.massivecraft.mcore.util.MUtil;

public class WorldMixinDefault extends WorldMixinAbstract
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static WorldMixinDefault i = new WorldMixinDefault();
	public static WorldMixinDefault get() { return i; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public boolean canSee(Permissible permissible, String worldId)
	{
		return true;
	}
	
	@Override
	public List<String> getWorldIds()
	{
		List<String> ret = new ArrayList<String>();
		for (World world : Bukkit.getWorlds())
		{
			ret.add(world.getName());
		}
		return ret;
	}
	
	@Override
	public ChatColor getWorldColor(String worldId)
	{
		return ChatColor.WHITE;
	}
	
	@Override
	public List<String> getWorldAliases(String worldId)
	{
		return new ArrayList<String>();
	}

	@Override
	public PS getWorldSpawnPs(String worldId)
	{
		World world = Bukkit.getWorld(worldId);
		if (world == null) return null;
		return new PS(world.getSpawnLocation());
	}

	@Override
	public void setWorldSpawnPs(String worldId, PS spawnPs)
	{
		World world = Bukkit.getWorld(worldId);
		if (world == null) return;
		
		spawnPs = spawnPs.clone();
		spawnPs.setWorldName(worldId);
		Location location = spawnPs.calcLocation();
		if (location == null) return;
		
		world.setSpawnLocation(location.getBlockX(), location.getBlockY(), location.getBlockZ());
	}
	
	@Override
	public boolean trySetWorldSpawnWp(CommandSender sender, String worldId, PS goal, boolean verbooseChange, boolean verbooseSame)
	{
		World world = Bukkit.getWorld(worldId);
		if (world == null)
		{
			if (verbooseChange || verbooseSame)
			{
				Mixin.msg(sender, "<b>Unknown world <h>%s<b>.", worldId);
			}
			return false;
		}
		
		// Pre Calculations
		String worldDisplayName = Mixin.getWorldDisplayName(worldId);
		PS current = this.getWorldSpawnPs(worldId);
		String currentFormatted = current.getShortDesc();
		String goalFormatted = goal.getShortDesc();
		
		// No change?
		if (MUtil.equals(goal, current))
		{
			if (verbooseSame)
			{
				Mixin.msg(sender, "<i>Spawn location is already <h>%s <i>for <h>%s<i>.", currentFormatted, worldDisplayName);
			}
			return true;
		}
		
		// Report
		if (verbooseChange)
		{
			Mixin.msg(sender, "<i>Changing spawn location from <h>%s <i>to <h>%s <i>for <h>%s<i>.", currentFormatted, goalFormatted, worldDisplayName);
		}
		
		// Set it
		this.setWorldSpawnPs(worldId, goal);
				
		// Return
		return true;
	}

}