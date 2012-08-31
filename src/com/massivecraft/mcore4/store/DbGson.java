package com.massivecraft.mcore4.store;

import java.io.File;

import com.massivecraft.mcore4.util.DiscUtil;
import com.massivecraft.mcore4.xlib.gson.JsonElement;

public class DbGson extends DbAbstract<JsonElement>
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //

	public File dir;
	
	protected DriverGson driver;
	@Override public DriverGson driver() { return driver; }
	
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
	public String name()
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
	public Object getCollDriverObject(Coll<?, ?> coll)
	{
		return new File(dir, coll.name());
	}
}
