package com.massivecraft.massivecore.store;

import java.io.File;

import com.massivecraft.massivecore.util.DiscUtil;

public class DbFlatfile extends DbAbstract
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //

	public File dir;
	
	protected DriverFlatfile driver;
	@Override public DriverFlatfile getDriver() { return driver; }
	
	// -------------------------------------------- //
	// CONSTRUCTORS
	// -------------------------------------------- //
	
	public DbFlatfile(DriverFlatfile driver, File folder)
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
