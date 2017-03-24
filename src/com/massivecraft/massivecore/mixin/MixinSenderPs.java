package com.massivecraft.massivecore.mixin;

import com.massivecraft.massivecore.ps.PS;
import com.massivecraft.massivecore.util.IdUtil;
import org.bukkit.entity.Player;

public class MixinSenderPs extends Mixin
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static MixinSenderPs d = new MixinSenderPs();
	private static MixinSenderPs i = d;
	public static MixinSenderPs get() { return i; }
	
	// -------------------------------------------- //
	// METHODS
	// -------------------------------------------- //
	
	public PS getSenderPs(Object senderObject)
	{
		Player player = IdUtil.getPlayer(senderObject);
		if (player == null) return null;
		return PS.valueOf(player.getLocation());
	}

	public void setSenderPs(Object senderObject, PS ps)
	{
		// Bukkit does not support setting the physical state for offline players for now.
	}

}
