package com.massivecraft.massivecore.mixin;

import org.bukkit.GameMode;

public interface GamemodeMixin
{
	public GameMode getGamemode(Object playerObject);
	public void setGamemode(Object playerObject, GameMode gm);
}
