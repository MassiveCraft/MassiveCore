package com.massivecraft.massivecore.engine;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import com.massivecraft.massivecore.Engine;
import com.massivecraft.massivecore.event.EventMassiveCorePlayerLeave;
import com.massivecraft.massivecore.mixin.Mixin;
import com.massivecraft.massivecore.teleport.ScheduledTeleport;
import com.massivecraft.massivecore.util.IdUtil;
import com.massivecraft.massivecore.util.MUtil;
import com.massivecraft.massivecore.util.TimeUnit;

public class EngineMassiveCoreScheduledTeleport extends Engine
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static EngineMassiveCoreScheduledTeleport i = new EngineMassiveCoreScheduledTeleport();
	public static EngineMassiveCoreScheduledTeleport get() { return i; }
	public EngineMassiveCoreScheduledTeleport()
	{
		this.setPeriod(1L);
	}
	
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
		
		st.setDueMillis(System.currentTimeMillis() + st.getDelaySeconds()*TimeUnit.MILLIS_PER_SECOND);
		
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
	// LISTENER: CANCEL TELEPORT
	// -------------------------------------------- //
	
	public void cancelTeleport(Player player)
	{
		if (MUtil.isntPlayer(player)) return;
		
		// If there there is a ScheduledTeleport ...
		ScheduledTeleport scheduledTeleport = teleporteeIdToScheduledTeleport.get(IdUtil.getId(player));
		if (scheduledTeleport == null) return;
		
		// ... unschedule it ...
		scheduledTeleport.unschedule();
		
		// ... and inform the teleportee.
		Mixin.msgOne(scheduledTeleport.getTeleporteeId(), "<rose>Cancelled <i>teleport to <h>"+scheduledTeleport.getDestination().getDesc(scheduledTeleport.getTeleporteeId())+"<i>.");
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void cancelTeleport(PlayerMoveEvent event)
	{
		if (MUtil.isSameBlock(event)) return;
		
		final Player player = event.getPlayer();
		this.cancelTeleport(player);
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void cancelTeleport(EntityDamageEvent event)
	{
		final Entity entity = event.getEntity();
		if (MUtil.isntPlayer(entity)) return;
		final Player player = (Player)entity;
		
		this.cancelTeleport(player);
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void cancelTeleport(PlayerDeathEvent event)
	{
		final Player player = event.getEntity();
		if (MUtil.isntPlayer(player)) return;
		
		this.cancelTeleport(player);
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void cancelTeleport(EventMassiveCorePlayerLeave event)
	{
		if ( ! Mixin.isActualLeave(event)) return;
		final Player player = event.getPlayer();
		
		this.cancelTeleport(player);
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void cancelTeleport(InventoryOpenEvent event)
	{
		final HumanEntity human = event.getPlayer();
		if (MUtil.isntPlayer(human)) return;
		final Player player = (Player)human;
		
		this.cancelTeleport(player);
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void cancelTeleport(InventoryClickEvent event)
	{
		final HumanEntity human = event.getWhoClicked();
		if (MUtil.isntPlayer(human)) return;
		final Player player = (Player)human;
		
		this.cancelTeleport(player);
	}
	
}
