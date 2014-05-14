package com.massivecraft.mcore.mixin;

import org.bukkit.entity.Player;

import com.massivecraft.mcore.ps.PS;
import com.massivecraft.mcore.util.IdUtil;

public class SenderPsMixinDefault extends SenderPsMixinAbstract
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static SenderPsMixinDefault i = new SenderPsMixinDefault();
	public static SenderPsMixinDefault get() { return i; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public PS getSenderPs(Object senderObject)
	{
		Player player = IdUtil.getPlayer(senderObject);
		if (player == null) return null;
		return PS.valueOf(player.getLocation());
	}

	@Override
	public void setSenderPs(Object senderObject, PS ps)
	{
		// Bukkit does not support setting the physical state for offline players for now.
	}
}
