package com.massivecraft.massivecore.collections;

import java.util.AbstractSet;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

public abstract class BackstringSet<T> extends AbstractSet<T>
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	private Set<String> stringSet = new LinkedHashSet<String>();
	public Set<String> getStringSet() { return this.stringSet; }
	
	// -------------------------------------------- //
	// ABSTRACT CONVERSION
	// -------------------------------------------- //
	
	public abstract T convertFromString(String string);
	public abstract String convertToString(Object t);
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public BackstringSet()
	{
		
	}
	
	public BackstringSet(Collection<?> c)
	{
		if (c != null)
		{
			for (Object o : c)
			{
				this.stringSet.add(this.convertToString(o));
			}
		}
	}
	
	public BackstringSet(Object... objects)
	{
		this(Arrays.asList(objects));
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public Iterator<T> iterator()
	{
		return new BackstringIterator<T>(this.stringSet.iterator(), this);
	}
	
	@Override
	public int size()
	{
		return this.stringSet.size();
	}

	@Override
	public boolean contains(Object o)
	{
		if (o == null)
		{
			return this.stringSet.contains(null);
		}
		else
		{
			return this.stringSet.contains(this.convertToString(o));
		}
	}

	@Override
	public boolean add(T e)
	{
		if (e == null)
		{
			return this.stringSet.add(null);
		}
		else
		{
			return this.stringSet.add(this.convertToString(e));
		}
	}

	@Override
	public boolean remove(Object o)
	{
		if (o == null)
		{
			return this.stringSet.remove(null);
		}
		else
		{
			return this.stringSet.remove(this.convertToString(o));
		}
	}

	@Override
	public void clear()
	{
		this.stringSet.clear();
	}

}
