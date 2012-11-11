package com.massivecraft.mcore5.cmd.arg;

public class ARByte extends ARAbstractPrimitive<Byte>
{	
	@Override
	public String typename()
	{
		return "byte";
	}

	@Override
	public Byte convert(String str) throws Exception
	{
		return Byte.parseByte(str);
	}
	
	// -------------------------------------------- //
	// INSTANCE
	// -------------------------------------------- //
	
	private static ARByte i = new ARByte();
	public static ARByte get() { return i; }
	
}
