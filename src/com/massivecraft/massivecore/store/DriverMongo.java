package com.massivecraft.massivecore.store;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.massivecraft.massivecore.MassiveCoreMConf;
import com.massivecraft.massivecore.xlib.gson.JsonObject;
import com.massivecraft.massivecore.xlib.mongodb.BasicDBObject;
import com.massivecraft.massivecore.xlib.mongodb.DB;
import com.massivecraft.massivecore.xlib.mongodb.DBCollection;
import com.massivecraft.massivecore.xlib.mongodb.DBCursor;
import com.massivecraft.massivecore.xlib.mongodb.MongoClient;
import com.massivecraft.massivecore.xlib.mongodb.MongoClientURI;

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
	public boolean dropDb(Db db)
	{
		if ( ! (db instanceof DbMongo)) throw new IllegalArgumentException("db");
		DbMongo dbMongo = (DbMongo)db;
		
		try
		{
			dbMongo.db.dropDatabase();
			return true;
		}
		catch (Exception e)
		{
			return false;
		}
	}

	@Override
	public Set<String> getCollnames(Db db)
	{
		Set<String> ret = ((DbMongo)db).db.getCollectionNames();
		ret.remove("system.indexes");
		return ret;
	}
	
	@Override
	public boolean renameColl(Db db, String from, String to)
	{
		if (!this.getCollnames(db).contains(from)) return false;
		if (this.getCollnames(db).contains(to)) return false;
		
		DB mdb = ((DbMongo)db).db;
		mdb.getCollection(from).rename(to);
		
		return true;
	}
	
	@Override
	public boolean containsId(Coll<?> coll, String id)
	{
		DBCollection dbcoll = fixColl(coll);
		DBCursor cursor = dbcoll.find(new BasicDBObject(ID_FIELD, id));
		return cursor.count() != 0;
	}
	
	@Override
	public long getMtime(Coll<?> coll, String id)
	{
		DBCollection dbcoll = fixColl(coll);
		
		BasicDBObject found = (BasicDBObject)dbcoll.findOne(new BasicDBObject(ID_FIELD, id), dboKeysMtime);
		if (found == null) return 0;
		
		// In case there is no _mtime set we assume 1337.
		// NOTE: We can not use 0 since that one is reserved for errors.
		// Probably a manual database addition by the server owner.
		long mtime = found.getLong(MTIME_FIELD, 1337L);
		
		return mtime;
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
			while (cursor.hasNext())
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
			while (cursor.hasNext())
			{
				BasicDBObject raw = (BasicDBObject)cursor.next();
				Object remoteId = raw.get(ID_FIELD);
				
				// In case there is no _mtime set we assume 1337.
				// NOTE: We can not use 0 since that one is reserved for errors.
				// Probably a manual database addition by the server owner.
				long mtime = raw.getLong(MTIME_FIELD, 1337L);
				
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
	public Entry<JsonObject, Long> load(Coll<?> coll, String id)
	{
		DBCollection dbcoll = fixColl(coll);
		BasicDBObject raw = (BasicDBObject)dbcoll.findOne(new BasicDBObject(ID_FIELD, id));
		return loadRaw(raw);
	}
	
	public Entry<JsonObject, Long> loadRaw(BasicDBObject raw)
	{
		if (raw == null) return new SimpleEntry<JsonObject, Long>(null, 0L);
		
		// Throw away the id field
		raw.removeField(ID_FIELD);
		
		// In case there is no _mtime set we assume 1337.
		// NOTE: We can not use 0 since that one is reserved for errors.
		// Probably a manual database addition by the server owner.
		long mtime = 1337L;
		Object mtimeObject = raw.removeField(MTIME_FIELD);
		if (mtimeObject != null)
		{
			mtime = ((Number)mtimeObject).longValue();
		}
		
		// Convert MongoDB --> GSON
		JsonObject element = GsonMongoConverter.mongo2GsonObject(raw);
		
		return new SimpleEntry<JsonObject, Long>(element, mtime);
	}
	
	@Override
	public Map<String, Entry<JsonObject, Long>> loadAll(Coll<?> coll)
	{
		// Declare Ret
		Map<String, Entry<JsonObject, Long>> ret = null;
		
		// Fix Coll
		DBCollection dbcoll = fixColl(coll);
		
		// Find All
		DBCursor cursor = dbcoll.find();
		
		try
		{
			// Create Ret
			ret = new LinkedHashMap<String, Entry<JsonObject, Long>>(cursor.count());
			
			// For Each Found
			while (cursor.hasNext())
			{
				BasicDBObject raw = (BasicDBObject)cursor.next();
				
				// Get ID
				Object rawId = raw.removeField(ID_FIELD);
				if (rawId == null) continue;
				String id = rawId.toString();
				
				// Get Entry
				Entry<JsonObject, Long> entry = loadRaw(raw);
				// NOTE: The entry can be a failed one with null and 0.
				// NOTE: We add it anyways since it's an informative failure.
				// NOTE: This is supported by our defined specification.
				
				// Add
				ret.put(id, entry);
			}
			
		}
		finally
		{
			cursor.close();
		}
		
		// Return Ret
		return ret;
	}

	@Override
	public long save(Coll<?> coll, String id, JsonObject data)
	{		
		DBCollection dbcoll = fixColl(coll);
		
		BasicDBObject dbo = new BasicDBObject();
		
		long mtime = System.currentTimeMillis();
		dbo.put(ID_FIELD, id);
		dbo.put(MTIME_FIELD, mtime);
		
		GsonMongoConverter.gson2MongoObject(data, dbo);
		
		dbcoll.save(dbo, MassiveCoreMConf.get().getMongoDbWriteConcernSave());

		return mtime;
	}

	@Override
	public void delete(Coll<?> coll, String id)
	{
		DBCollection dbcoll = fixColl(coll);
		dbcoll.remove(new BasicDBObject(ID_FIELD, id), MassiveCoreMConf.get().getMongoDbWriteConcernDelete());
	}

	@Override
	public boolean supportsPusher()
	{
		return false;
	}
	
	@Override
	public PusherColl getPusher(Coll<?> coll)
	{
		throw new UnsupportedOperationException("MongoDB does not have a pusher change.");
	}
	
	
	//----------------------------------------------//
	// UTIL
	//----------------------------------------------//
	
	protected static DBCollection fixColl(Coll<?> coll)
	{
		return (DBCollection) coll.getCollDriverObject();
	}
	
	@SuppressWarnings("deprecation")
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
