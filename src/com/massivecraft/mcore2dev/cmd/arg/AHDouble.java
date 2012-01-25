package com.massivecraft.mcore2dev.cmd.arg;

public class AHDouble extends AHPrimitive<Double>
{	
	@Override
	protected String getPrimitiveName()
	{
		return "double";
	}

	@Override
	protected Double unsafeConvert(String str) throws Exception
	{
		return Double.parseDouble(str);
	}	
}
