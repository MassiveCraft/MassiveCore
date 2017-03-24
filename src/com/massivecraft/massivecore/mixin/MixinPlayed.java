package com.massivecraft.massivecore.mixin;

import com.massivecraft.massivecore.util.IdUtil;
import com.massivecraft.massivecore.util.MUtil;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import java.util.UUID;

public class MixinPlayed extends Mixin
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static MixinPlayed d = new MixinPlayed();
	private static MixinPlayed i = d;
	public static MixinPlayed get() { return i; }
	
	// -------------------------------------------- //
	// METHODS
	// -------------------------------------------- //
	
	public boolean isOnline(Object senderObject)
	{
		return IdUtil.isOnline(senderObject);
	}
	
	public boolean isOffline(Object senderObject)
	{
		return ! this.isOnline(senderObject);
	}
	
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
	
	public boolean hasPlayedBefore(Object senderObject)
	{
		Long firstPlayed = this.getFirstPlayed(senderObject);
		return firstPlayed != null && firstPlayed != 0;
	}
	
	public String getIp(Object senderObject)
	{
		CommandSender sender = IdUtil.getSender(senderObject);
		if (MUtil.isntPlayer(senderObject)) return null;
		return MUtil.getIp(sender);
	}

}
