package com.massivecraft.mcore4.cmd.arg;

public class AHFloat extends AHPrimitive<Float>
{	
	@Override
	protected String getPrimitiveName()
	{
		return "integer";
	}

	@Override
	protected Float unsafeConvert(String str) throws Exception
	{
		return Float.parseFloat(str);
	}	
}
