package com.massivecraft.massivecore.teleport;

import com.massivecraft.massivecore.mixin.Mixin;
import com.massivecraft.massivecore.mixin.TeleporterException;

public class ScheduledTeleport implements Runnable
{
	// -------------------------------------------- //
	// FIELDS & RAW-DATA ACCESS
	// -------------------------------------------- //
	
	private final String teleporteeId;
	public String getTeleporteeId() { return this.teleporteeId; }
	
	private final PSGetter destinationGetter;
	public PSGetter getDestinationGetter() { return this.destinationGetter; }
	
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
	
	public ScheduledTeleport(String teleporteeId, PSGetter destinationGetter, String destinationDesc, int delaySeconds)
	{
		this.teleporteeId = teleporteeId;
		this.destinationGetter = destinationGetter;
		this.destinationDesc = destinationDesc;
		this.delaySeconds = delaySeconds;
		this.dueMillis = 0;
	}
	
	// -------------------------------------------- //
	// SCHEDULING
	// -------------------------------------------- //
		
	public boolean isScheduled()
	{
		return EngineScheduledTeleport.get().isScheduled(this);
	}
	
	public ScheduledTeleport schedule()
	{
		return EngineScheduledTeleport.get().schedule(this);
	}
	
	public boolean unschedule()
	{
		return EngineScheduledTeleport.get().unschedule(this);
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
			Mixin.teleport(this.getTeleporteeId(), this.getDestinationGetter(), this.getDestinationDesc());
		}
		catch (TeleporterException e)
		{
			Mixin.messageOne(this.getTeleporteeId(), e.getMessage());
		}
	}
	
}
