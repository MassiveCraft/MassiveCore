package com.massivecraft.massivecore.mixin;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.plugin.Plugin;

import com.massivecraft.massivecore.EngineAbstract;
import com.massivecraft.massivecore.MassiveCore;

public class EngineTeleportMixinCause extends EngineAbstract
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static EngineTeleportMixinCause i = new EngineTeleportMixinCause();
	public static EngineTeleportMixinCause get() { return i; }
	public EngineTeleportMixinCause() {}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public Plugin getPlugin()
	{
		return MassiveCore.get();
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
		Bukkit.getScheduler().scheduleSyncDelayedTask(MassiveCore.get(), new Runnable()
		{
			@Override
			public void run()
			{
				mixinCausedTeleportEvents.remove(event);
			}
		});
	}
	
}
