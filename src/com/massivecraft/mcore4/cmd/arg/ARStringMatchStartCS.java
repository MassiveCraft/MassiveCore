package com.massivecraft.mcore4.cmd.arg;

import java.util.Collection;

public class ARStringMatchStartCS extends ARAbstractStringMatch
{
	// -------------------------------------------- //
	// IMPLEMENTATION
	// -------------------------------------------- //
	
	@Override
	public boolean matches(String arg, String alt)
	{
		return alt.startsWith(arg);
	}

	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public ARStringMatchStartCS(String typename, Collection<Collection<String>> altColls)
	{
		super(typename, altColls);
	}
}
