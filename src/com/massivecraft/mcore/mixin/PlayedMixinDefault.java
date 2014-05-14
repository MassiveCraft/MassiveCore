package com.massivecraft.mcore.mixin;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import com.massivecraft.mcore.util.IdUtil;

public class PlayedMixinDefault extends PlayedMixinAbstract
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static PlayedMixinDefault i = new PlayedMixinDefault();
	public static PlayedMixinDefault get() { return i; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public Long getFirstPlayed(Object senderObject)
	{
		UUID uuid = IdUtil.getUuid(senderObject);
		if (uuid == null) return null;
		OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uuid);
		if (offlinePlayer == null) return null;
		
		Long ret = offlinePlayer.getFirstPlayed();
		if (ret == 0) ret = null;
		
		return ret;
	}
	
	@Override
	public Long getLastPlayed(Object senderObject)
	{
		//if (this.isOnline(senderObject)) return System.currentTimeMillis();
		// We do in fact NOT want this commented out behavior
		// It's important we can check the previous played time on join!
		
		UUID uuid = IdUtil.getUuid(senderObject);
		if (uuid == null) return null;
		OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uuid);
		if (offlinePlayer == null) return null;
		
		Long ret = offlinePlayer.getLastPlayed();
		if (ret == 0) ret = null;
		
		return ret;
	}
	
}