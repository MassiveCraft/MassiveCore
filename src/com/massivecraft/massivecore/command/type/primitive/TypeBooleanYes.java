package com.massivecraft.massivecore.command.type.primitive;

public class TypeBooleanYes extends TypeBooleanAbstract
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static TypeBooleanYes i = new TypeBooleanYes();
	public static TypeBooleanYes get() { return i; }
	public TypeBooleanYes()
	{
		super(NAME_YES, NAME_NO);
	}

}
