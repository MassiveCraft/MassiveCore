package com.massivecraft.massivecore.engine;

import com.massivecraft.massivecore.Engine;
import com.massivecraft.massivecore.MassiveCore;
import com.massivecraft.massivecore.event.EventMassiveCorePlayerLeave;
import com.massivecraft.massivecore.util.MUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class EngineMassiveCorePlayerLeave extends Engine
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static EngineMassiveCorePlayerLeave i = new EngineMassiveCorePlayerLeave();
	public static EngineMassiveCorePlayerLeave get() { return i; }
	
	@Override
	public void setActiveInner(boolean active)
	{
		if ( ! active) return;
		
		EventMassiveCorePlayerLeave.player2event.clear();
	}
	
	// -------------------------------------------- //
	// KICK (RUN)
	// -------------------------------------------- //
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void runKick(PlayerKickEvent event)
	{
		final Player player = event.getPlayer();
		if (MUtil.isntPlayer(player)) return;
		
		new EventMassiveCorePlayerLeave(player, true, "kick", event.getReason()).run();
	}
	
	// -------------------------------------------- //
	// QUIT (RUN AND CLEAR)
	// -------------------------------------------- //
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void runQuit(PlayerQuitEvent event)
	{
		final Player player = event.getPlayer();
		if (MUtil.isntPlayer(player)) return;
		final UUID uuid = player.getUniqueId();
		
		new EventMassiveCorePlayerLeave(player, false, "quit", null).run();
		
		// We do the schedule in order for the set to be correct through out the whole MONITOR priority state.
		Bukkit.getScheduler().scheduleSyncDelayedTask(MassiveCore.get(), new Runnable()
		{
			@Override
			public void run()
			{
				EventMassiveCorePlayerLeave.player2event.remove(uuid);
			}
		});
	}
	
}
