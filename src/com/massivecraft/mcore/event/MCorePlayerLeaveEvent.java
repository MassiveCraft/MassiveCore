package com.massivecraft.mcore.event;

import java.util.HashMap;
import java.util.Map;

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
 * event will run pre disconnect if possible due to the internal usage if the PlayerKickedEvent.
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
	
	protected final Player player;
	public Player getPlayer() { return this.player; }
	
	protected final boolean preDisconnect;
	public boolean isPreDisconnect() { return this.preDisconnect; }
	public boolean isPostDisconnect() { return !this.isPreDisconnect(); }
	
	protected final String caller;
	public String getCaller() { return this.caller; }
	public boolean isQuit() { return "quit".equals(caller); }
	public boolean isKick() { return "kick".equals(caller); }
	
	protected final String message;
	public String getMessage() { return this.message; }
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public MCorePlayerLeaveEvent(Player player, boolean preDisconnect, String caller, String message)
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
		
		//MCore.p.log("MCorePlayerLeaveEvent", "caller:", caller, "doit:", doit, "player:", player.getDisplayName(), "preDisconnect:", preDisconnect, "message:", message);
		
		if (doit)
		{
			//MCore.p.log("MCorePlayerLeaveEvent", caller, player.getDisplayName(), preDisconnect, message);
			player2event.put(this.player.getName(), this);
			Bukkit.getPluginManager().callEvent(this);
		}
	}
	
	// -------------------------------------------- //
	// STORING THE ACTIVE PLAYER EVENT
	// -------------------------------------------- //
	public static Map<String,MCorePlayerLeaveEvent> player2event = new HashMap<String,MCorePlayerLeaveEvent>();
	
}
