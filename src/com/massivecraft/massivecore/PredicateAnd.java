package com.massivecraft.massivecore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class PredicateAnd<T> implements Predicate<T>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	@SafeVarargs
	public static <T> PredicateAnd<T> get(Predicate<T>... predicates) { return new PredicateAnd<T>(predicates); }
	@SafeVarargs
	public PredicateAnd(Predicate<T>... predicates)
	{
		this(Arrays.asList(predicates));
	}
	
	public static <T> PredicateAnd<T> get(Collection<Predicate<T>> predicates) { return new PredicateAnd<T>(predicates); }
	public PredicateAnd(Collection<Predicate<T>> predicates)
	{
		this.predicates = Collections.unmodifiableList(new ArrayList<Predicate<T>>(predicates));
	}
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	private final List<Predicate<T>> predicates;
	public List<Predicate<T>> getPredicates() { return this.predicates; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public boolean apply(T type)
	{
		for (Predicate<T> predicate : this.getPredicates())
		{
			if (!predicate.apply(type)) return false;
		}
		return true;
	}
	
}
