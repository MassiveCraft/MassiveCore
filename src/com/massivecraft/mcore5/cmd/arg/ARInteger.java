package com.massivecraft.mcore5.cmd.arg;

public class ARInteger extends ARAbstractPrimitive<Integer>
{
	@Override
	public String typename()
	{
		return "integer";
	}

	@Override
	public Integer convert(String str) throws Exception
	{
		return Integer.parseInt(str);
	}
	
	// -------------------------------------------- //
	// INSTANCE
	// -------------------------------------------- //
	
	private static ARInteger i = new ARInteger();
	public static ARInteger get() { return i; }
	
}