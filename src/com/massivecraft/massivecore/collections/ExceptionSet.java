package com.massivecraft.massivecore.collections;

import java.util.Arrays;

import com.massivecraft.massivecore.comparator.ComparatorCaseInsensitive;

public class ExceptionSet<T>
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	public boolean standard = true;
	public boolean isStandard() { return this.standard; }
	
	public MassiveTreeSet<String, ComparatorCaseInsensitive> exceptions = new MassiveTreeSet<String, ComparatorCaseInsensitive>(ComparatorCaseInsensitive.get());
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public ExceptionSet()
	{
		
	}

	public ExceptionSet(boolean standard)
	{
		this.standard = standard;
	}
	
	public ExceptionSet(boolean standard, String... exceptions)
	{
		this.standard = standard;
		this.exceptions.addAll(Arrays.asList(exceptions));
	}
	
	// -------------------------------------------- //
	// CONTAINS
	// -------------------------------------------- //
	
	public boolean containsString(String item)
	{
		if (this.exceptions.contains(item)) return ! this.standard;
		return this.standard;
	}
	
	public boolean contains(String item)
	{
		return this.containsString(item);
	}

	public boolean contains(T item)
	{
		if (item == null) return ! this.standard;
		
		return this.contains(convert(item));
	}
	
	public String convert(T item)
	{
		return item.toString();
	}
	
}
