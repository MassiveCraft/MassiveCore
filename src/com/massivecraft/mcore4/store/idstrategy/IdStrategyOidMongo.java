package com.massivecraft.mcore4.store.idstrategy;

import com.massivecraft.mcore4.lib.bson.types.ObjectId;
import com.massivecraft.mcore4.store.CollInterface;

public class IdStrategyOidMongo extends IdStrategyAbstract<ObjectId, ObjectId>
{
	// -------------------------------------------- //
	// IMPLEMENTATION
	// -------------------------------------------- //
	
	@Override public ObjectId localToRemote(Object local) { return (ObjectId)local; }
	@Override public ObjectId remoteToLocal(Object remote) { return (ObjectId)remote; }
	
	@Override
	public ObjectId generateAttempt(CollInterface<?, ObjectId> coll)
	{
		return ObjectId.get();
	}
	
	//----------------------------------------------//
	// CONSTRUCTORS
	//----------------------------------------------//
	
	private IdStrategyOidMongo()
	{
		super("oid", ObjectId.class, ObjectId.class);
	}
	
	// -------------------------------------------- //
	// INSTANCE
	// -------------------------------------------- //
	
	protected static IdStrategyOidMongo instance = new IdStrategyOidMongo();
	public static IdStrategyOidMongo get()
	{
		return instance;
	}

}
