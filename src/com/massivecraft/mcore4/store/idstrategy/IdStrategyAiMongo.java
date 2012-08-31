package com.massivecraft.mcore4.store.idstrategy;

import com.massivecraft.mcore4.store.CollInterface;
import com.massivecraft.mcore4.store.DbMongo;
import com.massivecraft.mcore4.xlib.mongodb.BasicDBObject;
import com.massivecraft.mcore4.xlib.mongodb.DBCollection;
import com.massivecraft.mcore4.xlib.mongodb.DBObject;

public class IdStrategyAiMongo extends IdStrategyAbstract<String, String>
{
	private static String SEC = "seq";

	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	// String sequenseName;
	// DBCollection sequenseCollection;
	String sequenseField;
	
	//----------------------------------------------//
	// CONSTRUCTORS
	//----------------------------------------------//
	
	private IdStrategyAiMongo()
	{
		super("ai", String.class, String.class);
		this.sequenseField = SEC;
	}

	// -------------------------------------------- //
	// IMPLEMENTATION
	// -------------------------------------------- //
	
	@Override public String localToRemote(Object local) { return (String)local; }
	@Override public String remoteToLocal(Object remote) { return (String)remote; }
	
	// http://dev.bubblemix.net/blog/2011/04/auto-increment-for-mongodb-with-the-java-driver/
	@Override
	public String generateAttempt(CollInterface<?, String> coll)
	{
		String sequenseName = coll.name();
		DBCollection sequenseCollection = ((DbMongo)coll.db()).db.getCollection(SEC);
		
		// this object represents your "query", its analogous to a WHERE clause in SQL
		DBObject query = new BasicDBObject();
	    query.put("_id", sequenseName); // where _id = the input sequence name
	 
	    // this object represents the "update" or the SET blah=blah in SQL
	    DBObject change = new BasicDBObject(this.sequenseField, 1);
	    DBObject update = new BasicDBObject("$inc", change); // the $inc here is a mongodb command for increment
	 
	    // Atomically updates the sequence field and returns the value for you
	    DBObject res = sequenseCollection.findAndModify(query, new BasicDBObject(), new BasicDBObject(), false, update, true, true);
	    return res.get(this.sequenseField).toString();
	}
	
	// -------------------------------------------- //
	// INSTANCE
	// -------------------------------------------- //
	
	protected static IdStrategyAiMongo instance = new IdStrategyAiMongo();
	public static IdStrategyAiMongo get()
	{
		return instance;
	}
}
