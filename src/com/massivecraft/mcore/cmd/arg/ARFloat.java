package com.massivecraft.mcore.cmd.arg;

public class ARFloat extends ARAbstractPrimitive<Float>
{	
	@Override
	public String typename()
	{
		return "integer";
	}

	@Override
	public Float convert(String arg) throws Exception
	{
		return Float.parseFloat(arg);
	}
	
	// -------------------------------------------- //
	// INSTANCE
	// -------------------------------------------- //
	
	private static ARFloat i = new ARFloat();
	public static ARFloat get() { return i; }
	
}
