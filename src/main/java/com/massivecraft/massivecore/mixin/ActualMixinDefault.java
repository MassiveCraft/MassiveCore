package com.massivecraft.massivecore.mixin;

import org.bukkit.event.player.PlayerJoinEvent;

import com.massivecraft.massivecore.event.EventMassiveCorePlayerLeave;

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
	public boolean isActualLeave(EventMassiveCorePlayerLeave event)
	{
		return true;
	}
	
}