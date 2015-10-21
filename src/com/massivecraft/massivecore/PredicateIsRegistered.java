package com.massivecraft.massivecore;

public class PredicateIsRegistered implements Predicate<Registerable>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static PredicateIsRegistered i = new PredicateIsRegistered();
	public static PredicateIsRegistered get() { return i; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //

	@Override
	public boolean apply(Registerable registerable)
	{
		return registerable.isRegistered();
	}
	
}
