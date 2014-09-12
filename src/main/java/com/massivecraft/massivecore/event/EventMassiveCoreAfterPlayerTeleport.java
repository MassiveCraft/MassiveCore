package com.massivecraft.massivecore.event;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

public class EventMassiveCoreAfterPlayerTeleport extends EventMassiveCore
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
	
	private final PlayerTeleportEvent bukkitEvent;
	public PlayerTeleportEvent getBukkitEvent() { return this.bukkitEvent; }
	
	public Location getFrom() { return this.bukkitEvent.getFrom(); }
	public Location getTo() { return this.bukkitEvent.getTo(); }
	public Player getPlayer() { return this.bukkitEvent.getPlayer(); }
	public TeleportCause getCause() { return this.bukkitEvent.getCause(); }
	
	public boolean isCrossWorlds()
	{
		World worldFrom = this.getFrom().getWorld();
		World worldTo = this.getTo().getWorld();
		return ! worldFrom.equals(worldTo);
	}
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public EventMassiveCoreAfterPlayerTeleport(PlayerTeleportEvent bukkitEvent)
	{
		this.bukkitEvent = bukkitEvent;
	}
	
}
