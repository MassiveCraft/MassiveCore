package com.massivecraft.mcore.store.idstrategy;

import com.massivecraft.mcore.store.CollInterface;
import com.massivecraft.mcore.xlib.bson.types.ObjectId;

public class IdStrategyOid extends IdStrategyAbstract
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static IdStrategyOid i = new IdStrategyOid();
	public static IdStrategyOid get() { return i; }
	private IdStrategyOid() { super("oid"); }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public String generateAttempt(CollInterface<?> coll)
	{
		return ObjectId.get().toString();
	}
	
}
