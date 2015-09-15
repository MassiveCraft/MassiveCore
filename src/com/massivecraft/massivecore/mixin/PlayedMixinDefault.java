package com.massivecraft.massivecore.mixin;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import com.massivecraft.massivecore.util.IdUtil;
import com.massivecraft.massivecore.util.MUtil;

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
		if (MUtil.isNpc(senderObject)) return null;
		
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
		
		if (MUtil.isNpc(senderObject)) return null;
		
		UUID uuid = IdUtil.getUuid(senderObject);
		if (uuid == null) return null;
		OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uuid);
		if (offlinePlayer == null) return null;
		
		Long ret = offlinePlayer.getLastPlayed();
		if (ret == 0) ret = null;
		
		return ret;
	}
	
	@Override
	public String getIp(Object senderObject)
	{
		CommandSender sender = IdUtil.getSender(senderObject);
		if (MUtil.isntPlayer(senderObject)) return null;
		return MUtil.getIp(sender);
	}
	
}
