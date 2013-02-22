package com.massivecraft.mcore.util.extractor;

public class ExtractorSender implements Extractor
{
	@Override
	public Object extract(Object o)
	{
		return ExtractorLogic.senderFromObject(o);
	}
	
	// -------------------------------------------- //
	// INSTANCE
	// -------------------------------------------- //
	
	private static ExtractorSender i = new ExtractorSender();
	public static ExtractorSender get() { return i; }
	
}
