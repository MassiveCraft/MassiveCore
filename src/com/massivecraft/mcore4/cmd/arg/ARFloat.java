package com.massivecraft.mcore4.cmd.arg;

public class ARFloat extends ARAbstractPrimitive<Float>
{	
	@Override
	public String typename()
	{
		return "integer";
	}

	@Override
	public Float convert(String str) throws Exception
	{
		return Float.parseFloat(str);
	}
	
	// -------------------------------------------- //
	// INSTANCE
	// -------------------------------------------- //
	
	private static ARFloat i = new ARFloat();
	public static ARFloat get() { return i; }
	
}
