package com.massivecraft.mcore5.util.extractor;

public class ExtractorPlayer implements Extractor
{
	@Override
	public Object extract(Object o)
	{
		return ExtractorLogic.playerFromObject(o);
	}
}
