package com.massivecraft.mcore.store.idstrategy;

import com.massivecraft.mcore.store.CollInterface;
import com.massivecraft.mcore.xlib.bson.types.ObjectId;

public class IdStrategyOidMongo extends IdStrategyAbstract<ObjectId, ObjectId>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static IdStrategyOidMongo i = new IdStrategyOidMongo();
	public static IdStrategyOidMongo get() { return i; }
	private IdStrategyOidMongo() { super("oid", ObjectId.class, ObjectId.class); }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override public ObjectId localToRemote(Object local) { return (ObjectId)local; }
	@Override public ObjectId remoteToLocal(Object remote) { return (ObjectId)remote; }
	
	@Override
	public ObjectId generateAttempt(CollInterface<?, ObjectId> coll)
	{
		return ObjectId.get();
	}

}
