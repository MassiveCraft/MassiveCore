package com.massivecraft.mcore.mixin;

import com.massivecraft.mcore.ps.PS;

public class ScheduledTeleport implements Runnable
{
	// -------------------------------------------- //
	// FIELDS & RAW-DATA ACCESS
	// -------------------------------------------- //
	
	private final String teleporteeId;
	public String getTeleporteeId() { return this.teleporteeId; }
	
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
	
	public ScheduledTeleport(String teleporteeId, PS destinationPs, String destinationDesc, int delaySeconds)
	{
		this.teleporteeId = teleporteeId;
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
		
		try
		{
			Mixin.teleport(this.getTeleporteeId(), this.getDestinationPs(), this.getDestinationDesc());
		}
		catch (TeleporterException e)
		{
			Mixin.message(this.getTeleporteeId(), e.getMessage());
		}
	}
	
}
