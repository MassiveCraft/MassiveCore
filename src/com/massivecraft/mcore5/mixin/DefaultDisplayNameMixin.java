package com.massivecraft.mcore5.mixin;

import java.util.Map;
import java.util.TreeMap;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.massivecraft.mcore5.util.SenderUtil;

public class DefaultDisplayNameMixin implements DisplayNameMixin
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static DefaultDisplayNameMixin i = new DefaultDisplayNameMixin();
	public static DefaultDisplayNameMixin get() { return i; }
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	protected Map<String, String> idToDisplayName = new TreeMap<String, String>(String.CASE_INSENSITIVE_ORDER);
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public String get(String senderId)
	{
		if (senderId == null) return null;
		
		String ret = this.idToDisplayName.get(senderId);
		if (ret != null) return ret;
		
		Player player = Bukkit.getPlayerExact(senderId);
		if (player != null) return player.getDisplayName();
		
		return Mixin.getSenderIdFixerMixin().tryFix(senderId);
	}

	@Override
	public void set(String senderId, String displayName)
	{
		this.idToDisplayName.put(senderId, displayName);
		
		Player player = Bukkit.getPlayerExact(senderId);
		if (player == null) return;
		player.setDisplayName(displayName);
	}

	@Override
	public String get(CommandSender sender)
	{
		return this.get(SenderUtil.getSenderId(sender));
	}

	@Override
	public void set(CommandSender sender, String displayName)
	{
		this.set(SenderUtil.getSenderId(sender), displayName);
	}

}