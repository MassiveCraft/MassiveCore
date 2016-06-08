package com.massivecraft.massivecore.command.type.primitive;

public class TypeBooleanOn extends TypeBooleanAbstract
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static TypeBooleanOn i = new TypeBooleanOn();
	public static TypeBooleanOn get() { return i; }
	public TypeBooleanOn()
	{
		super(NAME_ON, NAME_OFF);
	}

}
