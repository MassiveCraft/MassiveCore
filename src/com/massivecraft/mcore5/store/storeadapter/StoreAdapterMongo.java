package com.massivecraft.mcore5.store.storeadapter;

import com.massivecraft.mcore5.store.Coll;
import com.massivecraft.mcore5.xlib.gson.JsonElement;
import com.massivecraft.mcore5.xlib.mongodb.DBObject;

public class StoreAdapterMongo extends StoreAdapterAbstract
{	
	public StoreAdapterMongo()
	{
		super("mongo");
	}

	@Override
	public Object read(Coll<?, ?> coll, Object entity)
	{
		return MongoGsonConverter.gson2MongoObject((JsonElement)StoreAdapterGson.get().read(coll, entity));
	}

	@Override
	public void write(Coll<?, ?> coll, Object raw, Object entity)
	{
		StoreAdapterGson.get().write(coll, MongoGsonConverter.mongo2GsonObject((DBObject) raw), entity);
	}
	
	// -------------------------------------------- //
	// INSTANCE
	// -------------------------------------------- //
	
	protected static StoreAdapterMongo instance = new StoreAdapterMongo();
	public static StoreAdapterMongo get()
	{
		return instance;
	}
}
