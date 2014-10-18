package com.massivecraft.massivecore;

import java.util.Comparator;

public class CaseInsensitiveComparator implements Comparator<String>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static CaseInsensitiveComparator i = new CaseInsensitiveComparator();
	public static CaseInsensitiveComparator get() { return i; }

	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //

	@Override
	public int compare(String o1, String o2)
	{
		return String.CASE_INSENSITIVE_ORDER.compare(o1, o2);
	}

}
