package com.massivecraft.mcore5.mixin;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.massivecraft.mcore5.util.SenderUtil;

public class KickMixinDefault extends KickMixinAbstract
{	
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static KickMixinDefault i = new KickMixinDefault();
	public static KickMixinDefault get() { return i; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //

	@Override
	public boolean kick(CommandSender sender, String message)
	{
		Player player = SenderUtil.getAsPlayer(sender);
		if (player == null) return false;
		player.kickPlayer(message);
		return true;
	}
	
	@Override
	public boolean kick(String senderId, String message)
	{
		Player player = SenderUtil.getPlayer(senderId);
		if (player == null) return false;
		player.kickPlayer(message);
		return true;
	}
	
}