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
	
	public MassiveTreeSet<String, ComparatorCaseInsensitive> exceptions = new MassiveTreeSet<>(ComparatorCaseInsensitive.get());
	
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
		this.exceptions.addAll(asStrings(exceptions));
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

	public MassiveTreeSet<String, ComparatorCaseInsensitive> asStrings(Object... exceptions)
	{
		return asStrings(Arrays.asList(exceptions));
	}
	
	public MassiveTreeSet<String, ComparatorCaseInsensitive> asStrings(Iterable<?> exceptions)
	{
		MassiveTreeSet<String, ComparatorCaseInsensitive> ret = new MassiveTreeSet<>(ComparatorCaseInsensitive.get());
		
		for (Object exception : exceptions)
		{
			String string = asString(exception);
			ret.add(string);
		}
		
		return ret;
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
