package com.massivecraft.massivecore;

public class PredictateStartsWithIgnoreCase implements Predictate<String>
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	private final String prefix;
	public String getPrefix() { return this.prefix; };
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public static PredictateStartsWithIgnoreCase get(String prefix) { return new PredictateStartsWithIgnoreCase(prefix); }
	public PredictateStartsWithIgnoreCase(String prefix)
	{
		this.prefix = prefix.toLowerCase();
	}

	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //

	@Override
	public boolean apply(String str)
	{
		if (str == null) return false;
		return str.toLowerCase().startsWith(this.getPrefix());
	}
	
}
