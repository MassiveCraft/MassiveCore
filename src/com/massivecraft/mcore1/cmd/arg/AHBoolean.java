package com.massivecraft.mcore1.cmd.arg;

public class AHBoolean extends AHPrimitive<Boolean>
{	
	@Override
	protected String getPrimitiveName()
	{
		return "boolean";
	}

	@Override
	protected Boolean unsafeConvert(String str) throws Exception
	{
		str = str.toLowerCase();
		if (str.startsWith("y") || str.startsWith("t") || str.startsWith("on") || str.startsWith("+") || str.startsWith("1"))
		{
			return true;
		}
		return false;
	}	
}
