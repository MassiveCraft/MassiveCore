package com.massivecraft.massivecore;

public enum SenderType
{
	PLAYER, // A player. Such as Notch or Dinnerbone. @console is not a player.
	NONPLAYER, // A sender which is not a player. Such as @console.
	ANY, // Anyone. Both players, and nonplayers.
	
	;
}
