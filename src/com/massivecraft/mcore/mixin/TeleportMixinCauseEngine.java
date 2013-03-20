package com.massivecraft.mcore.mixin;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

import com.massivecraft.mcore.MCore;

public class TeleportMixinCauseEngine implements Listener 
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static TeleportMixinCauseEngine i = new TeleportMixinCauseEngine();
	public static TeleportMixinCauseEngine get() { return i; }
	public TeleportMixinCauseEngine() {}
	
	// -------------------------------------------- //
	// SETUP
	// -------------------------------------------- //
	
	public void setup()
	{
		Bukkit.getPluginManager().registerEvents(this, MCore.get());
	}

	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	private boolean mixinCausedTeleportIncoming = false;
	public boolean isMixinCausedTeleportIncoming() { return this.mixinCausedTeleportIncoming; }
	public void setMixinCausedTeleportIncoming(boolean mixinCausedTeleportIncoming) { this.mixinCausedTeleportIncoming = mixinCausedTeleportIncoming; }
	
	private Set<PlayerTeleportEvent> mixinCausedTeleportEvents = Collections.newSetFromMap(new ConcurrentHashMap<PlayerTeleportEvent, Boolean>());
	
	// -------------------------------------------- //
	// TO BE USED
	// -------------------------------------------- //
	
	public boolean isCausedByTeleportMixin(PlayerTeleportEvent event)
	{
		return this.mixinCausedTeleportEvents.contains(event);
	}
	
	// -------------------------------------------- //
	// LISTENER
	// -------------------------------------------- //
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void markEvent(final PlayerTeleportEvent event)
	{
		if (!mixinCausedTeleportIncoming) return;
		mixinCausedTeleportIncoming = false;
		mixinCausedTeleportEvents.add(event);
		Bukkit.getScheduler().scheduleSyncDelayedTask(MCore.get(), new Runnable()
		{
			@Override
			public void run()
			{
				mixinCausedTeleportEvents.remove(event);
			}
		});
	}
	
}