package com.massivecraft.massivecore.mixin;

import org.bukkit.entity.Player;

import com.massivecraft.massivecore.util.IdUtil;

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
	public boolean kick(Object senderObject, String message)
	{
		Player player = IdUtil.getPlayer(senderObject);
		if (player == null) return false;
		player.kickPlayer(message);
		return true;
	}
	
}