package com.massivecraft.mcore.util.extractor;

public class ExtractorPlayerName implements Extractor
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static ExtractorPlayerName i = new ExtractorPlayerName();
	public static ExtractorPlayerName get() { return i; }
	
	// -------------------------------------------- //
	// OVERRIDE: EXTRACTOR
	// -------------------------------------------- //
	
	@Override
	public Object extract(Object o)
	{
		return ExtractorLogic.playerNameFromObject(o);
	}
	
}
