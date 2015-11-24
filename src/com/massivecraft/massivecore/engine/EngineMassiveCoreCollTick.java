package com.massivecraft.massivecore.engine;

import org.bukkit.plugin.Plugin;

import com.massivecraft.massivecore.EngineAbstract;
import com.massivecraft.massivecore.MassiveCore;
import com.massivecraft.massivecore.store.Coll;

public class EngineMassiveCoreCollTick extends EngineAbstract
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	protected static EngineMassiveCoreCollTick i = new EngineMassiveCoreCollTick();
	public static EngineMassiveCoreCollTick get() { return i; }
	
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
