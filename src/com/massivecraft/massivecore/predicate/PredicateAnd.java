package com.massivecraft.massivecore.predicate;

import java.util.Collection;
import java.util.List;

import com.google.common.collect.ImmutableList;

public class PredicateAnd<T> implements Predicate<T>
{
	// -------------------------------------------- //
	// INSTANCE
	// -------------------------------------------- //
	
	@SafeVarargs
	public static <T> PredicateAnd<T> get(Predicate<? super T>... predicates) { return new PredicateAnd<T>(predicates); }
	public static <T> PredicateAnd<T> get(Collection<Predicate<? super T>> predicates) { return new PredicateAnd<T>(predicates); }
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	@SafeVarargs
	public PredicateAnd(Predicate<? super T>... predicates)
	{
		this(ImmutableList.copyOf(predicates));
	}
	
	public PredicateAnd(Collection<Predicate<? super T>> predicates)
	{
		this.predicates = ImmutableList.copyOf(predicates);
	}
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	private final List<Predicate<? super T>> predicates;
	public List<Predicate<? super T>> getPredicates() { return this.predicates; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public boolean apply(T type)
	{
		for (Predicate<? super T> predicate : this.getPredicates())
		{
			if ( ! predicate.apply(type)) return false;
		}
		return true;
	}
	
}
