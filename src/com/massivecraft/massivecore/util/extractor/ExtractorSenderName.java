package com.massivecraft.massivecore.util.extractor;

public class ExtractorSenderName implements Extractor
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static ExtractorSenderName i = new ExtractorSenderName();
	public static ExtractorSenderName get() { return i; }
	
	// -------------------------------------------- //
	// OVERRIDE: EXTRACTOR
	// -------------------------------------------- //
	
	@Override
	public Object extract(Object o)
	{
		return ExtractorLogic.senderNameFromObject(o);
	}

}
