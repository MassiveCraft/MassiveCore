package com.massivecraft.massivecore.event;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerRespawnEvent;

public class EventMassiveCoreAfterPlayerRespawn extends EventMassiveCore
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
	
	private final Location deathLocation;
	public Location getDeathLocation() { return this.deathLocation; }
	
	private final PlayerRespawnEvent bukkitEvent;
	public PlayerRespawnEvent getBukkitEvent() { return this.bukkitEvent; }
	
	public Location getRespawnLocation() { return this.bukkitEvent.getRespawnLocation(); }
	public Player getPlayer() { return this.bukkitEvent.getPlayer(); }
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public EventMassiveCoreAfterPlayerRespawn(PlayerRespawnEvent bukkitEvent, Location deathLocation)
	{
		this.bukkitEvent = bukkitEvent;
		this.deathLocation = deathLocation;
	}
	
}
