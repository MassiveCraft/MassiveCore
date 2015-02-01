package com.massivecraft.massivecore;

import org.bukkit.plugin.Plugin;

import com.massivecraft.massivecore.store.Coll;

public class EngineCollTick extends EngineAbstract
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	protected static EngineCollTick i = new EngineCollTick();
	public static EngineCollTick get() { return i; }
	
	// -------------------------------------------- //
	// OVERRIDE: ENGINE
	// -------------------------------------------- //
	
	@Override
	public Plugin getPlugin()
	{
		return MassiveCore.get();
	}
	
	@Override
	public Long getPeriod()
	{
		return 1L;
	}
	
	// -------------------------------------------- //
	// OVERRIDE: RUNNABLE
	// -------------------------------------------- //
	
	@Override
	public void run()
	{
		for (Coll<?> coll : Coll.getInstances())
		{
			coll.onTick();
		}
	}
	
}
