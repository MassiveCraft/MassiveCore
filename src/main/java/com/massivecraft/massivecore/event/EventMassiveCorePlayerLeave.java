package com.massivecraft.massivecore.event;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * The EventMassiveCorePlayerLeave is a non-cancellable event.
 * It is run at the MONITOR of either PlayerKickEvent or PlayerQuitEvent.
 * It is also guaranteed to run before the MassiveCore "store" module syncs
 * all entities related to the player that is leaving the server.
 * 
 * Use this even if you want to update a player entity as
 * that player leaves. Automatic syncing will be guaranteed and the
 * event will run pre disconnect if possible due to the internal usage if the PlayerKickedEvent.
 */
public class EventMassiveCorePlayerLeave extends Event implements Runnable
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
	
	private final Player player;
	public Player getPlayer() { return this.player; }
	
	private final boolean preDisconnect;
	public boolean isPreDisconnect() { return this.preDisconnect; }
	public boolean isPostDisconnect() { return !this.isPreDisconnect(); }
	
	private final String caller;
	public String getCaller() { return this.caller; }
	public boolean isQuit() { return "quit".equals(caller); }
	public boolean isKick() { return "kick".equals(caller); }
	
	private final String message;
	public String getMessage() { return this.message; }
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public EventMassiveCorePlayerLeave(Player player, boolean preDisconnect, String caller, String message)
	{
		this.player = player;
		this.preDisconnect = preDisconnect;
		this.caller = caller;
		this.message = message;
	}
	
	// -------------------------------------------- //
	// HANDY RUN SHORTCUT
	// -------------------------------------------- //
	
	@Override
	public void run()
	{
		// Someone may already have issued a player leave event for this disconnect.
		// We ignore that since we want one leave event called per disconnect only.
		boolean doit = !player2event.containsKey(this.player.getName());
		
		//MassiveCore.p.log("EventMassiveCorePlayerLeave", "caller:", caller, "doit:", doit, "player:", player.getDisplayName(), "preDisconnect:", preDisconnect, "message:", message);
		
		if (doit)
		{
			//MassiveCore.p.log("EventMassiveCorePlayerLeave", caller, player.getDisplayName(), preDisconnect, message);
			player2event.put(this.player.getName(), this);
			Bukkit.getPluginManager().callEvent(this);
		}
	}
	
	// -------------------------------------------- //
	// STORING THE ACTIVE PLAYER EVENT
	// -------------------------------------------- //
	public static Map<String,EventMassiveCorePlayerLeave> player2event = new HashMap<String,EventMassiveCorePlayerLeave>();
	
}
