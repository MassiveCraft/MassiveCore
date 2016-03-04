package com.massivecraft.massivecore.comparator;

public class ComparatorCaseInsensitive extends ComparatorAbstract<String>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static ComparatorCaseInsensitive i = new ComparatorCaseInsensitive();
	public static ComparatorCaseInsensitive get() { return i; }

	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //

	@Override
	public Integer compareInner(String string1, String string2)
	{
		return String.CASE_INSENSITIVE_ORDER.compare(string1, string2);
	}

}
