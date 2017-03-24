package com.massivecraft.massivecore.mixin;

import com.massivecraft.massivecore.util.IdUtil;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

public class MixinGamemode extends Mixin
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static MixinGamemode d = new MixinGamemode();
	private static MixinGamemode i = d;
	public static MixinGamemode get() { return i; }
	
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
