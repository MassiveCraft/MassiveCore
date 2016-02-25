package com.massivecraft.massivecore.engine;

import com.massivecraft.massivecore.Engine;
import com.massivecraft.massivecore.store.Coll;

public class EngineMassiveCoreCollTick extends Engine
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static EngineMassiveCoreCollTick i = new EngineMassiveCoreCollTick();
	public static EngineMassiveCoreCollTick get() { return i; }
	public EngineMassiveCoreCollTick()
	{
		this.setPeriod(1L);
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
