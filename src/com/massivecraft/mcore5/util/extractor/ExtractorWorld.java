package com.massivecraft.mcore5.util.extractor;

public class ExtractorWorld implements Extractor
{
	@Override
	public Object extract(Object o)
	{
		return ExtractorLogic.worldFromObject(o);
	}
	
	// -------------------------------------------- //
	// INSTANCE
	// -------------------------------------------- //
	
	private static ExtractorWorld i = new ExtractorWorld();
	public static ExtractorWorld get() { return i; }
	
}
