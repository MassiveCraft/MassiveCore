package com.massivecraft.mcore4.store;

import java.io.File;
import java.io.FileFilter;

public class JsonFileFilter implements FileFilter
{
	private final static String DOTJSON = ".json";
	
	@Override
	public boolean accept(File pathname)
	{
		return pathname.getName().toLowerCase().endsWith(DOTJSON);
	}	
	
	// -------------------------------------------- //
	// INSTANCE
	// -------------------------------------------- //
	
	private static JsonFileFilter instance = new JsonFileFilter();
	public static JsonFileFilter get() { return instance; }
	
}
