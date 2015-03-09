package com.massivecraft.massivecore;

import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.scheduler.BukkitTask;

import com.massivecraft.massivecore.event.EventMassiveCoreEngineActivate;
import com.massivecraft.massivecore.event.EventMassiveCoreEngineDeactivate;

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
		// Event
		EventMassiveCoreEngineActivate event = new EventMassiveCoreEngineActivate(this);
		event.run();
		if (event.isCancelled()) return;
		
		// Listener
		Bukkit.getPluginManager().registerEvents(this, this.getPlugin());
		
		// Scheduler
		if (this.getPeriod() != null && this.getPeriod() > 0)
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
		
		// Registered
		Engine.ENGINES.add(this);
	}

	@Override
	public void deactivate()
	{
		// Event
		EventMassiveCoreEngineDeactivate event = new EventMassiveCoreEngineDeactivate(this);
		event.run();
		if (event.isCancelled()) return;
		
		//Listener
		HandlerList.unregisterAll(this);
		
		// Scheduler
		if (this.task != null)
		{
			this.task.cancel();
			this.task = null;
		}
		
		// Unregistered
		Engine.ENGINES.remove(this);
	}
	
	@Override
	public Long getDelay()
	{
		return 0L;
	}
	
	@Override
	public Long getPeriod()
	{
		return -1L;
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
