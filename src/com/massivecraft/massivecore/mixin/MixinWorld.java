package com.massivecraft.massivecore.mixin;

import com.massivecraft.massivecore.ps.PS;
import com.massivecraft.massivecore.ps.PSFormatDesc;
import com.massivecraft.massivecore.util.MUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permissible;

import java.util.ArrayList;
import java.util.List;

public class MixinWorld extends Mixin
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static MixinWorld d = new MixinWorld();
	private static MixinWorld i = d;
	public static MixinWorld get() { return i; }
	
	// -------------------------------------------- //
	// METHODS
	// -------------------------------------------- //
	
	public boolean canSeeWorld(Permissible permissible, String worldId)
	{
		return true;
	}
	
	public List<String> getWorldIds()
	{
		// Create
		List<String> ret = new ArrayList<>();
		
		// Fill
		for (World world : Bukkit.getWorlds())
		{
			ret.add(world.getName());
		}
		
		// Return
		return ret;
	}
	
	public List<String> getVisibleWorldIds(Permissible permissible)
	{
		// Create
		List<String> ret = new ArrayList<>();
		
		// Fill
		for (String worldId : this.getWorldIds())
		{
			if ( ! this.canSeeWorld(permissible, worldId)) continue;
			ret.add(worldId);
		}
		
		// Return
		return ret;
	}
	
	public ChatColor getWorldColor(String worldId)
	{
		return ChatColor.WHITE;
	}
	
	public List<String> getWorldAliases(String worldId)
	{
		return new ArrayList<>();
	}
	
	public String getWorldAliasOrId(String worldId)
	{
		List<String> aliases = this.getWorldAliases(worldId);
		if (aliases.size() > 0) return aliases.get(0);
		return worldId;
	}
	
	public String getWorldDisplayName(String worldId)
	{
		return this.getWorldColor(worldId).toString() + this.getWorldAliasOrId(worldId);
	}
	
	public PS getWorldSpawnPs(String worldId)
	{
		World world = Bukkit.getWorld(worldId);
		if (world == null) return null;
		return PS.valueOf(world.getSpawnLocation());
	}
	
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
	
	public boolean trySetWorldSpawnWp(CommandSender sender, String worldId, PS goal, boolean verbooseChange, boolean verbooseSame)
	{
		World world = Bukkit.getWorld(worldId);
		if (world == null)
		{
			if (verbooseChange || verbooseSame)
			{
				MixinMessage.get().msgOne(sender, "<b>Unknown world <h>%s<b>.", worldId);
			}
			return false;
		}
		
		// Pre Calculations
		String worldDisplayName = MixinWorld.get().getWorldDisplayName(worldId);
		PS current = this.getWorldSpawnPs(worldId);
		String currentFormatted = current.toString(PSFormatDesc.get());
		String goalFormatted = goal.toString(PSFormatDesc.get());
		
		// No change?
		if (MUtil.equals(goal, current))
		{
			if (verbooseSame)
			{
				MixinMessage.get().msgOne(sender, "<i>Spawn location is already <h>%s <i>for <h>%s<i>.", currentFormatted, worldDisplayName);
			}
			return true;
		}
		
		// Report
		if (verbooseChange)
		{
			MixinMessage.get().msgOne(sender, "<i>Changing spawn location from <h>%s <i>to <h>%s <i>for <h>%s<i>.", currentFormatted, goalFormatted, worldDisplayName);
		}
		
		// Set it
		this.setWorldSpawnPs(worldId, goal);
				
		// Return
		return true;
	}

}
