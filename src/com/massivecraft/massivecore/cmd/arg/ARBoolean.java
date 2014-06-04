package com.massivecraft.massivecore.cmd.arg;

public class ARBoolean extends ARAbstractPrimitive<Boolean>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static ARBoolean i = new ARBoolean();
	public static ARBoolean get() { return i; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
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
	
}
