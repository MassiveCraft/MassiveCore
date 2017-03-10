package com.massivecraft.massivecore.predicate;

import com.massivecraft.massivecore.util.ReflectionUtil;

public class PredicateIsClassSingleton implements Predicate<Class<?>>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static PredicateIsClassSingleton i = new PredicateIsClassSingleton();
	public static PredicateIsClassSingleton get() { return i; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //

	@Override
	public boolean apply(Class clazz)
	{
		return ReflectionUtil.isSingleton(clazz);
	}
	
}
