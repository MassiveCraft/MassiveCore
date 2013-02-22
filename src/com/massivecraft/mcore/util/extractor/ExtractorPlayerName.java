package com.massivecraft.mcore.util.extractor;

public class ExtractorPlayerName implements Extractor
{
	@Override
	public Object extract(Object o)
	{
		return ExtractorLogic.playerNameFromObject(o);
	}
	
	// -------------------------------------------- //
	// INSTANCE
	// -------------------------------------------- //
	
	private static ExtractorPlayerName i = new ExtractorPlayerName();
	public static ExtractorPlayerName get() { return i; }
	
}
