package com.massivecraft.mcore1.persist;

import java.util.Collection;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.massivecraft.mcore1.util.Txt;


public abstract class PlayerEntity<T extends PlayerEntity<T>> extends Entity<T>
{
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
	
	// -------------------------------------------- //
	// Message Sending Helpers
	// -------------------------------------------- //
	
	public void sendMessage(String msg)
	{
		Player player = this.getPlayer();
		if (player == null) return;
		player.sendMessage(msg);
	}
	
	public void sendMessage(Collection<String> msgs)
	{
		Player player = this.getPlayer();
		if (player == null) return;
		for(String msg : msgs)
		{
			player.sendMessage(msg);
		}
	}
	
	public void msg(String msg)
	{
		this.sendMessage(Txt.parse(msg));
	}
	
	public void msg(String msg, Object... args)
	{
		this.sendMessage(Txt.parse(msg, args));
	}
	
	public void msg(Collection<String> msgs)
	{
		Player player = this.getPlayer();
		if (player == null) return;
		for(String msg : msgs)
		{
			player.sendMessage(Txt.parse(msg));
		}
	}
}
