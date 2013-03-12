package com.massivecraft.mcore.mixin;

import org.bukkit.event.player.PlayerJoinEvent;

import com.massivecraft.mcore.event.MCorePlayerLeaveEvent;

public class ActuallMixinDefault extends ActuallMixinAbstract
{	
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static ActuallMixinDefault i = new ActuallMixinDefault();
	public static ActuallMixinDefault get() { return i; }

	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public boolean isActuallJoin(PlayerJoinEvent event)
	{
		return true;
	}
	
	@Override
	public boolean isActuallLeave(MCorePlayerLeaveEvent event)
	{
		return true;
	}
	
}