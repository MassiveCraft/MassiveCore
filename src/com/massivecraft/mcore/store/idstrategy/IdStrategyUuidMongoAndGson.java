package com.massivecraft.mcore.store.idstrategy;

import java.util.UUID;

import com.massivecraft.mcore.store.CollInterface;

public class IdStrategyUuidMongoAndGson extends IdStrategyAbstract<UUID, String>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static IdStrategyUuidMongoAndGson i = new IdStrategyUuidMongoAndGson();
	public static IdStrategyUuidMongoAndGson get() { return i; }
	private IdStrategyUuidMongoAndGson() { super("uuid", UUID.class, String.class); }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override public String localToRemote(Object local) { return ((UUID)local).toString(); }
	@Override public UUID remoteToLocal(Object remote) { return UUID.fromString((String)remote); }
	
	@Override
	public UUID generateAttempt(CollInterface<?, UUID> coll)
	{
		return UUID.randomUUID();
	}
	
}
