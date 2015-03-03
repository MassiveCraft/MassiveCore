package com.massivecraft.massivecore.event;

import org.bukkit.World;
import org.bukkit.event.HandlerList;

public class EventMassiveCoreWorldSetTime extends EventMassiveCore
{
	// -------------------------------------------- //
	// REQUIRED EVENT CODE
	// -------------------------------------------- //
	
	private static final HandlerList handlers = new HandlerList();
	@Override public HandlerList getHandlers() { return handlers; }
	public static HandlerList getHandlerList() { return handlers; }
	
	// -------------------------------------------- //
	// FIELD
	// -------------------------------------------- //
	
	private final World world;
	public World getWorld() { return this.world; }
	
	private long time;
	public long getTime() { return this.time; }
	public void setTime(long time) { this.time = time; }
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public EventMassiveCoreWorldSetTime(World world, long time)
	{
		this.world = world;
		this.time = time;
	}
	
	// -------------------------------------------- //
	// UTIL
	// -------------------------------------------- //
	
	public static void setTime(World world, long time)
	{
		EventMassiveCoreWorldSetTime event = new EventMassiveCoreWorldSetTime(world, time);
		event.run();
		if (event.isCancelled()) return;
		world.setTime(event.getTime());
	}	
	
	public static void setDay(World world)
	{
		setTime(world, 0);
	}
	public static void setNight(World world)
	{
		setTime(world, 14000);
	}
	
	public static void ensureDay(World world)
	{
		if (isDay(world)) return;
		setDay(world);
	}
	public static void ensureNight(World world)
	{
		if (isNight(world)) return;
		setNight(world);
	}
	
	public static boolean isDay(long time)
	{
		return time <= 11500;
	}
	public static boolean isNight(long time)
	{
		return time >= 14000 && time <= 22000;
	}
	
	public static boolean isDay(World world)
	{
		return isDay(world.getTime());
	}
	public static boolean isNight(World world)
	{
		return isNight(world.getTime());
	}
	
}
