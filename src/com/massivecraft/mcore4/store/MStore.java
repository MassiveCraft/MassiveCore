package com.massivecraft.mcore4.store;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import com.massivecraft.mcore4.MCore;


public class MStore
{
	protected static Map<String, Driver<?>> drivers = new HashMap<String, Driver<?>>();
	public static boolean registerDriver(Driver<?> driver)
	{
		if (drivers.containsKey(driver.getName())) return false;
		drivers.put(driver.getName(), driver);
		return true;
	}
	public static Driver<?> getDriver(String id)
	{
		
		return drivers.get(id);
	}
	
	public static Db<?> getDb(String uri)
	{
		try
		{
			if (uri.equalsIgnoreCase("default")) return MCore.getDb();
			return getDb(new URI(uri));
		}
		catch (URISyntaxException e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	public static Db<?> getDb(URI uri)
	{
		String scheme = uri.getScheme();
		Driver<?> driver = getDriver(scheme);
		if (driver == null) return null;
		return driver.getDb(uri.toString());
	}
	
	static
	{
		registerDriver(DriverMongo.get());
		registerDriver(DriverGson.get());
	}
}