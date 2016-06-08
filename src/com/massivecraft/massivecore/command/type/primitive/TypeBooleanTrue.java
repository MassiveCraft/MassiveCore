package com.massivecraft.massivecore.command.type.primitive;

public class TypeBooleanTrue extends TypeBooleanAbstract
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static TypeBooleanTrue i = new TypeBooleanTrue();
	public static TypeBooleanTrue get() { return i; }
	public TypeBooleanTrue()
	{
		super(NAME_TRUE, NAME_FALSE);
	}

}
