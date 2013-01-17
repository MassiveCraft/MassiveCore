package com.massivecraft.mcore5;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import com.massivecraft.mcore5.event.MCoreAfterPlayerRespawnEvent;
import com.massivecraft.mcore5.event.MCoreAfterPlayerTeleportEvent;
import com.massivecraft.mcore5.event.MCorePlayerLeaveEvent;
import com.massivecraft.mcore5.store.Coll;
import com.massivecraft.mcore5.store.PlayerColl;
import com.massivecraft.mcore5.store.SenderColl;
import com.massivecraft.mcore5.util.SmokeUtil;

public class InternalListener implements Listener
{
	MCore p;
	
	public InternalListener(MCore p)
	{
		this.p = p;
		MCorePlayerLeaveEvent.player2event.clear();
		Bukkit.getServer().getPluginManager().registerEvents(this, this.p);
	}
	
	/*
	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerLogin(PlayerLoginEvent event)
	{
		String id = event.getPlayer().getName();
		
		for (Persist instance : Persist.instances)
		{
			for (IClassManager<?> manager : instance.getClassManagers().values())
			{
				if (manager.idCanFix(Player.class) == false) continue;
				if (manager.containsId(id)) continue;
				manager.create(id);
			}
		}
	}*/
	
	// -------------------------------------------- //
	// EXPLOSION FX
	// -------------------------------------------- //
	
	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void explosionFx(EntityDamageByBlockEvent event)
	{
		// If an entity is taking damage from a block explosion ...
		DamageCause cause = event.getCause();
		if (cause != DamageCause.BLOCK_EXPLOSION) return;
		
		// ... and that explosion is a fake ...
		if (SmokeUtil.fakeExplosion == false) return;
		
		// ... then cancel the event and the damage.
		event.setCancelled(true);
	}
	
	// -------------------------------------------- //
	// PLAYER AND SENDER REFERENCES
	// -------------------------------------------- //
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void playerReferencesLoginLowest(PlayerLoginEvent event)
	{
		String id = event.getPlayer().getName();
		Player player = event.getPlayer();
		
		PlayerColl.setPlayerRefferences(id, player);
		SenderColl.setSenderRefferences(id, player);
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void playerReferencesLoginMonitor(PlayerLoginEvent event)
	{
		if (event.getResult() == Result.ALLOWED) return;
		
		String id = event.getPlayer().getName();
		Player player = null;
		
		PlayerColl.setPlayerRefferences(id, player);
		SenderColl.setSenderRefferences(id, player);
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void playerReferencesQuitMonitor(PlayerQuitEvent event)
	{
		String id = event.getPlayer().getName();
		Player player = null;
		
		PlayerColl.setPlayerRefferences(id, player);
		SenderColl.setSenderRefferences(id, player);
	}
	
	// -------------------------------------------- //
	// AFTER EVENTS
	// -------------------------------------------- //
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void after(PlayerTeleportEvent event)
	{
		Bukkit.getScheduler().scheduleSyncDelayedTask(p, new MCoreAfterPlayerTeleportEvent(event), 0);
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void after(PlayerRespawnEvent event)
	{
		Bukkit.getScheduler().scheduleSyncDelayedTask(p, new MCoreAfterPlayerRespawnEvent(event, event.getPlayer().getLocation()), 0);
	}
	
	// -------------------------------------------- //
	// EVENT TOOL: causedByKick
	// -------------------------------------------- //
	
	public static Map<String,String> kickedPlayerReasons = new HashMap<String,String>();
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void causedByKick(PlayerKickEvent event)
	{
		final String name = event.getPlayer().getName();
		kickedPlayerReasons.put(name, event.getReason());
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void causedByKick(PlayerQuitEvent event)
	{
		// We do the schedule in order for the set to be correct through out the whole MONITOR priority state.
		final String name = event.getPlayer().getName();
		Bukkit.getScheduler().scheduleSyncDelayedTask(MCore.p, new Runnable()
		{
			@Override
			public void run()
			{
				kickedPlayerReasons.remove(name);
			}
		});
	}
	
	// -------------------------------------------- //
	// PLAYER LEAVE EVENT
	// -------------------------------------------- //
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void leaveEventKickCall(PlayerKickEvent event)
	{
		new MCorePlayerLeaveEvent(event.getPlayer(), true, "kick", event.getReason()).run();
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void leaveEventQuitCall(PlayerQuitEvent event)
	{
		new MCorePlayerLeaveEvent(event.getPlayer(), false, "quit", null).run();
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void leaveEventQuitClear(PlayerQuitEvent event)
	{
		// We do the schedule in order for the set to be correct through out the whole MONITOR priority state.
		final String name = event.getPlayer().getName();
		Bukkit.getScheduler().scheduleSyncDelayedTask(MCore.p, new Runnable()
		{
			@Override
			public void run()
			{
				MCorePlayerLeaveEvent.player2event.remove(name);
			}
		});
	}
	
	// -------------------------------------------- //
	// SYNC PLAYER ON LOGON AND LEAVE
	// -------------------------------------------- //
	
	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public void syncOnPlayerLogin(PlayerLoginEvent event)
	{
		//p.log("syncOnPlayerLogin", event.getPlayer().getName());
		this.syncAllForPlayer(event.getPlayer());
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void syncOnPlayerLeave(MCorePlayerLeaveEvent event)
	{
		//p.log("syncOnPlayerLeave", event.getPlayer().getName());
		this.syncAllForPlayer(event.getPlayer());
	}
	
	public void syncAllForPlayer(Player player)
	{
		String playerName = player.getName();
		for (Coll<?, ?> coll : Coll.instances)
		{
			if (!(coll instanceof PlayerColl)) continue;
			PlayerColl<?> pcoll = (PlayerColl<?>)coll;
			pcoll.syncId(playerName);
		}
	}
}
