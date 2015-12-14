package com.massivecraft.massivecore.mixin;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import com.massivecraft.massivecore.util.IdUtil;

public class GamemodeMixinDefault extends GamemodeMixinAbstract
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static GamemodeMixinDefault i = new GamemodeMixinDefault();
	public static GamemodeMixinDefault get() { return i; }

	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public GameMode getGamemode(Object playerObject)
	{
		Player player = IdUtil.getPlayer(playerObject);
		if (player == null) return null;
		
		return player.getGameMode();
	}

	@Override
	public void setGamemode(Object playerObject, GameMode gm)
	{
		Player player = IdUtil.getPlayer(playerObject);
		if (player == null) return;
		
		player.setGameMode(gm);
	}

}
