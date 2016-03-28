package com.massivecraft.massivecore.mixin;

import org.bukkit.entity.Player;

import com.massivecraft.massivecore.util.IdUtil;

public class MixinKick extends MixinAbstract
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static MixinKick d = new MixinKick();
	private static MixinKick i = d;
	public static MixinKick get() { return i; }
	public static void set(MixinKick i) { MixinKick.i = i; }
	
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
