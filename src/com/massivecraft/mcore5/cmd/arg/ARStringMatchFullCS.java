package com.massivecraft.mcore5.cmd.arg;

import java.util.Collection;

public class ARStringMatchFullCS extends ARAbstractStringMatch
{
	// -------------------------------------------- //
	// IMPLEMENTATION
	// -------------------------------------------- //
	
	@Override
	public Integer matches(String arg, String alt)
	{
		return alt.equals(arg) ? 0 : null;
	}

	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public ARStringMatchFullCS(String typename, Collection<Collection<String>> altColls)
	{
		super(typename, altColls);
	}
}
