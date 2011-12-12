package com.massivecraft.mcore1.cmd;

public class AHInteger extends AHPrimitive<Integer>
{	
	@Override
	protected String getPrimitiveName()
	{
		return "integer";
	}

	@Override
	protected Integer unsafeConvert(String str) throws Exception
	{
		return Integer.parseInt(str);
	}	
}