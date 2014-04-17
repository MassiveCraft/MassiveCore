package com.massivecraft.mcore.mixin;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

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
	public boolean isOnline(String senderId)
	{
		if (senderId == null) return false;
		return Mixin.getOnlineSenderIds().contains(senderId);
	}
	
	@Override
	public Long getFirstPlayed(String senderId)
	{
		OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(senderId);
		Long ret = offlinePlayer.getFirstPlayed();
		if (ret == 0) ret = null;
		return ret;
	}
	
	@Override
	public Long getLastPlayed(String senderId)
	{
		if (this.isOnline(senderId)) return System.currentTimeMillis();
		
		OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(senderId);
		Long ret = offlinePlayer.getLastPlayed();
		if (ret == 0) ret = null;
		return ret;
	}
	
}