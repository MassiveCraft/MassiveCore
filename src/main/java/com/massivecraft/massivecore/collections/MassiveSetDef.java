package com.massivecraft.massivecore.collections;

import java.util.Collection;

/**
 * This subclass does nothing new except implementing the Def interface.
 * Def is short for "Default" and means GSON should handle "null" as "empty".
 */
public class MassiveSetDef<E> extends MassiveSet<E> implements Def
{
	// -------------------------------------------- //
	// CONSTANTS
	// -------------------------------------------- //
	
	private static final long serialVersionUID = 1L;
	
	// -------------------------------------------- //
	// CONSTRUCT: SUPER
	// -------------------------------------------- //
	
	public MassiveSetDef(int initialCapacity, float loadFactor)
	{
		super(initialCapacity, loadFactor);
	}

	public MassiveSetDef(int initialCapacity)
	{
		super(initialCapacity);
	}

	public MassiveSetDef()
	{
		super();
	}

	public MassiveSetDef(Collection<? extends E> c)
	{
		super(c);
	}
	
	@SafeVarargs
	public MassiveSetDef(E... elements)
	{
		super(elements);
	}

}
