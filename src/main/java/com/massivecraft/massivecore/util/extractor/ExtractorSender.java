package com.massivecraft.massivecore.util.extractor;

public class ExtractorSender implements Extractor
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static ExtractorSender i = new ExtractorSender();
	public static ExtractorSender get() { return i; }
	
	// -------------------------------------------- //
	// OVERRIDE: EXTRACTOR
	// -------------------------------------------- //
	
	@Override
	public Object extract(Object o)
	{
		return ExtractorLogic.senderFromObject(o);
	}
	
}
