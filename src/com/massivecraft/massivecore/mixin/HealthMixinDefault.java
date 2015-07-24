package com.massivecraft.massivecore.mixin;

import org.bukkit.entity.Player;

import com.massivecraft.massivecore.util.IdUtil;

public class HealthMixinDefault extends HealthMixinAbstract
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static HealthMixinDefault i = new HealthMixinDefault();
	public static HealthMixinDefault get() { return i; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public Double getHealth(Object senderObject)
	{	
		Player player = IdUtil.getPlayer(senderObject);
		if (player == null) return null;
		
		return player.getHealth();
	}

	@Override
	public boolean setHealth(Object senderObject, double health)
	{
		Player player = IdUtil.getPlayer(senderObject);
		if (player == null) return false; // Failure
		
		// Ensure health is not lower than 0. (We also ensure to avoid bukkit throwing exceptions).
		if (health < 0)
		{
			health = 0;
		}
		
		// Ensure is not higher than max. (We also ensure to avoid bukkit throwing exceptions).
		Double max = this.getMaxHealth(senderObject);
		if (max != null && health > max)
		{
			health = max;
		}
		
		player.setHealth(health);
		return true; // Success
	}

	@Override
	public Double getMaxHealth(Object senderObject)
	{
		Player player = IdUtil.getPlayer(senderObject);
		if (player == null) return null;
		
		return player.getMaxHealth();
	}

	@Override
	public boolean setMaxHealth(Object senderObject, double maxHealth)
	{
		Player player = IdUtil.getPlayer(senderObject);
		if (player == null) return false; // Failure
		
		// Lower than 0 makes no sense.
		// Should we return false or throw an exception?
		// If we throw an exception, returning false is only when a player is not found.
		if (maxHealth < 0) throw new IllegalArgumentException("max health must be above zero");
		
		player.setMaxHealth(maxHealth);
		return true; // Success
	}

}
