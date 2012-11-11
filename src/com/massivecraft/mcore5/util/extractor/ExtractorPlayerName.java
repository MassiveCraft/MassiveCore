package com.massivecraft.mcore5.util.extractor;

public class ExtractorPlayerName implements Extractor
{
	@Override
	public Object extract(Object o)
	{
		return ExtractorLogic.playerNameFromObject(o);
	}
}
