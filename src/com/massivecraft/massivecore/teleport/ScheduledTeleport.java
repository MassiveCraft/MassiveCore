package com.massivecraft.massivecore.teleport;

import com.massivecraft.massivecore.engine.EngineMassiveCoreScheduledTeleport;
import com.massivecraft.massivecore.mixin.Mixin;
import com.massivecraft.massivecore.mixin.TeleporterException;

public class ScheduledTeleport implements Runnable
{
	// -------------------------------------------- //
	// FIELDS & RAW-DATA ACCESS
	// -------------------------------------------- //
	
	private final String teleporteeId;
	public String getTeleporteeId() { return this.teleporteeId; }
	
	private final Destination destination;
	public Destination getDestination() { return this.destination; }
	
	private final int delaySeconds;
	public int getDelaySeconds() { return this.delaySeconds; }
	
	private long dueMillis;
	public long getDueMillis() { return this.dueMillis; }
	public void setDueMillis(long dueMillis) { this.dueMillis = dueMillis; }
	public boolean isDue(long now) { return now >= this.dueMillis; }
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public ScheduledTeleport(String teleporteeId, Destination destination, int delaySeconds)
	{
		this.teleporteeId = teleporteeId;
		this.destination = destination;
		this.delaySeconds = delaySeconds;
		this.dueMillis = 0;
	}
	
	// -------------------------------------------- //
	// SCHEDULING
	// -------------------------------------------- //
		
	public boolean isScheduled()
	{
		return EngineMassiveCoreScheduledTeleport.get().isScheduled(this);
	}
	
	public ScheduledTeleport schedule()
	{
		return EngineMassiveCoreScheduledTeleport.get().schedule(this);
	}
	
	public boolean unschedule()
	{
		return EngineMassiveCoreScheduledTeleport.get().unschedule(this);
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
			Mixin.teleport(this.getTeleporteeId(), this.getDestination(), 0);
		}
		catch (TeleporterException e)
		{
			Mixin.messageOne(this.getTeleporteeId(), e.getMessage());
		}
	}
	
}
