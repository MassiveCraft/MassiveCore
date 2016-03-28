package com.massivecraft.massivecore.mixin;

import java.io.Serializable;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;

public class MixinEvent extends MixinAbstract
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static MixinEvent d = new MixinEvent();
	private static MixinEvent i = d;
	public static MixinEvent get() { return i; }
	public static void set(MixinEvent i) { MixinEvent.i = i; }
	
	// -------------------------------------------- //
	// METHODS
	// -------------------------------------------- //
	
	public <E extends Event & Serializable> void distribute(E event)
	{
		this.distribute(event, false);
	}
	
	public <E extends Event & Serializable> void distribute(E event, boolean includeSelf)
	{
		this.distributeOthers(event);
		if (includeSelf) this.distributeSelf(event);
	}
	
	protected <E extends Event & Serializable> void distributeSelf(E event)
	{
		Bukkit.getPluginManager().callEvent(event);
	}
	
	protected <E extends Event & Serializable> void distributeOthers(E event)
	{
		// NOTE: This is where the event would be sent to all other servers in the network.
	}

}
