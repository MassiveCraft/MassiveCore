package com.massivecraft.mcore5.cmd.arg;

public class ARString extends ARAbstractPrimitive<String>
{
	@Override
	public String typename()
	{
		return "string";
	}

	@Override
	public String convert(String str) throws Exception
	{
		return str;
	}
	
	// -------------------------------------------- //
	// INSTANCE
	// -------------------------------------------- //
	
	private static ARString i = new ARString();
	public static ARString get() { return i; }
	
}
