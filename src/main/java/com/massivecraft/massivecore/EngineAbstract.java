package com.massivecraft.massivecore;

import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.scheduler.BukkitTask;

public abstract class EngineAbstract implements Engine
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	private BukkitTask task;
	@Override public Integer getTaskId() { return this.task.getTaskId(); }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void activate()
	{
		Bukkit.getPluginManager().registerEvents(this, this.getPlugin());
		if (this.getPeriod() != null)
		{
			if (this.isSync())
			{
				Bukkit.getScheduler().runTaskTimer(this.getPlugin(), this, this.getDelay(), this.getPeriod());
			}
			else
			{
				Bukkit.getScheduler().runTaskTimerAsynchronously(this.getPlugin(), this, this.getDelay(), this.getPeriod());
			}
		}
	}

	@Override
	public void deactivate()
	{
		HandlerList.unregisterAll(this);
		if (this.task != null)
		{
			this.task.cancel();
			this.task = null;
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
	
	@Override
	public BukkitTask getBukkitTask()
	{
		return this.task;
	}
	
	@Override
	public boolean isSync()
	{
		return true;
	}
	
}
