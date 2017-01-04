package com.massivecraft.massivecore.predicate;

public class PredicateStringStartsWith implements Predicate<String>
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //

	private final String prefix;

	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //

	public static PredicateStringStartsWith get(String prefix) { return new PredicateStringStartsWith(prefix); }
	public PredicateStringStartsWith(String prefix)
	{
		if (prefix == null) throw new NullPointerException("prefix");
		this.prefix = prefix;
	}

	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //

	@Override
	public boolean apply(String str)
	{
		if (str == null) return false;
		return str.startsWith(prefix);
	}
	
}
