package com.massivecraft.mcore.store.storeadapter;

import com.massivecraft.mcore.store.Coll;
import com.massivecraft.mcore.xlib.gson.JsonElement;

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
		if (raw == null) throw new NullPointerException("raw");
		if (entity == null) throw new NullPointerException("entity");
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
