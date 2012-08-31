package com.massivecraft.mcore4.store.storeadapter;

import com.massivecraft.mcore4.store.Coll;
import com.massivecraft.mcore4.store.accessor.Accessor;
import com.massivecraft.mcore4.xlib.gson.JsonElement;

public class StoreAdapterGson extends StoreAdapterAbstract
{
	public StoreAdapterGson()
	{
		super("gson");
	}

	@Override
	public Object read(Coll<?, ?> coll, Object entity)
	{
		return coll.mplugin().gson.toJsonTree(entity, coll.entityClass());
	}

	@Override
	public void write(Coll<?, ?> coll, Object raw, Object entity)
	{
		Object temp = coll.mplugin().gson.fromJson((JsonElement)raw, coll.entityClass());
		Accessor.get(coll.entityClass()).copy(temp, entity);
	}
	
	// -------------------------------------------- //
	// INSTANCE
	// -------------------------------------------- //
	
	protected static StoreAdapterGson instance = new StoreAdapterGson();
	public static StoreAdapterGson get()
	{
		return instance;
	}
}
