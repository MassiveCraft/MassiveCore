package com.massivecraft.mcore5.util.extractor;

public class ExtractorSenderId implements Extractor
{
	@Override
	public Object extract(Object o)
	{
		return ExtractorLogic.senderIdFromObject(o);
	}
	
	// -------------------------------------------- //
	// INSTANCE
	// -------------------------------------------- //
	
	private static ExtractorSenderId i = new ExtractorSenderId();
	public static ExtractorSenderId get() { return i; }
	
}
