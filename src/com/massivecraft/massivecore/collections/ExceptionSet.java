package com.massivecraft.massivecore.collections;

import java.util.Arrays;
import java.util.Collection;

import com.massivecraft.massivecore.comparator.ComparatorCaseInsensitive;

public class ExceptionSet
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	private boolean standard = true;
	public boolean isStandard() { return this.standard; }
	public void setStandard(boolean standard) { this.standard = standard; }
	
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
	public <O extends Object> ExceptionSet(boolean standard, O... exceptions)
	{
		this.standard = standard;
		if (exceptions.length == 0) return;
		Collection<String> strings = stringifyAll(exceptions);
		this.exceptions.addAll(strings);
	}
	
	// -------------------------------------------- //
	// CONTAINS
	// -------------------------------------------- //
	
	public <O extends Object> boolean contains(O object)
	{
		if (object == null) return ! this.standard;
		String string = stringify(object);
		if (this.exceptions.contains(string)) return ! this.standard;
		return this.standard;
	}
	
	// -------------------------------------------- //
	// IS EMPTY
	// -------------------------------------------- //
	
	public boolean isEmpty()
	{
		return ! this.isStandard() && this.exceptions.isEmpty();
	}
	
	// -------------------------------------------- //
	// STRINGIFY
	// -------------------------------------------- //
	
	public String stringify(Object object)
	{
		if (object == null) return null;
		if (object instanceof String) return (String)object;
		String ret = this.stringifyInner(object);
		if (ret != null) return ret;
		return object.toString();
	}
	
	public String stringifyInner(Object object)
	{
		return null;
	}
	
	// -------------------------------------------- //
	// STRINGIFY ALL
	// -------------------------------------------- //
	
	public MassiveTreeSet<String, ComparatorCaseInsensitive> stringifyAll(Object... exceptions)
	{
		return stringifyAll(Arrays.asList(exceptions));
	}
	
	public MassiveTreeSet<String, ComparatorCaseInsensitive> stringifyAll(Iterable<?> exceptions)
	{
		MassiveTreeSet<String, ComparatorCaseInsensitive> ret = new MassiveTreeSet<>(ComparatorCaseInsensitive.get());
		
		for (Object exception : exceptions)
		{
			String string = stringify(exception);
			ret.add(string);
		}
		
		return ret;
	}
	
}
