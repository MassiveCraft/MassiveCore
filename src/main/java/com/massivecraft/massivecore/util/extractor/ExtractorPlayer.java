package com.massivecraft.massivecore.util.extractor;

public class ExtractorPlayer implements Extractor
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static ExtractorPlayer i = new ExtractorPlayer();
	public static ExtractorPlayer get() { return i; }
	
	// -------------------------------------------- //
	// OVERRIDE: EXTRACTOR
	// -------------------------------------------- //
	
	@Override
	public Object extract(Object o)
	{
		return ExtractorLogic.playerFromObject(o);
	}
	
}
