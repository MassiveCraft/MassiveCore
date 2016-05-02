package com.massivecraft.massivecore.comparator;

import java.util.Comparator;

public abstract class ComparatorAbstract<T> implements Comparator<T>
{
	// -------------------------------------------- //
	// SMART
	// -------------------------------------------- //
	// Smart comparators falls back to generalized Object comparison solutions we have implemented.
	// They just try to compare better if the initial comparison does not find a difference.
	// Note that they leave equals be. Objects that actually are equal will remain that way.
	
	private boolean smart = false;
	public boolean isSmart() { return this.smart; }
	public ComparatorAbstract<T> setSmart(boolean smart) { this.smart = smart; return this; }
	
	// -------------------------------------------- //
	// LENIENT
	// -------------------------------------------- //
	// Lenient comparators will not ever accept 0 as a return value.
	// The only common user case is when sorting map entries by value.
	
	private boolean lenient = false;
	public boolean isLenient() { return this.lenient; }
	public ComparatorAbstract<T> setLenient(boolean lenient) { this.lenient = lenient; return this; }
	
	// -------------------------------------------- //
	// REVERSED
	// -------------------------------------------- //
	// Reversed comparators multiply the return value with -1.
	
	private boolean reversed = false;
	public boolean isReversed() { return this.reversed; }
	public ComparatorAbstract<T> setReversed(boolean reversed) { this.reversed = reversed; return this; }
	
	// -------------------------------------------- //
	// COMPARE
	// -------------------------------------------- //
	
	@Override
	public int compare(T object1, T object2)
	{
		// Create
		int ret = compareSystem(object1, object2);
		
		// Lenient
		if (this.isLenient() && ret == 0)
		{
			ret = ComparatorIdentity.get().compare(object1, object2);
			if (ret == 0) ret = 1;
		}
		
		// Reversed
		if (this.isReversed())
		{
			ret *= -1;
		}
		
		// Return
		return ret;
	}
	
	// -------------------------------------------- //
	// COMPARE > SYSTEM
	// -------------------------------------------- //
	
	private int compareSystem(T object1, T object2)
	{	
		// Create
		int ret = 0;
		
		// Null
		if (object1 == null && object2 == null) return 0;
		if (object1 == null) return -1;
		if (object2 == null) return +1;
		
		// Inner
		ret = this.compareInner(object1, object2);
		if (ret != 0) return ret;
		
		// Smart
		if (this.isSmart())
		{
			ret = ComparatorPrioritized.get().compare(object1, object2);
			if (ret != 0) return ret;
			
			ret = ComparatorNamed.get().compare(object1, object2);
			if (ret != 0) return ret;
			
			ret = ComparatorIdentified.get().compare(object1, object2);
			if (ret != 0) return ret;
			
			ret = ComparatorComparable.get().compare(object1, object2);
			if (ret != 0) return ret;
			
			ret = ComparatorEntry.get().compare(object1, object2);
			if (ret != 0) return ret;
			
			ret = ComparatorCollection.get().compare(object1, object2);
			if (ret != 0) return ret;
			
			ret = ComparatorMap.get().compare(object1, object2);
			if (ret != 0) return ret;
		}
		
		// Return
		return ret;
	}
	
	// -------------------------------------------- //
	// COMPARE > INNER
	// -------------------------------------------- //
	
	public abstract int compareInner(T type1, T type2);
	
	// -------------------------------------------- //
	// UTILITY
	// -------------------------------------------- //
	
	@SuppressWarnings("unchecked")
	public int compare(T... objects)
	{
		if (objects == null) throw new NullPointerException("objects");
		if (objects.length % 2 != 0) throw new IllegalArgumentException("objects length not even");
		
		int index = 1;
		while (index < objects.length)
		{
			T object1 = objects[index - 1];
			T object2 = objects[index];
			
			int ret = this.compare(object1, object2);
			if (ret != 0) return ret;
			
			index += 2;
		}
		
		return 0;
	}
	
}
