package com.massivecraft.mcore5.cmd.arg;

public class ARBoolean extends ARAbstractPrimitive<Boolean>
{
	@Override
	public String typename()
	{
		return "boolean";
	}

	@Override
	public Boolean convert(String str) throws Exception
	{
		str = str.toLowerCase();
		if (str.startsWith("y") || str.startsWith("t") || str.startsWith("on") || str.startsWith("+") || str.startsWith("1"))
		{
			return true;
		}
		return false;
	}	
	
	// -------------------------------------------- //
	// INSTANCE
	// -------------------------------------------- //
	
	private static ARBoolean i = new ARBoolean();
	public static ARBoolean get() { return i; }
	
}
