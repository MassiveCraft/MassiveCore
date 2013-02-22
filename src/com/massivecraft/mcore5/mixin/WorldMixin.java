package com.massivecraft.mcore5.mixin;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permissible;

import com.massivecraft.mcore5.PS;

public interface WorldMixin
{
	public boolean canSee(Permissible permissible, String worldId);
	
	public List<String> getWorldIds();
	
	public List<String> getVisibleWorldIds(Permissible permissible);
	
	public ChatColor getWorldColor(String worldId);
	
	public List<String> getWorldAliases(String worldId);
	
	public String getWorldAliasOrId(String worldId);
	
	public String getWorldDisplayName(String worldId);
	
	public PS getWorldSpawnPs(String worldId);
	
	public void setWorldSpawnPs(String worldId, PS spawnPs);
	
	public boolean trySetWorldSpawnWp(CommandSender sender, String worldId, PS spawnPs, boolean verbooseChange, boolean verbooseSame);
	
}
