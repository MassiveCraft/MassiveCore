package com.massivecraft.mcore4.cmd.arg;

import java.util.Collection;

public class ARStringMatchFullCI extends ARAbstractStringMatch
{
	// -------------------------------------------- //
	// IMPLEMENTATION
	// -------------------------------------------- //
	
	@Override
	public Integer matches(String arg, String alt)
	{
		return alt.equalsIgnoreCase(arg) ? 0 : null;
	}

	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public ARStringMatchFullCI(String typename, Collection<Collection<String>> altColls)
	{
		super(typename, altColls);
	}
}
