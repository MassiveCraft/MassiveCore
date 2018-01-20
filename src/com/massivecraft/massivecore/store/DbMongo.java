package com.massivecraft.massivecore.store;

import com.massivecraft.massivecore.xlib.mongodb.DB;

public class DbMongo extends DbAbstract
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //

	public final DB db;
	
	private final DriverMongo driver;
	@Override
	public DriverMongo getDriver()
	{
		return driver;
	}
	
	// -------------------------------------------- //
	// CONSTRUCTORS
	// -------------------------------------------- //
	
	public DbMongo(DriverMongo driver, DB db)
	{
		this.driver = driver;
		this.db = db;
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //

	@Override
	public String getDbName()
	{
		return db.getName();
	}
	
	@Override
	public Object createCollDriverObject(Coll<?> coll)
	{
		return db.getCollection(coll.getName());
	}
	
}
