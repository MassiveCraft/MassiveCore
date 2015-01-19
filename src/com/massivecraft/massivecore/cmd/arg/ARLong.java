package com.massivecraft.massivecore.cmd.arg;

public class ARLong extends ARAbstractPrimitive<Long>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static ARLong i = new ARLong();
	public static ARLong get() { return i; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public String typename()
	{
		return "long";
	}

	@Override
	public Long convert(String arg) throws Exception
	{
		return Long.parseLong(arg);
	}
	
}
