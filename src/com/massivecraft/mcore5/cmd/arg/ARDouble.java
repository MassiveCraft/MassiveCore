package com.massivecraft.mcore5.cmd.arg;

public class ARDouble extends ARAbstractPrimitive<Double>
{	
	@Override
	public String typename()
	{
		return "double";
	}

	@Override
	public Double convert(String str) throws Exception
	{
		return Double.parseDouble(str);
	}
	
	// -------------------------------------------- //
	// INSTANCE
	// -------------------------------------------- //
	
	private static ARDouble i = new ARDouble();
	public static ARDouble get() { return i; }
	
}
