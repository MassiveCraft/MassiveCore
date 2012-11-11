package com.massivecraft.mcore5.cmd.arg;

public class ARLong extends ARAbstractPrimitive<Long>
{	
	@Override
	public String typename()
	{
		return "long";
	}

	@Override
	public Long convert(String str) throws Exception
	{
		return Long.parseLong(str);
	}
	
	// -------------------------------------------- //
	// INSTANCE
	// -------------------------------------------- //
	
	private static ARLong i = new ARLong();
	public static ARLong get() { return i; }
	
}
