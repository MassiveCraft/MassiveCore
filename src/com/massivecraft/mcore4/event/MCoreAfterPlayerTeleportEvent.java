package com.massivecraft.mcore4.event;

import lombok.Getter;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

public class MCoreAfterPlayerTeleportEvent extends Event implements Runnable
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
	
	@Getter protected final PlayerTeleportEvent bukkitEvent;
	
	public Location getFrom() { return this.bukkitEvent.getFrom(); }
	public Location getTo() { return this.bukkitEvent.getTo(); }
	public Player getPlayer() { return this.bukkitEvent.getPlayer(); }
	public TeleportCause getCause() { return this.bukkitEvent.getCause(); }
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public MCoreAfterPlayerTeleportEvent(PlayerTeleportEvent bukkitEvent)
	{
		this.bukkitEvent = bukkitEvent;
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
