package com.massivecraft.mcore4;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import com.massivecraft.mcore4.event.MCoreAfterPlayerRespawnEvent;
import com.massivecraft.mcore4.event.MCoreAfterPlayerTeleportEvent;
import com.massivecraft.mcore4.event.MCorePlayerLeaveEvent;
import com.massivecraft.mcore4.persist.IClassManager;
import com.massivecraft.mcore4.persist.Persist;
import com.massivecraft.mcore4.store.Coll;
import com.massivecraft.mcore4.store.PlayerColl;
import com.massivecraft.mcore4.util.MUtil;

public class InternalListener implements Listener
{
	MCore p;
	
	public InternalListener(MCore p)
	{
		this.p = p;
		Bukkit.getServer().getPluginManager().registerEvents(this, this.p);
	}
	
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
	// STORE SYSTEM: SYNC IN AND SYNC OUT
	// -------------------------------------------- //
	// There are some forced syncs of players in all collections so developers can rest assure the data is up to date.
	// PlayerLoginEvent LOW. LOWEST is left for anti flood and bans.
	// PlayerKickEvent MONITOR.
	// PlayerQuitEvent MONITOR.
	// Why do we sync at both PlayerKickEvent and PlayerQuitEvent you may wonder?
	// PlayerQuitEvent do not always fire, for example due to a spoutcraft bug
	// and it also fires AFTER the player left the server. In kick cases we can sync
	// directly before the player leaves the server. That is great.
	
	public void syncAllForPlayer(Player player)
	{
		String playerName = player.getName();
		for (Coll<?, ?> coll : Coll.instances)
		{
			if (!(coll instanceof PlayerColl)) continue;
			PlayerColl<?> pcoll = (PlayerColl<?>)coll;
			pcoll.syncId(playerName);
			//ModificationState mstate = pcoll.syncId(playerName);
			//p.log("syncAllForPlayer", coll.name(), playerName, pcoll.syncId(playerName), pcoll.syncId(playerName));
		}
	}
	
	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public void syncAllForPlayer(PlayerLoginEvent event)
	{
		//p.log("syncAllForPlayer PlayerLoginEvent LOW", event.getPlayer().getName());
		this.syncAllForPlayer(event.getPlayer());
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void syncAllForPlayer(PlayerKickEvent event)
	{
		//p.log("syncAllForPlayer PlayerKickEvent MONITOR", event.getPlayer().getName());
		new MCorePlayerLeaveEvent(event.getPlayer(), true, event.getReason()).run();
		this.syncAllForPlayer(event.getPlayer());
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void syncAllForPlayer(PlayerQuitEvent event)
	{
		//p.log("syncAllForPlayer PlayerQuitEvent MONITOR", event.getPlayer().getName());
		if (!MUtil.causedByKick(event))
		{
			new MCorePlayerLeaveEvent(event.getPlayer(), false, null).run();
		}
		
		this.syncAllForPlayer(event.getPlayer());
	}
	
}
