package com.massivecraft.massivecore.collections;

import com.massivecraft.massivecore.command.editor.annotation.EditorType;
import com.massivecraft.massivecore.command.type.container.TypeMassiveTreeSetInsensitive;
import com.massivecraft.massivecore.comparator.ComparatorCaseInsensitive;
import com.massivecraft.massivecore.util.MUtil;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

public class ExceptionSet
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	private boolean standard = true;
	public boolean isStandard() { return this.standard; }
	public void setStandard(boolean standard) { this.standard = standard; }
	
	@EditorType(TypeMassiveTreeSetInsensitive.class)
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
	public <O> ExceptionSet(boolean standard, O... exceptions)
	{
		this.standard = standard;
		if (exceptions.length == 0) return;
		Collection<String> strings = stringifyAll(exceptions);
		this.exceptions.addAll(strings);
	}
	
	// -------------------------------------------- //
	// CONTAINS
	// -------------------------------------------- //
	
	public <O> boolean contains(O object)
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
	
	// -------------------------------------------- //
	// EQUALS & HASH CODE
	// -------------------------------------------- //
	
	@Override
	public boolean equals(Object object)
	{
		if ( ! (object instanceof ExceptionSet)) return false;
		ExceptionSet that = (ExceptionSet)object;
		
		return MUtil.equals(
			this.standard, that.standard,
			this.exceptions, that.exceptions
		);
	}
	
	@Override
	public int hashCode()
	{
		return Objects.hash(
			this.standard,
			this.exceptions
		);
	}
	
}
