package com.massivecraft.massivecore.collections;

import java.util.Collection;
import java.util.Comparator;

/**
 * This subclass does nothing new except implementing the Def interface.
 * Def is short for "Default" and means GSON should handle "null" as "empty".
 */
public class MassiveTreeSetDef<E, C extends Comparator<? super E>> extends MassiveTreeSet<E, C> implements Def
{
	// -------------------------------------------- //
	// CONSTANTS
	// -------------------------------------------- //
	
	private static final long serialVersionUID = 1L;
	
	// -------------------------------------------- //
	// CONSTRUCT: SUPER
	// -------------------------------------------- //
	
	public MassiveTreeSetDef(Object comparator)
	{
		super(comparator);
	}
	
	public MassiveTreeSetDef(Object comparator, Collection<? extends E> c)
	{
		super(comparator, c);
	}
	
	@SafeVarargs
	public MassiveTreeSetDef(Object comparator, E... elements)
	{
		super(comparator, elements);
	}

}
