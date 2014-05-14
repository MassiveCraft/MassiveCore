package com.massivecraft.mcore.teleport;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.Plugin;

import com.massivecraft.mcore.EngineAbstract;
import com.massivecraft.mcore.MCore;
import com.massivecraft.mcore.mixin.Mixin;
import com.massivecraft.mcore.util.IdUtil;
import com.massivecraft.mcore.util.MUtil;

public class EngineScheduledTeleport extends EngineAbstract
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static EngineScheduledTeleport i = new EngineScheduledTeleport();
	public static EngineScheduledTeleport get() { return i; }
	
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
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public Plugin getPlugin()
	{
		return MCore.get();
	}
	
	@Override
	public Long getPeriod()
	{
		return 1L;
	}
	
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
	
	// -------------------------------------------- //
	// LISTENER
	// -------------------------------------------- //
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerMoved(PlayerMoveEvent event)
	{
		// If the player moved from one block to another ...
		if (MUtil.isSameBlock(event.getFrom(), event.getTo())) return;
		
		// ... and there is a ScheduledTeleport ...
		ScheduledTeleport scheduledTeleport = teleporteeIdToScheduledTeleport.get(IdUtil.getId(event.getPlayer()));
		if (scheduledTeleport == null) return;
		
		// ... unschedule it ...
		scheduledTeleport.unschedule();
		
		// ... and inform the teleportee.
		Mixin.msgOne(scheduledTeleport.getTeleporteeId(), "<rose>Cancelled <i>teleport to <h>"+scheduledTeleport.getDestinationDesc()+"<i>.");
	}
	
}
