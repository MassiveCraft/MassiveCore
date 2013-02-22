package com.massivecraft.mcore.util.extractor;

public class ExtractorPlayer implements Extractor
{
	@Override
	public Object extract(Object o)
	{
		return ExtractorLogic.playerFromObject(o);
	}
	
	// -------------------------------------------- //
	// INSTANCE
	// -------------------------------------------- //
	
	private static ExtractorPlayer i = new ExtractorPlayer();
	public static ExtractorPlayer get() { return i; }
	
}
