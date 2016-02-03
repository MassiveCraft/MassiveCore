package com.massivecraft.massivecore.comparator;

import java.util.Comparator;

public abstract class ComparatorAbstractWrapper<T, X> extends ComparatorAbstract<T>
{	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	private Comparator<X> comparator;
	public Comparator<X> getComparator() { return this.comparator; }
	public void setComparator(Comparator<X> comparator) { this.comparator = comparator; }
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public ComparatorAbstractWrapper(Comparator<X> comparator)
	{
		this.comparator = comparator;
	}
	
}
