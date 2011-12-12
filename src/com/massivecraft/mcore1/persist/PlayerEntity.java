package com.massivecraft.mcore1.persist;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;


public abstract class PlayerEntity extends Entity<PlayerEntity>
{
	@Override
	protected PlayerEntity getThis()
	{
		return this;
	}
	
	public Player getPlayer()
	{
		return Bukkit.getPlayer(this.getId());
	}
	
	public boolean isOnline()
	{
		return this.getPlayer() != null;
	}
	
	public boolean isOffline()
	{
		return ! isOnline();
	}

}
