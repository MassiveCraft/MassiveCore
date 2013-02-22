package com.massivecraft.mcore.mixin;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import com.massivecraft.mcore.util.MUtil;
import com.massivecraft.mcore.util.SenderUtil;

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
		CommandSender sender = SenderUtil.getSender(senderId);
		return sender != null;
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
		
		String playerNameCC = Mixin.reqFix(senderId);
		if (playerNameCC == null) return null;
		
		File playerFile = new File(MUtil.getPlayerDirectory(), playerNameCC+".dat");
		long lastModified = playerFile.lastModified();
		if (lastModified == 0) return null;
		return lastModified;
	}
	
}