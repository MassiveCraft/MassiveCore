package com.massivecraft.massivecore.predicate;

// Inspired by: String#regionMatches(ignoreCase, toffset, other, ooffset, len)
public class PredicateStartsWithIgnoreCase implements Predicate<String>
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	private final String prefixLower;
	private final String prefixUpper;
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public static PredicateStartsWithIgnoreCase get(String prefix) { return new PredicateStartsWithIgnoreCase(prefix); }
	public PredicateStartsWithIgnoreCase(String prefix)
	{
		if (prefix == null) throw new NullPointerException("prefix");
		this.prefixLower = prefix.toLowerCase();
		this.prefixUpper = prefix.toUpperCase();
	}

	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //

	@Override
	public boolean apply(String str)
	{
		if (str == null) return false;
		int index = this.prefixLower.length();
		if (str.length() < index) return false;
		while (index-- > 0)
		{
			char c = str.charAt(index);
			if (c == prefixLower.charAt(index)) continue;
			if (c != prefixUpper.charAt(index)) return false;
		}
		return true;
	}
	
}
