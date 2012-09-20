package com.massivecraft.mcore4.cmd.arg;

import java.util.Collection;

public class ARStringMatchFullCI extends ARAbstractStringMatch
{
	// -------------------------------------------- //
	// IMPLEMENTATION
	// -------------------------------------------- //
	
	@Override
	public boolean matches(String arg, String alt)
	{
		return alt.equalsIgnoreCase(arg);
	}

	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public ARStringMatchFullCI(String typename, Collection<Collection<String>> altColls)
	{
		super(typename, altColls);
	}
}
