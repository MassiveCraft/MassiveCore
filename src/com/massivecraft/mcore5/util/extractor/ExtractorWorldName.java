package com.massivecraft.mcore5.util.extractor;

public class ExtractorWorldName implements Extractor
{
	@Override
	public Object extract(Object o)
	{
		return ExtractorLogic.worldNameFromObject(o);
	}
	
	// -------------------------------------------- //
	// INSTANCE
	// -------------------------------------------- //
	
	private static ExtractorWorldName i = new ExtractorWorldName();
	public static ExtractorWorldName get() { return i; }
	
}
