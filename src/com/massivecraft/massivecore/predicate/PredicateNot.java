package com.massivecraft.massivecore.predicate;

public class PredicateNot<T> implements Predicate<T>
{
	// -------------------------------------------- //
	// INSTANCE
	// -------------------------------------------- //
	
	public static <T> PredicateNot<T> get(Predicate<? super T> predicate) { return new PredicateNot<>(predicate); }
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public PredicateNot(Predicate<? super T> predicate)
	{
		this.predicate = predicate;
	}
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	private final Predicate<? super T> predicate;
	public Predicate<? super T> getPredicate() { return this.predicate; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public boolean apply(T type)
	{
		return !this.getPredicate().apply(type);
	}
	
}
