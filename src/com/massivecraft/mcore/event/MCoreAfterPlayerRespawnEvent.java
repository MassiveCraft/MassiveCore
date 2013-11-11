package com.massivecraft.mcore.event;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerRespawnEvent;

public class MCoreAfterPlayerRespawnEvent extends Event implements Runnable
{
	// -------------------------------------------- //
	// REQUIRED EVENT CODE
	// -------------------------------------------- //
	
	private static final HandlerList handlers = new HandlerList();
	@Override public HandlerList getHandlers() { return handlers; }
	public static HandlerList getHandlerList() { return handlers; }
	
	// -------------------------------------------- //
	// FIELD
	// -------------------------------------------- //
	
	protected final Location deathLocation;
	public Location getDeathLocation() { return this.deathLocation; }
	
	protected final PlayerRespawnEvent bukkitEvent;
	public PlayerRespawnEvent getBukkitEvent() { return this.bukkitEvent; }
	
	public Location getRespawnLocation() { return this.bukkitEvent.getRespawnLocation(); }
	public Player getPlayer() { return this.bukkitEvent.getPlayer(); }
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public MCoreAfterPlayerRespawnEvent(PlayerRespawnEvent bukkitEvent, Location deathLocation)
	{
		this.bukkitEvent = bukkitEvent;
		this.deathLocation = deathLocation;
	}
	
	// -------------------------------------------- //
	// HANDY RUN SHORTCUT
	// -------------------------------------------- //
	
	@Override
	public void run()
	{
		Bukkit.getPluginManager().callEvent(this);
	}
	
}
