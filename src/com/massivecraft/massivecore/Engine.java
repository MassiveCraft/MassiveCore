package com.massivecraft.massivecore;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockMultiPlaceEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.scheduler.BukkitTask;

import com.massivecraft.massivecore.predicate.PredicateStartsWithIgnoreCase;

public abstract class Engine implements Active, Listener, Runnable
{
	// -------------------------------------------- //
	// PLUGIN
	// -------------------------------------------- //
	
	private MassivePlugin plugin = null;
	public MassivePlugin getPlugin() { return this.plugin; }
	public boolean hasPlugin() { return this.getPlugin() != null; }
	public void setPlugin(MassivePlugin plugin) { this.plugin = plugin; }
	public void setPluginSoft(MassivePlugin plugin)
	{
		if (this.hasPlugin()) return;
		this.plugin = plugin;
	}
	
	// -------------------------------------------- //
	// TASK
	// -------------------------------------------- //
	
	private Long delay = 0L;
	public Long getDelay() { return this.delay; }
	public void setDelay(Long delay) { this.delay = delay; }
	
	private Long period = null;
	public Long getPeriod() { return this.period; }
	public void setPeriod(Long period) { this.period = period; }
	
	private boolean sync = true;
	public boolean isSync() { return this.sync; }
	public void setSync(boolean sync) { this.sync = sync; }
	
	private BukkitTask task = null;
	public BukkitTask getTask() { return this.task; }
	public Integer getTaskId() { return this.task == null ? null : this.task.getTaskId(); }
	
	@Override
	public void run()
	{
		
	}
	
	// -------------------------------------------- //
	// ACTIVE
	// -------------------------------------------- //
	
	private boolean active = false;
	
	@Override
	public boolean isActive()
	{
		return this.active;
	}
	
	@Override
	public void setActive(boolean active)
	{
		this.setActiveListener(active);
		this.setActiveTask(active);
		this.setActiveInner(active);
		this.active = active;
	}
	
	@Override
	public MassivePlugin setActivePlugin(MassivePlugin activePlugin)
	{
		this.setPluginSoft(activePlugin);
		return null;
	}
	
	@Override
	public MassivePlugin getActivePlugin()
	{
		return this.getPlugin();
	}
	
	// -------------------------------------------- //
	// ACTIVE > EVENTS
	// -------------------------------------------- //
	
	public void setActiveListener(boolean active)
	{
		if (active)
		{
			Bukkit.getPluginManager().registerEvents(this, this.getPlugin());
		}
		else
		{
			HandlerList.unregisterAll(this);
		}
	}
	
	// -------------------------------------------- //
	// ACTIVE > TASK
	// -------------------------------------------- //
	
	public void setActiveTask(boolean active)
	{
		if (active)
		{
			if (this.getPeriod() != null)
			{
				if (this.isSync())
				{
					this.task = Bukkit.getScheduler().runTaskTimer(this.getPlugin(), this, this.getDelay(), this.getPeriod());
				}
				else
				{
					this.task = Bukkit.getScheduler().runTaskTimerAsynchronously(this.getPlugin(), this, this.getDelay(), this.getPeriod());
				}
			}
		}
		else
		{
			if (this.task != null)
			{
				this.task.cancel();
				this.task = null;
			}
		}
	}
	
	// -------------------------------------------- //
	// ACTIVE > INNER
	// -------------------------------------------- //
	
	public void setActiveInner(boolean active)
	{
		// NOTE: Here you can add some extra custom logic.
	}
	
	// -------------------------------------------- //
	// UTIL
	// -------------------------------------------- //
	
	public static final PredicateStartsWithIgnoreCase STARTING_WITH_FAKE = PredicateStartsWithIgnoreCase.get("fake");
	public static boolean isFake(Event event)
	{
		final Class<?> clazz = event.getClass();
		if (event instanceof BlockPlaceEvent)
		{
			return ! BlockPlaceEvent.class.equals(clazz) && ! BlockMultiPlaceEvent.class.equals(clazz);
		}
		else
		{
			return STARTING_WITH_FAKE.apply(clazz.getSimpleName());
		}
	}
	
}
