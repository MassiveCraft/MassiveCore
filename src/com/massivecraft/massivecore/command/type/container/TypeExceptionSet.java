package com.massivecraft.massivecore.command.type.container;

import com.massivecraft.massivecore.collections.ExceptionSet;
import com.massivecraft.massivecore.command.type.TypeReflection;

public class TypeExceptionSet extends TypeReflection<ExceptionSet> 
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static TypeExceptionSet i = new TypeExceptionSet();
	public static TypeExceptionSet get() { return i; }
	public TypeExceptionSet()
	{
		super(ExceptionSet.class);
	}

}
