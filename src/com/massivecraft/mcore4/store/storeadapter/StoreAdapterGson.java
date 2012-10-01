package com.massivecraft.mcore4.store.storeadapter;

import com.massivecraft.mcore4.store.Coll;
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
		return coll.getMplugin().gson.toJsonTree(entity, coll.getEntityClass());
	}

	@Override
	public void write(Coll<?, ?> coll, Object raw, Object entity)
	{
		Object temp = coll.getMplugin().gson.fromJson((JsonElement)raw, coll.getEntityClass());
		coll.copy(temp, entity);
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
