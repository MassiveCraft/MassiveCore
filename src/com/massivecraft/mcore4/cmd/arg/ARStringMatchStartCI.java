package com.massivecraft.mcore4.cmd.arg;

import java.util.Collection;

public class ARStringMatchStartCI extends ARAbstractStringMatch
{
	// -------------------------------------------- //
	// IMPLEMENTATION
	// -------------------------------------------- //
	
	@Override
	public boolean matches(String arg, String alt)
	{
		arg = arg.toLowerCase();
		alt = alt.toLowerCase();
		return alt.startsWith(arg);
	}

	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public ARStringMatchStartCI(String typename, Collection<Collection<String>> altColls)
	{
		super(typename, altColls);
	}
}
