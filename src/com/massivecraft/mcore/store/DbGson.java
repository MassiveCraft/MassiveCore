package com.massivecraft.mcore.store;

import java.io.File;

import com.massivecraft.mcore.util.DiscUtil;

public class DbGson extends DbAbstract
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //

	public File dir;
	
	protected DriverGson driver;
	@Override public DriverGson getDriver() { return driver; }
	
	// -------------------------------------------- //
	// CONSTRUCTORS
	// -------------------------------------------- //
	
	public DbGson(DriverGson driver, File folder)
	{
		this.driver = driver;
		this.dir = folder;
	}
	
	// -------------------------------------------- //
	// IMPLEMENTATION
	// -------------------------------------------- //

	@Override
	public String getName()
	{
		return dir.getAbsolutePath();
	}
	
	@Override
	public boolean drop()
	{
		try
		{
			return DiscUtil.deleteRecursive(this.dir);
		}
		catch (Exception e)
		{
			return false;
		}
	}
	
	@Override
	public Object getCollDriverObject(Coll<?> coll)
	{
		return new File(dir, coll.getName());
	}
}
