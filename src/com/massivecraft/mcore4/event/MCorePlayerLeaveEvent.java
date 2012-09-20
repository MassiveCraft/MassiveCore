package com.massivecraft.mcore4.event;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * The MCorePlayerLeaveEvent is a non-cancellable event.
 * It is run at the MONITOR of either PlayerKickEvent or PlayerQuitEvent.
 * It is also guaranteed to run before the MCore "store" module syncs
 * all entities related to the player that is leaving the server.
 * 
 * Use this even if you want to update a player entity as
 * that player leaves. Automatic syncing will be guaranteed and the
 * event will run the moment BEFORE the player leaves the server if possible
 * due to the internal usage if the PlayerKickedEvent.
 */
public class MCorePlayerLeaveEvent extends Event implements Runnable
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
	
	protected Player player;
	public Player getPlayer() { return this.player; }
	
	protected boolean kick;
	public boolean isKick() { return this.kick; }
	public boolean isQuit() { return !this.kick; }
	
	protected String kickReason;
	public String getKickReason() { return this.kickReason; }
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public MCorePlayerLeaveEvent(Player player, boolean kick, String kickReason)
	{
		this.player = player;
		this.kick = kick;
		this.kickReason = kickReason;
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
