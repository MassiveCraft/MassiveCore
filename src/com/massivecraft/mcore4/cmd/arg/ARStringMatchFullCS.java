package com.massivecraft.mcore4.cmd.arg;

import java.util.Collection;

public class ARStringMatchFullCS extends ARAbstractStringMatch
{
	// -------------------------------------------- //
	// IMPLEMENTATION
	// -------------------------------------------- //
	
	@Override
	public boolean matches(String arg, String alt)
	{
		return alt.equals(arg);
	}

	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public ARStringMatchFullCS(String typename, Collection<Collection<String>> altColls)
	{
		super(typename, altColls);
	}
}
