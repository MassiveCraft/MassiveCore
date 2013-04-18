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
		long now = System.currentTimeMillis();
		long nowInvocationNumber = now / this.getDelayMillis();
		long previousInvocationNumber = this.getPreviousMillis() / this.getDelayMillis();
		
		if (nowInvocationNumber == previousInvocationNumber) return;
		
		this.invoke();
		
		this.setPreviousMillis(now);
	}
	
	// -------------------------------------------- //
	// EIGEN
	// -------------------------------------------- //
	
	public int schedule(Plugin plugin)
	{
		return Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, this, 1, 1);
	}
	
	// -------------------------------------------- //
	// ABSTRACT
	// -------------------------------------------- //
	
	public abstract void invoke();

}
