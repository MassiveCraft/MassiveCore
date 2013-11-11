package com.massivecraft.mcore;

import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;

public abstract class EngineAbstract implements Engine
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	private Integer taskId;
	@Override public Integer getTaskId() { return this.taskId; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void activate()
	{
		Bukkit.getPluginManager().registerEvents(this, this.getPlugin());
		if (this.getPeriod() != null)
		{
			this.taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(this.getPlugin(), this, this.getDelay(), this.getPeriod());
		}
	}

	@Override
	public void deactivate()
	{
		HandlerList.unregisterAll(this);
		if (this.getTaskId() != null)
		{
			Bukkit.getScheduler().cancelTask(this.getTaskId());
			this.taskId = null;
		}
	}
	
	@Override
	public Long getDelay()
	{
		return 0L;
	}
	
	@Override
	public Long getPeriod()
	{
		return null;
	}
	
	@Override
	public void run()
	{
		
	}
	
}
