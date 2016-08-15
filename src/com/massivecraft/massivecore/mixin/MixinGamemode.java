package com.massivecraft.massivecore.mixin;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import com.massivecraft.massivecore.util.IdUtil;

public class MixinGamemode extends MixinAbstract
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static MixinGamemode d = new MixinGamemode();
	private static MixinGamemode i = d;
	public static MixinGamemode get() { return i; }
	public static void set(MixinGamemode i) { MixinGamemode.i = i; }
	
	// -------------------------------------------- //
	// METHODS
	// -------------------------------------------- //
	
	public GameMode getGamemode(Object playerObject)
	{
		Player player = IdUtil.getPlayer(playerObject);
		if (player == null) return null;
		
		return player.getGameMode();
	}

	public void setGamemode(Object playerObject, GameMode gameMode)
	{
		Player player = IdUtil.getPlayer(playerObject);
		if (player == null) return;
		
		player.setGameMode(gameMode);
	}

}
