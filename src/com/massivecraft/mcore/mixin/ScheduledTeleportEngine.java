package com.massivecraft.mcore.mixin;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import com.massivecraft.mcore.MCore;
import com.massivecraft.mcore.util.MUtil;

public class ScheduledTeleportEngine implements Listener, Runnable
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static ScheduledTeleportEngine i = new ScheduledTeleportEngine();
	public static ScheduledTeleportEngine get() { return i; }
	
	// -------------------------------------------- //
	// SCHEDULED TELEPORT INDEX
	// -------------------------------------------- //
	
	protected Map<Player, ScheduledTeleport> teleporteeToScheduledTeleport = new ConcurrentHashMap<Player, ScheduledTeleport>();
	
	public boolean isScheduled(ScheduledTeleport st)
	{
		return this.teleporteeToScheduledTeleport.containsValue(st);
	}
	
	public ScheduledTeleport schedule(ScheduledTeleport st)
	{
		ScheduledTeleport old = this.teleporteeToScheduledTeleport.get(st.getTeleportee());
		if (old != null) old.unschedule();
		
		this.teleporteeToScheduledTeleport.put(st.getTeleportee(), st);
		
		st.setDueMillis(System.currentTimeMillis() + st.getDelaySeconds()*1000);
		
		return old;
	}
	
	public boolean unschedule(ScheduledTeleport st)
	{
		ScheduledTeleport old = this.teleporteeToScheduledTeleport.get(st.getTeleportee());
		if (old == null) return false;
		if (old != st) return false;
		
		return this.teleporteeToScheduledTeleport.remove(st.getTeleportee()) != null;
	}
	
	// -------------------------------------------- //
	// SETUP
	// -------------------------------------------- //
	
	public void setup()
	{
		Bukkit.getPluginManager().registerEvents(this, MCore.get());
		Bukkit.getScheduler().scheduleSyncRepeatingTask(MCore.get(), this, 1, 1);
	}
	
	// -------------------------------------------- //
	// LISTENER
	// -------------------------------------------- //
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerMoved(PlayerMoveEvent event)
	{
		// If the player moved from one block to another ...
		if (MUtil.isSameBlock(event.getFrom(), event.getTo())) return;
		
		// ... and there is a ScheduledTeleport ...
		ScheduledTeleport scheduledTeleport = teleporteeToScheduledTeleport.get(event.getPlayer());
		if (scheduledTeleport == null) return;
		
		// ... unschedule it ...
		scheduledTeleport.unschedule();
		
		// ... and inform the teleportee.
		Mixin.msg(scheduledTeleport.getTeleportee(), "<rose>Cancelled <i>teleport to <h>"+scheduledTeleport.getDestinationDesc()+"<i>.");
	}

	// -------------------------------------------- //
	// LISTENER
	// -------------------------------------------- //
	
	@Override
	public void run()
	{
		long now = System.currentTimeMillis();
		for (ScheduledTeleport st : teleporteeToScheduledTeleport.values())
		{
			if (st.isDue(now))
			{
				st.run();
			}
		}
	}
	
}
