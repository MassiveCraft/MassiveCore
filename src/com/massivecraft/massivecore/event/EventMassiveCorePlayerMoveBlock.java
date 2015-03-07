package com.massivecraft.massivecore.event;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerMoveEvent;

import com.massivecraft.massivecore.ps.PS;

public class EventMassiveCorePlayerMoveBlock extends EventMassiveCore
{
	// -------------------------------------------- //
	// REQUIRED EVENT CODE
	// -------------------------------------------- //
	
	private static final HandlerList handlers = new HandlerList();
	@Override public HandlerList getHandlers() { return handlers; }
	public static HandlerList getHandlerList() { return handlers; }
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	// Bukkit event
	private final PlayerMoveEvent bukkitEvent;
	public PlayerMoveEvent getBukkitEvent() { return this.bukkitEvent; }
	
	private final PS from;
	public PS getFromPs() { return this.from; }
	
	private final PS to;
	public PS getToPs() { return this.to; }
	
	// This event is just for monitoring & will not be thrown if the bukkit event was cancelled.
	@Override public boolean isCancelled() { return false; }
	@Override public void setCancelled(boolean cancel) { throw new UnsupportedOperationException("This event is only for watching not modifying."); }
	
	// Fake fields
	public Player getPlayer() { return this.getBukkitEvent().getPlayer(); }
	
	// -------------------------------------------- //
	// CONSTRCUT
	// -------------------------------------------- //
	
	public EventMassiveCorePlayerMoveBlock(PlayerMoveEvent bukkitEvent)
	{
		this.bukkitEvent = bukkitEvent;
		from = PS.valueOf(bukkitEvent.getFrom());
		to = PS.valueOf(bukkitEvent.getTo());
	}
	
}
