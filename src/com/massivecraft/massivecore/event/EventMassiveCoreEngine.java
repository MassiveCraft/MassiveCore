package com.massivecraft.massivecore.event;

import com.massivecraft.massivecore.Engine;

public abstract class EventMassiveCoreEngine extends EventMassiveCore
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	private final Engine engine;
	public Engine getEngine() { return this.engine; };

	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public EventMassiveCoreEngine(Engine engine)
	{
		this.engine = engine;
	}
	
}
