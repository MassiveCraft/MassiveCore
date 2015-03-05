package com.massivecraft.massivecore.store;

import java.io.File;

public class DbFlatfile extends DbAbstract
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //

	public File directory;
	
	protected DriverFlatfile driver;
	@Override public DriverFlatfile getDriver() { return driver; }
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public DbFlatfile(DriverFlatfile driver, File directory)
	{
		this.driver = driver;
		this.directory = directory;
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //

	@Override
	public String getDbName()
	{
		return directory.getAbsolutePath();
	}
	
	@Override
	public Object createCollDriverObject(Coll<?> coll)
	{
		return new File(directory, coll.getName());
	}
	
}
