package com.massivecraft.mcore.store.idstrategy;

import java.util.UUID;

import com.massivecraft.mcore.store.CollInterface;

public class IdStrategyUuidMongoAndGson extends IdStrategyAbstract<UUID, String>
{
	// -------------------------------------------- //
	// IMPLEMENTATION
	// -------------------------------------------- //
	
	@Override public String localToRemote(Object local) { return ((UUID)local).toString(); }
	@Override public UUID remoteToLocal(Object remote) { return UUID.fromString((String)remote); }
	
	@Override
	public UUID generateAttempt(CollInterface<?, UUID> coll)
	{
		return UUID.randomUUID();
	}
	
	//----------------------------------------------//
	// CONSTRUCTORS
	//----------------------------------------------//
	
	private IdStrategyUuidMongoAndGson()
	{
		super("uuid", UUID.class, String.class);
	}
	
	// -------------------------------------------- //
	// INSTANCE
	// -------------------------------------------- //
	
	protected static IdStrategyUuidMongoAndGson instance = new IdStrategyUuidMongoAndGson();
	public static IdStrategyUuidMongoAndGson get()
	{
		return instance;
	}
}
