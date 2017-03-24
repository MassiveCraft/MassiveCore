package com.massivecraft.massivecore.mixin;

import com.massivecraft.massivecore.event.EventMassiveCorePlayerLeave;
import org.bukkit.event.player.PlayerJoinEvent;

public class MixinActual extends Mixin
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static MixinActual d = new MixinActual();
	private static MixinActual i = d;
	public static MixinActual get() { return i; }
	
	// -------------------------------------------- //
	// METHODS
	// -------------------------------------------- //
	
	public boolean isActualJoin(PlayerJoinEvent event)
	{
		return true;
	}
	
	public boolean isActualLeave(EventMassiveCorePlayerLeave event)
	{
		return true;
	}

}
