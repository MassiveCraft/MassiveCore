package com.massivecraft.massivecore.collections;

import java.util.Iterator;

public class BackstringIterator<E> implements Iterator<E>
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	private final Iterator<String> iterator;
	private final BackstringSet<E> set;
	private String next = null;
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public BackstringIterator(Iterator<String> iterator, BackstringSet<E> set)
	{
		this.iterator = iterator;
		this.set = set;
		this.prepareNext();
	}
	
	// -------------------------------------------- //
	// INTERNAL
	// -------------------------------------------- //
	
	private void prepareNext()
	{
		String perhaps = null;
		while (this.iterator.hasNext())
		{
			perhaps = this.iterator.next();
			if (this.set.convertFromString(perhaps) != null)
			{
				this.next = perhaps;
				break;
			}
		}
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public boolean hasNext()
	{
		return (this.next != null); 
	}

	@Override
	public E next()
	{
		String current = this.next;
		this.prepareNext();
		if (current == null) return null;
		return this.set.convertFromString(current);
	}
	
	@Override
	public void remove()
	{
		throw new UnsupportedOperationException();
	}
	
	// -------------------------------------------- //
	// EXTRAS
	// -------------------------------------------- //
	
	public String peek()
	{
		return next;
	}
	
}
