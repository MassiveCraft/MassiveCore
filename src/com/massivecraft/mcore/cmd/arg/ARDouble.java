package com.massivecraft.mcore.cmd.arg;

public class ARDouble extends ARAbstractPrimitive<Double>
{	
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
	
	// -------------------------------------------- //
	// INSTANCE
	// -------------------------------------------- //
	
	private static ARDouble i = new ARDouble();
	public static ARDouble get() { return i; }
	
}
