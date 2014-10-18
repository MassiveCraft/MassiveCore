package com.massivecraft.massivecore.collections;

import java.util.Collection;

/**
 * This subclass does nothing new except implementing the Def interface.
 * Def is short for "Default" and means GSON should handle "null" as "empty".
 */
public class MassiveListDef<E> extends MassiveList<E> implements Def
{
	// -------------------------------------------- //
	// CONSTANTS
	// -------------------------------------------- //
	
	private static final long serialVersionUID = 1L;
	
	// -------------------------------------------- //
	// CONSTRUCT: SUPER
	// -------------------------------------------- //
	
	public MassiveListDef(int initialCapacity)
	{
		super(initialCapacity);
	}

	public MassiveListDef()
	{
		super();
	}
	
	public MassiveListDef(Collection<? extends E> c)
	{
		super(c);
	}
	
	@SafeVarargs
	public MassiveListDef(E... elements)
	{
		super(elements);
	}

}
