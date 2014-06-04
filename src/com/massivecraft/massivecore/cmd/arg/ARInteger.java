package com.massivecraft.massivecore.cmd.arg;

public class ARInteger extends ARAbstractPrimitive<Integer>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static ARInteger i = new ARInteger();
	public static ARInteger get() { return i; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public String typename()
	{
		return "integer";
	}

	@Override
	public Integer convert(String arg) throws Exception
	{
		return Integer.parseInt(arg);
	}

}