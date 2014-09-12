package com.massivecraft.massivecore.cmd.arg;

public class ARFloat extends ARAbstractPrimitive<Float>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static ARFloat i = new ARFloat();
	public static ARFloat get() { return i; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
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
	
}
