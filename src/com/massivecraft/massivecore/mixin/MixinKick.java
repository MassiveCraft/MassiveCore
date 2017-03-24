package com.massivecraft.massivecore.mixin;

import com.massivecraft.massivecore.util.IdUtil;
import org.bukkit.entity.Player;

public class MixinKick extends Mixin
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static MixinKick d = new MixinKick();
	private static MixinKick i = d;
	public static MixinKick get() { return i; }
	
	// -------------------------------------------- //
	// METHODS
	// -------------------------------------------- //
	
	public boolean kick(Object senderObject)
	{
		return this.kick(senderObject, null);
	}
	
	public boolean kick(Object senderObject, String message)
	{
		Player player = IdUtil.getPlayer(senderObject);
		if (player == null) return false;
		player.kickPlayer(message);
		return true;
	}

}
