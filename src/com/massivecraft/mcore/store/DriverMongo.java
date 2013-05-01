package com.massivecraft.mcore.store;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.AbstractMap.SimpleEntry;
import java.util.Map.Entry;

import com.massivecraft.mcore.xlib.gson.JsonElement;
import com.massivecraft.mcore.xlib.mongodb.BasicDBObject;
import com.massivecraft.mcore.xlib.mongodb.DB;
import com.massivecraft.mcore.xlib.mongodb.DBCollection;
import com.massivecraft.mcore.xlib.mongodb.DBCursor;
import com.massivecraft.mcore.xlib.mongodb.MongoClient;
import com.massivecraft.mcore.xlib.mongodb.MongoClientURI;

public class DriverMongo extends DriverAbstract
{
	// -------------------------------------------- //
	// CONSTANTS
	// -------------------------------------------- //
	
	public final static String ID_FIELD = "_id";
	public final static String MTIME_FIELD = "_mtime";
	
	public final static BasicDBObject dboEmpty = new BasicDBObject();
	public final static BasicDBObject dboKeysId = new BasicDBObject().append(ID_FIELD, 1);
	public final static BasicDBObject dboKeysMtime = new BasicDBObject().append(MTIME_FIELD, 1);
	public final static BasicDBObject dboKeysIdandMtime = new BasicDBObject().append(ID_FIELD, 1).append(MTIME_FIELD, 1);
	
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	protected static DriverMongo i = new DriverMongo();
	public static DriverMongo get() { return i; }
	private DriverMongo() { super("mongodb"); }
	
	// -------------------------------------------- //
	// IMPLEMENTATION
	// -------------------------------------------- //
	
	@Override
	public Db getDb(String uri)
	{
		DB db = this.getDbInner(uri);
		return new DbMongo(this, db);
	}

	@Override
	public Set<String> getCollnames(Db db)
	{
		return ((DbMongo)db).db.getCollectionNames();
	}
	
	@Override
	public boolean containsId(Coll<?> coll, String id)
	{
		DBCollection dbcoll = fixColl(coll);
		DBCursor cursor = dbcoll.find(new BasicDBObject(ID_FIELD, id));
		return cursor.count() != 0;
	}
	
	@Override
	public Long getMtime(Coll<?> coll, String id)
	{
		DBCollection dbcoll = fixColl(coll);
		BasicDBObject found = (BasicDBObject)dbcoll.findOne(new BasicDBObject(ID_FIELD, id), dboKeysMtime);
		if (found == null) return null;
		if ( ! found.containsField(MTIME_FIELD)) return null; // This should not happen! But better to ignore than crash?
		return found.getLong(MTIME_FIELD);
	}
	
	@Override
	public Collection<String> getIds(Coll<?> coll)
	{
		List<String> ret = null;
		
		DBCollection dbcoll = fixColl(coll);
		
		DBCursor cursor = dbcoll.find(dboEmpty, dboKeysId);
		try
		{
			ret = new ArrayList<String>(cursor.count());
			while(cursor.hasNext())
			{
				Object remoteId = cursor.next().get(ID_FIELD);
				ret.add(remoteId.toString());
			}
		}
		finally
		{
			cursor.close();
		}
		
		return ret;
	}
	
	@Override
	public Map<String, Long> getId2mtime(Coll<?> coll)
	{
		Map<String, Long> ret = null;
		
		DBCollection dbcoll = fixColl(coll);
		
		DBCursor cursor = dbcoll.find(dboEmpty, dboKeysIdandMtime);
		try
		{
			ret = new HashMap<String, Long>(cursor.count());
			while(cursor.hasNext())
			{
				BasicDBObject raw = (BasicDBObject)cursor.next();
				Object remoteId = raw.get(ID_FIELD);
				if ( ! raw.containsField(MTIME_FIELD)) continue; // This should not happen! But better to ignore than crash?
				Long mtime = raw.getLong(MTIME_FIELD);
				ret.put(remoteId.toString(), mtime);
			}
		}
		finally
		{
			cursor.close();
		}
		
		return ret;
	}

	@Override
	public Entry<JsonElement, Long> load(Coll<?> coll, String id)
	{
		DBCollection dbcoll = fixColl(coll);
		BasicDBObject raw = (BasicDBObject)dbcoll.findOne(new BasicDBObject(ID_FIELD, id));
		if (raw == null) return null;
		
		raw.removeField(ID_FIELD);
		Long mtime = (Long) raw.removeField(MTIME_FIELD);
		
		JsonElement element = MongoGsonConverter.mongo2GsonObject(raw);
		
		return new SimpleEntry<JsonElement, Long>(element, mtime);
	}

	@Override
	public Long save(Coll<?> coll, String id, JsonElement data)
	{		
		DBCollection dbcoll = fixColl(coll);
		
		BasicDBObject dbo = MongoGsonConverter.gson2MongoObject(data);
		Long mtime = System.currentTimeMillis();
		dbo.put(MTIME_FIELD, mtime);
		dbo.put(ID_FIELD, id);
		
		dbcoll.save(dbo);

		return mtime;
	}

	@Override
	public void delete(Coll<?> coll, String id)
	{
		fixColl(coll).remove(new BasicDBObject(ID_FIELD, id));
	}

	//----------------------------------------------//
	// UTIL
	//----------------------------------------------//
	
	protected static DBCollection fixColl(Coll<?> coll)
	{
		return (DBCollection) coll.getCollDriverObject();
	}
	
	protected DB getDbInner(String uri)
	{
		MongoClientURI muri = new MongoClientURI(uri);
		
		try
		{
			// TODO: Create one of these per collection? Really? Perhaps I should cache.
			MongoClient mongoClient = new MongoClient(muri);
			
			DB db = mongoClient.getDB(muri.getDatabase());
			
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

}
