package com.massivecraft.mcore.cmd.arg;

public class ARInteger extends ARAbstractPrimitive<Integer>
{
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
	
	// -------------------------------------------- //
	// INSTANCE
	// -------------------------------------------- //
	
	private static ARInteger i = new ARInteger();
	public static ARInteger get() { return i; }
	
}