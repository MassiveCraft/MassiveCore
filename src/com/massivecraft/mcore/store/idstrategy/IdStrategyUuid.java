package com.massivecraft.mcore.store.idstrategy;

import java.util.UUID;

import com.massivecraft.mcore.adapter.UUIDAdapter;
import com.massivecraft.mcore.store.CollInterface;

public class IdStrategyUuid extends IdStrategyAbstract<UUID, String>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static IdStrategyUuid i = new IdStrategyUuid();
	public static IdStrategyUuid get() { return i; }
	private IdStrategyUuid() { super("uuid", UUID.class, String.class); }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override public String localToRemote(Object local) { return UUIDAdapter.convertUUIDToString((UUID)local); }
	@Override public UUID remoteToLocal(Object remote) { return UUIDAdapter.convertStringToUUID((String)remote); }
	
	@Override
	public UUID generateAttempt(CollInterface<?, UUID> coll)
	{
		return UUID.randomUUID();
	}
	
}
