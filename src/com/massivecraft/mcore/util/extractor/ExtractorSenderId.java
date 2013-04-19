package com.massivecraft.mcore.util.extractor;

public class ExtractorSenderId implements Extractor
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static ExtractorSenderId i = new ExtractorSenderId();
	public static ExtractorSenderId get() { return i; }
	
	// -------------------------------------------- //
	// OVERRIDE: EXTRACTOR
	// -------------------------------------------- //
	
	@Override
	public Object extract(Object o)
	{
		return ExtractorLogic.senderIdFromObject(o);
	}

}
