package com.massivecraft.massivecore.teleport;

import com.massivecraft.massivecore.mixin.MixinWorld;
import com.massivecraft.massivecore.ps.PS;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DestinationWorld extends DestinationAbstract
{
	private static final long serialVersionUID = 1L;
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	protected String worldId;
	
	public String getWorldId() { return this.worldId; }
	public void setWorldId(String worldId) { this.worldId = worldId; }
	
	public void setWorld(World world) { this.worldId = (world == null ? null : world.getName()); }
	public World getWorld() { return Bukkit.getWorld(this.worldId); }
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public DestinationWorld()
	{
		
	}
	
	public DestinationWorld(CommandSender sender)
	{
		if ( ! (sender instanceof Player)) return;
		Player player = (Player)sender;
		this.setWorld(player.getWorld());
	}
	
	public DestinationWorld(World world)
	{
		this.setWorld(world);
	}
	
	public DestinationWorld(String worldId)
	{
		this.setWorldId(worldId);
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public PS getPsInner()
	{
		String worldId = this.getWorldId();
		if (worldId == null) return null;
		
		return MixinWorld.get().getWorldSpawnPs(worldId);
	}
	
	@Override
	public String getDesc(Object watcherObject)
	{
		return "World " + MixinWorld.get().getWorldDisplayName(this.getWorldId());
	}

}
