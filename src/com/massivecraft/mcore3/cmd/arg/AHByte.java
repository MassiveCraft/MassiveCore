package com.massivecraft.mcore3.cmd.arg;

public class AHByte extends AHPrimitive<Byte>
{	
	@Override
	protected String getPrimitiveName()
	{
		return "byte";
	}

	@Override
	protected Byte unsafeConvert(String str) throws Exception
	{
		return Byte.parseByte(str);
	}	
}
