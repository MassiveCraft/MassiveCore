package com.massivecraft.massivecore.store;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.massivecraft.massivecore.ConfServer;
import com.massivecraft.massivecore.xlib.gson.JsonElement;

public class MStore
{
	// -------------------------------------------- //
	// DRIVER REGISTRY
	// -------------------------------------------- //
	
	private static Map<String, Driver> drivers = new HashMap<String, Driver>();
	public static boolean registerDriver(Driver driver)
	{
		if (drivers.containsKey(driver.getName())) return false;
		drivers.put(driver.getName(), driver);
		return true;
	}

	public static Driver getDriver(String id)
	{
		return drivers.get(id);
	}
	
	static
	{
		registerDriver(DriverMongo.get());
		registerDriver(DriverFlatfile.get());
	}
	
	// -------------------------------------------- //
	// ID CREATION
	// -------------------------------------------- //
	
	public static String createId()
	{
		return UUID.randomUUID().toString();
	}
	
	// -------------------------------------------- //
	// JSON ELEMENT EQUAL
	// -------------------------------------------- //
	
	public static boolean equal(JsonElement one, JsonElement two)
	{
		if (one == null) return two == null;
		if (two == null) return one == null;
		
		return GsonEqualsChecker.equals(one, two);
	}
	
	// -------------------------------------------- //
	// FROODLSCHTEIN
	// -------------------------------------------- //
	
	// We cache databases here
	private static Map<String, Db> uri2db = new HashMap<String, Db>();
	
	public static String resolveAlias(String alias)
	{
		String uri = ConfServer.alias2uri.get(alias);
		if (uri == null) return alias;
		return resolveAlias(uri);
	}
	
	public static Db getDb(String alias)
	{
		String uri = resolveAlias(alias);
		Db db = uri2db.get(uri);
		if (db != null) return db;
		
		try
		{
			db = getDb(new URI(uri));
		}
		catch (URISyntaxException e)
		{
			e.printStackTrace();
			return null;
		}
		
		uri2db.put(uri, db);
		
		return db;
	}
	
	public static Db getDb()
	{
		return getDb(ConfServer.dburi);
	}
	
	public static Db getDb(URI uri)
	{
		String scheme = uri.getScheme();
		Driver driver = getDriver(scheme);
		if (driver == null) return null;
		return driver.getDb(uri.toString());
	}
	
	
}