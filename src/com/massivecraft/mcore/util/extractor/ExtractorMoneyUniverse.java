package com.massivecraft.mcore.util.extractor;

public class ExtractorMoneyUniverse implements Extractor
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static ExtractorMoneyUniverse i = new ExtractorMoneyUniverse();
	public static ExtractorMoneyUniverse get() { return i; }
	
	// -------------------------------------------- //
	// OVERRIDE: EXTRACTOR
	// -------------------------------------------- //
	
	@Override
	public Object extract(Object o)
	{
		return ExtractorLogic.moneyUniverseFromObject(o);
	}
	
}
