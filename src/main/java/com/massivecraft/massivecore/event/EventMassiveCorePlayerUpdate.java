package com.massivecraft.massivecore.event;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

import com.massivecraft.massivecore.util.PlayerUtil;

public class EventMassiveCorePlayerUpdate extends EventMassiveCore
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
	
	private double maxHealth;
	public double getMaxHealth() { return this.maxHealth; }
	public void setMaxHealth(double maxHealth) { this.maxHealth = maxHealth; }
	
	private boolean flyAllowed;
	public boolean isFlyAllowed() { return this.flyAllowed; }
	public void setAllowed(boolean flyAllowed) { this.flyAllowed = flyAllowed; }
	
	private boolean flyActive;
	public boolean isFlyActive() { return this.flyActive; }
	public void setFlyActive(boolean flyActive) { this.flyActive = flyActive; }
	
	private float flySpeed;
	public float getFlySpeed() { return this.flySpeed; }
	public void setFlySpeed(float flySpeed) { this.flySpeed = flySpeed; }
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public EventMassiveCorePlayerUpdate(Player player)
	{
		this.player = player;
		this.maxHealth = PlayerUtil.getMaxHealth(player);
		this.flyAllowed = PlayerUtil.isFlyAllowed(player);
		this.flyActive = PlayerUtil.isFlyActive(player);
		this.flySpeed = PlayerUtil.getFlySpeed(player);
	}
	
}

