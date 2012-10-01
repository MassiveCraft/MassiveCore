package com.massivecraft.mcore4.store;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.AbstractMap.SimpleEntry;
import java.util.Map.Entry;

import com.massivecraft.mcore4.store.idstrategy.IdStrategyAiMongo;
import com.massivecraft.mcore4.store.idstrategy.IdStrategyOidMongo;
import com.massivecraft.mcore4.store.idstrategy.IdStrategyUuidMongoAndGson;
import com.massivecraft.mcore4.store.storeadapter.StoreAdapter;
import com.massivecraft.mcore4.store.storeadapter.StoreAdapterMongo;
import com.massivecraft.mcore4.xlib.mongodb.BasicDBObject;
import com.massivecraft.mcore4.xlib.mongodb.DB;
import com.massivecraft.mcore4.xlib.mongodb.DBCollection;
import com.massivecraft.mcore4.xlib.mongodb.DBCursor;
import com.massivecraft.mcore4.xlib.mongodb.MongoURI;

public class DriverMongo extends DriverAbstract<BasicDBObject>
{
	// -------------------------------------------- //
	// STATIC
	// -------------------------------------------- //
	
	public final static String ID_FIELD = "_id";
	public final static String MTIME_FIELD = "_mtime";
	
	public final static BasicDBObject dboEmpty = new BasicDBObject();
	public final static BasicDBObject dboKeysId = new BasicDBObject().append(ID_FIELD, 1);
	public final static BasicDBObject dboKeysMtime = new BasicDBObject().append(MTIME_FIELD, 1);
	public final static BasicDBObject dboKeysIdandMtime = new BasicDBObject().append(ID_FIELD, 1).append(MTIME_FIELD, 1);
	
	// -------------------------------------------- //
	// IMPLEMENTATION
	// -------------------------------------------- //
	
	@Override public Class<BasicDBObject> getRawdataClass() { return BasicDBObject.class; }
	
	@Override
	public StoreAdapter getStoreAdapter()
	{
		return StoreAdapterMongo.get();
	}
	
	@Override
	public Db<BasicDBObject> getDb(String uri)
	{
		DB db = this.getDbInner(uri);
		return new DbMongo(this, db);
	}

	@Override
	public Set<String> getCollnames(Db<?> db)
	{
		return ((DbMongo)db).db.getCollectionNames();
	}
	
	@Override
	public <L> boolean containsId(Coll<?, L> coll, L id)
	{
		DBCollection dbcoll = fixColl(coll);
		DBCursor cursor = dbcoll.find(new BasicDBObject(ID_FIELD, coll.getIdStrategy().localToRemote(id)));
		return cursor.count() != 0;
	}
	
	@Override
	public <L> Long getMtime(Coll<?, L> coll, L id)
	{
		DBCollection dbcoll = fixColl(coll);
		BasicDBObject found = (BasicDBObject)dbcoll.findOne(new BasicDBObject(ID_FIELD, coll.getIdStrategy().localToRemote(id)), dboKeysMtime);
		if (found == null) return null;
		if ( ! found.containsField(MTIME_FIELD)) return null; // This should not happen! But better to ignore than crash?
		return found.getLong(MTIME_FIELD);
	}
	
	@Override
	public <L> Collection<L> getIds(Coll<?, L> coll)
	{
		List<L> ret = null;
		
		DBCollection dbcoll = fixColl(coll);
		
		DBCursor cursor = dbcoll.find(dboEmpty, dboKeysId);
		try
		{
			ret = new ArrayList<L>(cursor.count());
			while(cursor.hasNext())
			{
				Object remoteId = cursor.next().get(ID_FIELD);
				L localId = coll.getIdStrategy().remoteToLocal(remoteId);
				ret.add(localId);
			}
		}
		finally
		{
			cursor.close();
		}
		
		return ret;
	}
	
	@Override
	public <L> Map<L, Long> getId2mtime(Coll<?, L> coll)
	{
		Map<L, Long> ret = null;
		
		DBCollection dbcoll = fixColl(coll);
		
		DBCursor cursor = dbcoll.find(dboEmpty, dboKeysIdandMtime);
		try
		{
			ret = new HashMap<L, Long>(cursor.count());
			while(cursor.hasNext())
			{
				BasicDBObject raw = (BasicDBObject)cursor.next();
				Object remoteId = raw.get(ID_FIELD);
				L localId = coll.getIdStrategy().remoteToLocal(remoteId);
				if ( ! raw.containsField(MTIME_FIELD)) continue; // This should not happen! But better to ignore than crash?
				Long mtime = raw.getLong(MTIME_FIELD);
				ret.put(localId, mtime);
			}
		}
		finally
		{
			cursor.close();
		}
		
		return ret;
	}

	@Override
	public <L> Entry<BasicDBObject, Long> load(Coll<?, L> coll, L id)
	{
		DBCollection dbcoll = fixColl(coll);
		BasicDBObject raw = (BasicDBObject)dbcoll.findOne(new BasicDBObject(ID_FIELD, coll.getIdStrategy().localToRemote(id)));
		if (raw == null) return null;
		Long mtime = (Long) raw.removeField(MTIME_FIELD);
		return new SimpleEntry<BasicDBObject, Long>(raw, mtime);
	}

	@Override
	public <L> Long save(Coll<?, L> coll, L id, Object rawData)
	{		
		DBCollection dbcoll = fixColl(coll);
		
		// We shallow copy here in order to stop the extra "_mtime" field from messing up the lastRaw.
		BasicDBObject data = (BasicDBObject)rawData;
		data = (BasicDBObject)data.clone();
		Long mtime = System.currentTimeMillis();
		data.put(MTIME_FIELD, mtime);
		
		Object remoteId = coll.getIdStrategy().localToRemote(id);		
		dbcoll.update(new BasicDBObject(ID_FIELD, remoteId), data, true, false);

		return mtime;
	}

	@Override
	public <L> void delete(Coll<?, L> coll, L id)
	{
		fixColl(coll).remove(new BasicDBObject(ID_FIELD, coll.getIdStrategy().localToRemote(id)));
	}

	//----------------------------------------------//
	// UTIL
	//----------------------------------------------//
	
	protected static DBCollection fixColl(Coll<?, ?> coll)
	{
		return (DBCollection) coll.getCollDriverObject();
	}
	
	protected DB getDbInner(String uri)
	{
		MongoURI muri = new MongoURI(uri);
		
		try
		{
			DB db = muri.connectDB();
			
			if (muri.getUsername() == null) return db;
			
			if ( ! db.authenticate(muri.getUsername(), muri.getPassword()))
			{
				//log(Level.SEVERE, "... db authentication failed.");
				return null;
			}
			return db;
		}
		catch (Exception e)
		{
			//log(Level.SEVERE, "... mongo connection failed.");
			e.printStackTrace();
			return null;
		}
	}
	
	//----------------------------------------------//
	// CONSTRUCTORS
	//----------------------------------------------//
	
	private DriverMongo()
	{
		super("mongodb");
	}
	
	// -------------------------------------------- //
	// INSTANCE
	// -------------------------------------------- //
	
	protected static DriverMongo instance;
	public static DriverMongo get()
	{
		return instance;
	}
	
	static
	{
		instance = new DriverMongo();
		instance.registerIdStrategy(IdStrategyAiMongo.get());
		instance.registerIdStrategy(IdStrategyOidMongo.get());
		instance.registerIdStrategy(IdStrategyUuidMongoAndGson.get());
	}
}
