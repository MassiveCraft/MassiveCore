package com.massivecraft.massivecore.comparator;

import java.util.Comparator;

public abstract class ComparatorAbstractTransformer<T, X> extends ComparatorAbstractWrapper<T, X>
{	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public ComparatorAbstractTransformer(Comparator<X> comparator)
	{
		super(comparator);
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public int compareInner(T type1, T type2)
	{
		X x1 = this.transform(type1);
		X x2 = this.transform(type2);
		
		return this.getComparator().compare(x1, x2);
	}
	
	// -------------------------------------------- //
	// ABSTRACT
	// -------------------------------------------- //
	
	public abstract X transform(T type);
	
}
