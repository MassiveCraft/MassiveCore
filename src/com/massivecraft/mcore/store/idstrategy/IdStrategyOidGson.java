package com.massivecraft.mcore.store.idstrategy;

import com.massivecraft.mcore.store.CollInterface;
import com.massivecraft.mcore.xlib.bson.types.ObjectId;

public class IdStrategyOidGson extends IdStrategyAbstract<ObjectId, String>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static IdStrategyOidGson i = new IdStrategyOidGson();
	public static IdStrategyOidGson get() { return i; }
	private IdStrategyOidGson() { super("oid", ObjectId.class, String.class); }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override public String localToRemote(Object local) { return ((ObjectId)local).toStringBabble(); }
	@Override public ObjectId remoteToLocal(Object remote) { return ObjectId.massageToObjectId((String)remote); }
	
	@Override
	public ObjectId generateAttempt(CollInterface<?, ObjectId> coll)
	{
		return ObjectId.get();
	}
	
}
