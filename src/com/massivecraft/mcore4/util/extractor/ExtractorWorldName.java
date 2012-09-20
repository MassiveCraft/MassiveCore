package com.massivecraft.mcore4.util.extractor;

public class ExtractorWorldName implements Extractor
{
	@Override
	public Object extract(Object o)
	{
		return ExtractorLogic.worldNameFromObject(o);
	}
}
