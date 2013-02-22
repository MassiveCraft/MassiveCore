package com.massivecraft.mcore.cmd.arg;

public class ARByte extends ARAbstractPrimitive<Byte>
{	
	@Override
	public String typename()
	{
		return "byte";
	}

	@Override
	public Byte convert(String arg) throws Exception
	{
		return Byte.parseByte(arg);
	}
	
	// -------------------------------------------- //
	// INSTANCE
	// -------------------------------------------- //
	
	private static ARByte i = new ARByte();
	public static ARByte get() { return i; }
	
}
