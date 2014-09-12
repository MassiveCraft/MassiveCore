package com.massivecraft.massivecore.mixin;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permissible;

import com.massivecraft.massivecore.ps.PS;
import com.massivecraft.massivecore.ps.PSFormatDesc;
import com.massivecraft.massivecore.util.MUtil;

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
	public boolean canSeeWorld(Permissible permissible, String worldId)
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
		return PS.valueOf(world.getSpawnLocation());
	}

	@Override
	public void setWorldSpawnPs(String worldId, PS spawnPs)
	{
		World world = Bukkit.getWorld(worldId);
		if (world == null) return;
		
		spawnPs = spawnPs.withWorld(world.getName());
		
		Location location = null;
		try
		{
			location = spawnPs.asBukkitLocation(true);
		}
		catch (Exception e)
		{
			return;
		}
		
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
				Mixin.msgOne(sender, "<b>Unknown world <h>%s<b>.", worldId);
			}
			return false;
		}
		
		// Pre Calculations
		String worldDisplayName = Mixin.getWorldDisplayName(worldId);
		PS current = this.getWorldSpawnPs(worldId);
		String currentFormatted = current.toString(PSFormatDesc.get());
		String goalFormatted = goal.toString(PSFormatDesc.get());
		
		// No change?
		if (MUtil.equals(goal, current))
		{
			if (verbooseSame)
			{
				Mixin.msgOne(sender, "<i>Spawn location is already <h>%s <i>for <h>%s<i>.", currentFormatted, worldDisplayName);
			}
			return true;
		}
		
		// Report
		if (verbooseChange)
		{
			Mixin.msgOne(sender, "<i>Changing spawn location from <h>%s <i>to <h>%s <i>for <h>%s<i>.", currentFormatted, goalFormatted, worldDisplayName);
		}
		
		// Set it
		this.setWorldSpawnPs(worldId, goal);
				
		// Return
		return true;
	}

}