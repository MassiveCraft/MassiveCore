package com.massivecraft.mcore5.cmd.arg;

public class ARBoolean extends ARAbstractPrimitive<Boolean>
{
	@Override
	public String typename()
	{
		return "boolean";
	}

	@Override
	public Boolean convert(String arg) throws Exception
	{
		arg = arg.toLowerCase();
		if (arg.startsWith("y") || arg.startsWith("t") || arg.startsWith("on") || arg.startsWith("+") || arg.startsWith("1"))
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
