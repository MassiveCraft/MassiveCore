package com.massivecraft.massivecore.event;

import java.util.Set;

import org.bukkit.entity.Damageable;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

import com.massivecraft.massivecore.collections.MassiveSet;
import com.massivecraft.massivecore.util.MUtil;

/**
 * The purpose of this event is to allow multiple plugins to have a say about these player properties.
 * 
 * It would have been great if the properties were event driven themselves.
 * Meaning Bukkit could run an event to calculate the value every time it was asked for.
 * That is however not the case.
 * 
 * So instead I trigger this event every once.
 * This is done from EngineMassiveCorePlayerUpdate.
 * 
 * The plugins wanting to have a say about the values can just listen to this event at the priority they want and modify values they care for.
 */
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
	
	private float walkSpeed;
	public float getWalkSpeed() { return this.walkSpeed; }
	public void setWalkSpeed(float walkSpeed) { this.walkSpeed = walkSpeed; }
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	// The boolean current decides if we use the current values or the default ones.
	// With current true it will be an "update".
	// With current false it will be a "reset".
	
	public EventMassiveCorePlayerUpdate(Player player, boolean current)
	{
		this.player = player;
		
		this.maxHealth = getMaxHealth(player, current);
		this.flyAllowed = isFlyAllowed(player, current);
		this.flyActive = isFlyActive(player, current);
		this.flySpeed = getFlySpeed(player, current);
		this.walkSpeed = getWalkSpeed(player, current);
	}
	
	// -------------------------------------------- //
	// RUN
	// -------------------------------------------- //
	
	public static void run(Player player, boolean current)
	{
		if (MUtil.isntPlayer(player)) return;
		
		EventMassiveCorePlayerUpdate event = new EventMassiveCorePlayerUpdate(player, current);
		event.run();
		
		setMaxHealth(player, event.getMaxHealth());
		setFlyAllowed(player, event.isFlyAllowed());
		setFlyActive(player, event.isFlyActive());
		setFlySpeed(player, event.getFlySpeed());
		setWalkSpeed(player, event.getWalkSpeed());
	}
	
	public static void run(Player player)
	{
		run(player, true);
	}

	// -------------------------------------------- //
	// MAX HEALTH
	// -------------------------------------------- //

	public static boolean setMaxHealth(Player player, double maxHealth)
	{	
		// NoChange
		if (getMaxHealth(player) == maxHealth) return false;
		
		// Apply
		player.setMaxHealth(maxHealth);
		
		// Return
		return true;
	}
	
	public static double getMaxHealth(Player player, boolean current)
	{
		if ( ! current) return 20D; 
		
		Damageable d = (Damageable) player;
		return d.getMaxHealth();
	}
	
	public static double getMaxHealth(Player player)
	{
		return getMaxHealth(player, true);
	}
	
	public static boolean resetMaxHealth(Player player)
	{
		return setMaxHealth(player, getMaxHealth(player, false));
	}
	
	// -------------------------------------------- //
	// FLY ALLOWED
	// -------------------------------------------- //
	
	// For backwards version compatibility we use the enumeration names rather than the enumerations themselves.
	public static Set<String> FLY_DEFAULT_GAME_MODE_NAMES = new MassiveSet<String>(
		"CREATIVE",
		"SPECTATOR"
	);
	
	public static boolean setFlyAllowed(Player player, boolean allowed)
	{	
		// NoChange
		if (isFlyAllowed(player) == allowed) return false;
		
		// Apply
		player.setFallDistance(0);
		player.setAllowFlight(allowed);
		player.setFallDistance(0);
		
		// Return
		return true;
	}
	
	public static boolean isFlyAllowed(Player player, boolean current)
	{
		if ( ! current) return FLY_DEFAULT_GAME_MODE_NAMES.contains(player.getGameMode().name());
		
		return player.getAllowFlight();
	}
	
	public static boolean isFlyAllowed(Player player)
	{
		return isFlyAllowed(player, true);
	}
	
	public static boolean resetFlyAllowed(Player player)
	{
		return setFlyAllowed(player, isFlyAllowed(player, false));
	}
	
	// -------------------------------------------- //
	// FLY ACTIVE
	// -------------------------------------------- //
	
	public static boolean setFlyActive(Player player, boolean active)
	{
		// NoChange
		if (isFlyActive(player) == active) return false;
		
		// Apply
		player.setFallDistance(0);
		player.setFlying(active);
		player.setFallDistance(0);
		
		// Return
		return true;
	}
	
	public static boolean isFlyActive(Player player, boolean current)
	{
		if ( ! current) return FLY_DEFAULT_GAME_MODE_NAMES.contains(player.getGameMode().name());
		
		return player.isFlying();
	}
	
	public static boolean isFlyActive(Player player)
	{
		return isFlyActive(player, true);
	}
	
	public static boolean resetFlyActive(Player player)
	{
		return setFlyActive(player, isFlyActive(player, false));
	}
	
	// -------------------------------------------- //
	// FLY SPEED
	// -------------------------------------------- //
	
	public final static float DEFAULT_FLY_SPEED = 0.1f;
	
	public static boolean setFlySpeed(Player player, float speed)
	{
		// NoChange
		if (getFlySpeed(player) == speed) return false;
		
		// Apply
		player.setFallDistance(0);
		player.setFlySpeed(speed);
		player.setFallDistance(0);
		
		// Return
		return true;
	}
	
	public static float getFlySpeed(Player player, boolean current)
	{
		if ( ! current) return DEFAULT_FLY_SPEED;
		
		return player.getFlySpeed();
	}
	
	public static float getFlySpeed(Player player)
	{
		return getFlySpeed(player, true);
	}
	
	public static boolean resetFlySpeed(Player player)
	{
		return setFlySpeed(player, getFlySpeed(player, false));
	}
	
	// -------------------------------------------- //
	// WALK SPEED
	// -------------------------------------------- //
	
	public final static float DEFAULT_WALK_SPEED = 0.2f;
	
	public static boolean setWalkSpeed(Player player, float speed)
	{
		// NoChange
		if (getWalkSpeed(player) == speed) return false;
		
		// Apply
		player.setWalkSpeed(speed);
		
		// Return
		return true;
	}
	
	public static float getWalkSpeed(Player player, boolean current)
	{
		if ( ! current) return DEFAULT_WALK_SPEED;
		
		return player.getWalkSpeed();
	}
	
	public static float getWalkSpeed(Player player)
	{
		return getWalkSpeed(player, true);
	}
	
	public static boolean resetWalkSpeed(Player player)
	{
		return setWalkSpeed(player, getWalkSpeed(player, false));
	}
	
}

