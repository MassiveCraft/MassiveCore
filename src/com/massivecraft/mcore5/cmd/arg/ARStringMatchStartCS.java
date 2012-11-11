package com.massivecraft.mcore5.cmd.arg;

import java.util.Collection;

public class ARStringMatchStartCS extends ARAbstractStringMatch
{
	// -------------------------------------------- //
	// IMPLEMENTATION
	// -------------------------------------------- //
	
	@Override
	public Integer matches(String arg, String alt)
	{
		if (alt.startsWith(arg))
		{
			return alt.length() - arg.length();
		}
		else
		{
			return null;
		}
	}

	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public ARStringMatchStartCS(String typename, Collection<Collection<String>> altColls)
	{
		super(typename, altColls);
	}
}
