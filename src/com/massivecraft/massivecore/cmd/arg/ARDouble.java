package com.massivecraft.massivecore.cmd.arg;

public class ARDouble extends ARAbstractPrimitive<Double>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static ARDouble i = new ARDouble();
	public static ARDouble get() { return i; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public String typename()
	{
		return "double";
	}

	@Override
	public Double convert(String arg) throws Exception
	{
		return Double.parseDouble(arg);
	}
	
}
