package com.massivecraft.massivecore.comparator;

import java.util.List;

public class ComparatorWithList<T> extends ComparatorAbstract<T>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	public static <T> ComparatorWithList<T> get(List<T> list) { return new ComparatorWithList<>(list); }
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	private List<T> list = null;
	public List<T> getList() { return this.list; }
	public ComparatorWithList<T> setList(List<T> list) { this.list = list; return this; }
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public ComparatorWithList(List<T> list)
	{
		this.list = list;
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //

	@Override
	public int compareInner(T object1, T object2)
	{
		int index1 = this.getList().indexOf(object1);
		int index2 = this.getList().indexOf(object2);
		
		return Integer.compare(index1, index2);
	}

}
