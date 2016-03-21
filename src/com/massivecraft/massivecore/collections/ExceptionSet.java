package com.massivecraft.massivecore.collections;

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
	
	@SafeVarargs
	public <X extends Object> ExceptionSet(boolean standard, X... exceptions)
	{
		this.standard = standard;
		if (exceptions.length == 0) return;
		for (Object exception : exceptions)
		{
			String string = asString(exception);
			this.exceptions.add(string);
		}
	}
	
	// -------------------------------------------- //
	// AS STRING
	// -------------------------------------------- //
	
	public String asString(Object exception)
	{
		if (exception == null) return null;
		
		if (exception instanceof String) return (String)exception;
		
		@SuppressWarnings("unchecked")
		T t = (T)exception;
		return this.convert(t);
	}
	
	// -------------------------------------------- //
	// CONVERT
	// -------------------------------------------- //
	
	public String convert(T item)
	{
		return item.toString();
	}
	
	// -------------------------------------------- //
	// CONTAINS
	// -------------------------------------------- //
	
	public boolean contains(Object object)
	{
		if (object == null) return ! this.standard;
		String string = asString(object);
		if (this.exceptions.contains(string)) return ! this.standard;
		return this.standard;
	}
	
}
