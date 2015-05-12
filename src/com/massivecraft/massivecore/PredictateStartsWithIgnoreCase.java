package com.massivecraft.massivecore;

// Inspired by: String#regionMatches(ignoreCase, toffset, other, ooffset, len)
public class PredictateStartsWithIgnoreCase implements Predictate<String>
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	private final String prefixLower;
	private final String prefixUpper;
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public static PredictateStartsWithIgnoreCase get(String prefix) { return new PredictateStartsWithIgnoreCase(prefix); }
	public PredictateStartsWithIgnoreCase(String prefix)
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
