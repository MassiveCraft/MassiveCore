package com.massivecraft.mcore.store;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import com.massivecraft.mcore.ConfServer;

public class MStore
{
	// -------------------------------------------- //
	// DRIVER REGISTRY
	// -------------------------------------------- //
	
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
	
	static
	{
		registerDriver(DriverMongo.get());
		registerDriver(DriverGson.get());
	}
	
	// -------------------------------------------- //
	// FROODLSCHTEIN
	// -------------------------------------------- //
	
	// We cache databases here
	private static Map<String, Db<?>> uri2db = new HashMap<String, Db<?>>();
	
	public static String resolveAlias(String alias)
	{
		String uri = ConfServer.alias2uri.get(alias);
		if (uri == null) return alias;
		return resolveAlias(uri);
	}
	
	public static Db<?> getDb(String alias)
	{
		String uri = resolveAlias(alias);
		Db<?> ret = uri2db.get(uri);
		if (ret != null) return ret;
		
		try
		{
			ret = getDb(new URI(uri));
		}
		catch (URISyntaxException e)
		{
			e.printStackTrace();
			return null;
		}
		
		return ret;
	}
	
	public static Db<?> getDb(URI uri)
	{
		String scheme = uri.getScheme();
		Driver<?> driver = getDriver(scheme);
		if (driver == null) return null;
		return driver.getDb(uri.toString());
	}
	
	
}