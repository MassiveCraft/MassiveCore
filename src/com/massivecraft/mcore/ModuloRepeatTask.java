package com.massivecraft.mcore;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

/**
 * This class will allow you to create non-tps-dependent repeating tasks.
 * It makes use of the Bukkit scheduler internally.
 */
public abstract class ModuloRepeatTask implements Runnable
{
	// -------------------------------------------- //
	// FIELDS: RAW
	// -------------------------------------------- //
	
	// How many milliseconds should approximately pass between each invocation?
	private long delayMillis;
	public long getDelayMillis() { return this.delayMillis; }
	public void setDelayMillis(long delayMillis) { this.delayMillis = delayMillis; }
	
	// When did the last invocation occur?
	private long previousMillis;
	public long getPreviousMillis() { return this.previousMillis; }
	public void setPreviousMillis(long previousMillis) { this.previousMillis = previousMillis; }
	
	private Integer taskId = null;
	
	// -------------------------------------------- //
	// INVOCATION NUMBER CALCULATION
	// -------------------------------------------- //
	
	public long getInvocation(long now)
	{
		return now / this.getDelayMillis();
	}
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public ModuloRepeatTask()
	{
		this(0);
	}
	
	public ModuloRepeatTask(long delayMilis)
	{
		this(delayMilis, System.currentTimeMillis());
	}
	
	public ModuloRepeatTask(long delayMilis, long previousMillis)
	{
		this.delayMillis = delayMilis;
		this.previousMillis = previousMillis;
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void run()
	{
		// So the delay millis is lower than one? (could for example be zero)
		// This probably means the task should not be executed at all.
		if (this.getDelayMillis() < 1) return;
			
		long nowMillis = System.currentTimeMillis();
		long previousMillis = this.getPreviousMillis();
		
		long currentInvocation = this.getInvocation(nowMillis);
		long previousInvocation = this.getInvocation(previousMillis);
		
		if (currentInvocation == previousInvocation) return;
		
		this.invoke(nowMillis);
		
		this.setPreviousMillis(nowMillis);
	}
	
	// -------------------------------------------- //
	// EIGEN
	// -------------------------------------------- //
	
	@Deprecated
	public int schedule(Plugin plugin)
	{
		this.activate(plugin);
		return this.taskId;
	}
	
	public void activate(Plugin plugin)
	{
		this.taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, this, 1, 1);
	}
	
	public void deactivate()
	{
		if (this.taskId == null) return;
		Bukkit.getScheduler().cancelTask(this.taskId);
	}
	
	// -------------------------------------------- //
	// ABSTRACT
	// -------------------------------------------- //
	
	public abstract void invoke(long now);

}
