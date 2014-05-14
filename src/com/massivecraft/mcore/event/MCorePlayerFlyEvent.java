package com.massivecraft.mcore.event;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

import com.massivecraft.mcore.util.FlyUtil;

public class MCorePlayerFlyEvent extends MCoreEvent
{
	// -------------------------------------------- //
	// CONSTANTS
	// -------------------------------------------- //
	
	public final static float DEFAULT_SPEED = 0.1f;
	
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
	
	private boolean allowed;
	public boolean isAllowed() { return this.allowed; }
	public void setAllowed(boolean allowed) { this.allowed = allowed; }
	
	private boolean active;
	public boolean isActive() { return this.active; }
	public void setActive(boolean active) { this.active = active; }
	
	private float speed;
	public float getSpeed() { return this.speed; }
	public void setSpeed(float speed) { this.speed = speed; }
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public MCorePlayerFlyEvent(Player player, boolean allowed, boolean active, float speed)
	{
		this.player = player;
		this.allowed = allowed;
		this.active = active;
		this.speed = speed;
	}
	
	public MCorePlayerFlyEvent(Player player)
	{
		this.player = player;
		this.allowed = FlyUtil.isAllowed(player);
		this.active = FlyUtil.isActive(player);
		this.speed = FlyUtil.getSpeed(player);
	}
	
}

