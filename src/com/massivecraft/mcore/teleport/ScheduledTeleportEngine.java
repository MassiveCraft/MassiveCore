package com.massivecraft.mcore.teleport;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import com.massivecraft.mcore.MCore;
import com.massivecraft.mcore.mixin.Mixin;
import com.massivecraft.mcore.util.MUtil;
import com.massivecraft.mcore.util.SenderUtil;

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
	
	protected Map<String, ScheduledTeleport> teleporteeIdToScheduledTeleport = new ConcurrentHashMap<String, ScheduledTeleport>();
	
	public boolean isScheduled(ScheduledTeleport st)
	{
		return this.teleporteeIdToScheduledTeleport.containsValue(st);
	}
	
	public ScheduledTeleport schedule(ScheduledTeleport st)
	{
		ScheduledTeleport old = this.teleporteeIdToScheduledTeleport.get(st.getTeleporteeId());
		if (old != null) old.unschedule();
		
		this.teleporteeIdToScheduledTeleport.put(st.getTeleporteeId(), st);
		
		st.setDueMillis(System.currentTimeMillis() + st.getDelaySeconds()*1000);
		
		return old;
	}
	
	public boolean unschedule(ScheduledTeleport st)
	{
		ScheduledTeleport old = this.teleporteeIdToScheduledTeleport.get(st.getTeleporteeId());
		if (old == null) return false;
		if (old != st) return false;
		
		return this.teleporteeIdToScheduledTeleport.remove(st.getTeleporteeId()) != null;
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
		ScheduledTeleport scheduledTeleport = teleporteeIdToScheduledTeleport.get(SenderUtil.getSenderId(event.getPlayer()));
		if (scheduledTeleport == null) return;
		
		// ... unschedule it ...
		scheduledTeleport.unschedule();
		
		// ... and inform the teleportee.
		Mixin.msg(scheduledTeleport.getTeleporteeId(), "<rose>Cancelled <i>teleport to <h>"+scheduledTeleport.getDestinationDesc()+"<i>.");
	}

	// -------------------------------------------- //
	// LISTENER
	// -------------------------------------------- //
	
	@Override
	public void run()
	{
		long now = System.currentTimeMillis();
		for (ScheduledTeleport st : teleporteeIdToScheduledTeleport.values())
		{
			if (st.isDue(now))
			{
				st.run();
			}
		}
	}
	
}
