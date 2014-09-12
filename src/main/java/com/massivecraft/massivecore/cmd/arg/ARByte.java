package com.massivecraft.massivecore.cmd.arg;

public class ARByte extends ARAbstractPrimitive<Byte>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static ARByte i = new ARByte();
	public static ARByte get() { return i; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
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
	
}
