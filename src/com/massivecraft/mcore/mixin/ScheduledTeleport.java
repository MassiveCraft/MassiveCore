package com.massivecraft.mcore.mixin;

import org.bukkit.entity.Player;

import com.massivecraft.mcore.PS;

public class ScheduledTeleport implements Runnable
{
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
	
	private long dueMillis;
	public long getDueMillis() { return this.dueMillis; }
	public void setDueMillis(long dueMillis) { this.dueMillis = dueMillis; }
	public boolean isDue(long now) { return now >= this.dueMillis; }
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public ScheduledTeleport(Player teleportee, PS destinationPs, String destinationDesc, int delaySeconds)
	{
		this.teleportee = teleportee;
		this.destinationPs = destinationPs;
		this.destinationDesc = destinationDesc;
		this.delaySeconds = delaySeconds;
		this.dueMillis = 0;
	}
	
	// -------------------------------------------- //
	// SCHEDULING
	// -------------------------------------------- //
		
	public boolean isScheduled()
	{
		return ScheduledTeleportEngine.get().isScheduled(this);
	}
	
	public ScheduledTeleport schedule()
	{
		return ScheduledTeleportEngine.get().schedule(this);
	}
	
	public boolean unschedule()
	{
		return ScheduledTeleportEngine.get().unschedule(this);
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
