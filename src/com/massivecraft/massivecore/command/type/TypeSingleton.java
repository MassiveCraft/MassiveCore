package com.massivecraft.massivecore.command.type;

public class TypeSingleton<T> extends TypeReflection<T> 
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	public static <T> TypeSingleton<T> get(T singleton){ return new TypeSingleton<>(singleton); }
	@SuppressWarnings("unchecked")
	public TypeSingleton(T singleton)
	{
		super((Class<T>) singleton.getClass());
		super.setAll(singleton);
	}

}
