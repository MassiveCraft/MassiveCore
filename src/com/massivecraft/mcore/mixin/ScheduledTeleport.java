package com.massivecraft.mcore.mixin;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.massivecraft.mcore.MCore;
import com.massivecraft.mcore.PS;

public class ScheduledTeleport implements Runnable
{
	// -------------------------------------------- //
	// CONSTANTS
	// -------------------------------------------- //
	
	public final static transient int NON_SCHEDULED_TASK_ID = -1;
	
	// -------------------------------------------- //
	// STATIC INDEX
	// -------------------------------------------- //
	
	public static Map<Player, ScheduledTeleport> teleporteeToScheduledTeleport = new ConcurrentHashMap<Player, ScheduledTeleport>();
	
	public static void schedule(ScheduledTeleport scheduledTeleport)
	{
		if (isScheduled(scheduledTeleport)) return;
		Bukkit.getScheduler().scheduleSyncDelayedTask(MCore.get(), scheduledTeleport, scheduledTeleport.getDelaySeconds()*20);
	}
	
	public static boolean isScheduled(ScheduledTeleport scheduledTeleport)
	{
		return teleporteeToScheduledTeleport.containsKey(scheduledTeleport.getTeleportee());
	}
	
	// -------------------------------------------- //
	// FIELDS & RAW-DATA ACCESS
	// -------------------------------------------- //
	
	private final Player teleportee;
	public Player getTeleportee() { return this.teleportee; }
	
	private final PS destinationPs;
	public PS getDestinationPs() { return this.destinationPs; }
	
	private final String destinationDesc;
	public String getDestinationDesc() { return this.destinationDesc; }
	
	private final int delaySeconds;
	public int getDelaySeconds() { return this.delaySeconds; }
	
	private int taskId;
	public int getTaskId() { return this.taskId; }
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public ScheduledTeleport(Player teleportee, PS destinationPs, String destinationDesc, int delaySeconds)
	{
		this.teleportee = teleportee;
		this.destinationPs = destinationPs;
		this.destinationDesc = destinationDesc;
		this.delaySeconds = delaySeconds;
		this.taskId = NON_SCHEDULED_TASK_ID;
	}
	
	// -------------------------------------------- //
	// SCHEDULING
	// -------------------------------------------- //
		
	public boolean isScheduled()
	{
		return this.taskId != NON_SCHEDULED_TASK_ID;
	}
	
	public ScheduledTeleport schedule()
	{
		ScheduledTeleport old = teleporteeToScheduledTeleport.get(this.getTeleportee());
		if (old != null) old.unschedule();
		
		teleporteeToScheduledTeleport.put(this.getTeleportee(), this);
		
		this.taskId = Bukkit.getScheduler().scheduleSyncDelayedTask(MCore.get(), this, this.getDelaySeconds()*20);
		
		return old;
	}
	
	public boolean unschedule()
	{
		Bukkit.getScheduler().cancelTask(this.getTaskId());
		
		this.taskId = NON_SCHEDULED_TASK_ID;
		
		return teleporteeToScheduledTeleport.remove(this.getTeleportee()) != null;
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void run()
	{
		this.unschedule();
		if (!teleportee.isOnline()) return;
		try
		{
			Mixin.teleport(this.teleportee, this.destinationPs, this.destinationDesc);
		}
		catch (TeleporterException e)
		{
			this.teleportee.sendMessage(e.getMessage());
		}
	}
	
}
