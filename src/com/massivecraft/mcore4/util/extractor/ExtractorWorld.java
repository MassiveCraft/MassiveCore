package com.massivecraft.mcore4.util.extractor;

public class ExtractorWorld implements Extractor
{
	@Override
	public Object extract(Object o)
	{
		return ExtractorLogic.worldFromObject(o);
	}
}
