package com.massivecraft.mcore.mixin;

import org.bukkit.event.player.PlayerJoinEvent;

import com.massivecraft.mcore.event.MCorePlayerLeaveEvent;

public class ActualMixinDefault extends ActualMixinAbstract
{	
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static ActualMixinDefault i = new ActualMixinDefault();
	public static ActualMixinDefault get() { return i; }

	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public boolean isActualJoin(PlayerJoinEvent event)
	{
		return true;
	}
	
	@Override
	public boolean isActualLeave(MCorePlayerLeaveEvent event)
	{
		return true;
	}
	
}