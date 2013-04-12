package com.massivecraft.mcore.store.idstrategy;

import com.massivecraft.mcore.store.CollInterface;
import com.massivecraft.mcore.store.DbMongo;
import com.massivecraft.mcore.xlib.mongodb.BasicDBObject;
import com.massivecraft.mcore.xlib.mongodb.DBCollection;
import com.massivecraft.mcore.xlib.mongodb.DBObject;

public class IdStrategyAiMongo extends IdStrategyAiAbstract
{
	// -------------------------------------------- //
	// CONST
	// -------------------------------------------- //
	
	public static final String SEC_COLL = "seq";
	public static final String SEC_FIELD = "seq";
	
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	protected static IdStrategyAiMongo i = new IdStrategyAiMongo();
	public static IdStrategyAiMongo get() { return i; }
	private IdStrategyAiMongo() { super(); }	

	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	// http://dev.bubblemix.net/blog/2011/04/auto-increment-for-mongodb-with-the-java-driver/
	
	@Override
	public Integer getNextAndUpdate(CollInterface<?> coll)
	{
		DBCollection dbcoll = this.getSeqColl(coll);
		BasicDBObject res = (BasicDBObject) dbcoll.findAndModify(createQueryObject(coll), new BasicDBObject(), new BasicDBObject(), false, createUpdateObject(), true, true);
		return res.getInt(SEC_FIELD);
	}
	
	@Override
	public Integer getNext(CollInterface<?> coll)
	{
		DBCollection dbcoll = this.getSeqColl(coll);
		BasicDBObject res = (BasicDBObject) dbcoll.findOne(createQueryObject(coll));
		return res.getInt(SEC_FIELD) + 1;
	}
	
	@Override
	public boolean setNext(CollInterface<?> coll, int next)
	{
		throw new RuntimeException("Not implemented yet");
		
		/*File file = this.getAiFile(coll);
		if (this.ensureFileExists(file) == false) return false;
		if (DiscUtil.writeCatch(file, String.valueOf(next)) == false) return false;
		return true;*/
	}
	
	// -------------------------------------------- //
	// UTIL
	// -------------------------------------------- //
	
	public DBCollection getSeqColl(CollInterface<?> coll)
	{
		return ((DbMongo)coll.getDb()).db.getCollection(SEC_COLL);
	}
	
	public static DBObject createQueryObject(CollInterface<?> coll)
	{
		// this object represents your "query", its analogous to a WHERE clause in SQL
		DBObject query = new BasicDBObject();
		query.put("_id", coll.getName()); // where _id = the input sequence name
		return query;
	}
	
	public static DBObject createUpdateObject()
	{
		// this object represents the "update" or the SET blah=blah in SQL
		DBObject change = new BasicDBObject(SEC_FIELD, 1);
		DBObject update = new BasicDBObject("$inc", change); // the $inc here is a mongodb command for increment
		return update;
	}
	
}
