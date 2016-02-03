package com.massivecraft.massivecore.predicate;

// Inspired by: String#regionMatches(ignoreCase, toffset, other, ooffset, len)
public class PredicateEqualsIgnoreCase implements Predicate<String>
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	private final String strLower;
	private final String strUpper;
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public static PredicateEqualsIgnoreCase get(String prefix) { return new PredicateEqualsIgnoreCase(prefix); }
	public PredicateEqualsIgnoreCase(String str)
	{
		if (str == null) throw new NullPointerException("str");
		this.strLower = str.toLowerCase();
		this.strUpper = str.toUpperCase();
	}

	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //

	@Override
	public boolean apply(String str)
	{
		if (str == null) return false;
		int index = this.strLower.length();
		if (str.length() != index) return false;
		while (index-- > 0)
		{
			char c = str.charAt(index);
			if (c == strLower.charAt(index)) continue;
			if (c != strUpper.charAt(index)) return false;
		}
		return true;
	}
	
}
