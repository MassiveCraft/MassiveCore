package com.massivecraft.core.persist.gson;

import java.io.File;
import java.io.FileFilter;

public class JsonFileFilter implements FileFilter
{
	private static JsonFileFilter instance = null;
	private final static String DOTJSON = ".json";
	public static JsonFileFilter getInstance()
	{
		if (instance == null)
		{
			instance = new JsonFileFilter();
		}
		return instance;
	}
	
	@Override
	public boolean accept(File pathname)
	{
		return pathname.getName().toLowerCase().endsWith(DOTJSON);
	}
	
	private JsonFileFilter() {}
}
