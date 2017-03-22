package com.massivecraft.massivecore.collections;

import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.type.RegistryType;
import com.massivecraft.massivecore.command.type.Type;

import java.util.AbstractSet;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class BackstringSet<T> extends AbstractSet<T>
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	private final Type<T> type;
	
	private final Set<String> stringSet;
	public Set<String> getStringSet() { return this.stringSet; }
	
	// -------------------------------------------- //
	// CONVTERT
	// -------------------------------------------- //
	
	private T convertFromString(String string) throws MassiveException
	{
		if (string == null) return null;
		return this.type.read(string);
	}
	
	@SuppressWarnings("unchecked")
	private String convertToString(Object object)
	{
		if (object == null) return null;
		
		if (object instanceof String)
		{
			return (String)object;
		}
		
		return this.type.getId((T) object);
	}
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public BackstringSet(Type<T> type)
	{
		this.type = type;
		this.stringSet = new MassiveSet<>();
	}
	
	public BackstringSet(Type<T> type, Collection<?> collection)
	{
		this(type);
		if (collection != null)
		{
			for (Object object : collection)
			{
				String string = this.convertToString(object);
				this.stringSet.add(string);
			}
		}
	}
	
	public BackstringSet(Type<T> type, Object... objects)
	{
		this(type, Arrays.asList(objects));
	}
	
	// -------------------------------------------- //
	// MOAR CONSTRUCT
	// -------------------------------------------- //
	
	@SuppressWarnings("unchecked")
	public BackstringSet(Class<T> clazz)
	{
		this((Type<T>) RegistryType.getType(clazz));
	}
	
	@SuppressWarnings("unchecked")
	public BackstringSet(Class<T> clazz, Collection<?> collection)
	{
		this((Type<T>) RegistryType.getType(clazz), collection);
	}
	
	@SuppressWarnings("unchecked")
	public BackstringSet(Class<T> clazz, Object... objects)
	{
		this((Type<T>) RegistryType.getType(clazz), objects);
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public Iterator<T> iterator()
	{
		// Create
		List<T> temporaryList = new MassiveList<>();
		
		// Fill
		for (String string : this.getStringSet())
		{
			try
			{
				T value = this.convertFromString(string);
				if (value != null) temporaryList.add(value);
			}
			catch (MassiveException ignored)
			{
				// ignored
			}
		}
		
		// Return
		final Iterator<T> temporaryIterator = temporaryList.iterator();
		return new Iterator<T>()
		{
			@Override
			public boolean hasNext()
			{
				return temporaryIterator.hasNext();
			}
			
			@Override
			public T next()
			{
				return temporaryIterator.next();
			}
			
			@Override
			public void remove()
			{
				String message = String.format("%s iterator does not support removal.", BackstringSet.class.getSimpleName());
				throw new UnsupportedOperationException(message);
			}
		};
	}
	
	@Override
	public int size()
	{
		return this.stringSet.size();
	}

	@Override
	public boolean contains(Object object)
	{
		String string = this.convertToString(object);
		return this.stringSet.contains(string);
	}

	@Override
	public boolean add(T object)
	{
		return this.addObject(object);
	}
	
	public boolean addString(String string)
	{
		return this.addObject(string);
	}
	
	private boolean addObject(Object object)
	{
		String string = this.convertToString(object);
		return this.stringSet.add(string);
	}

	@Override
	public boolean remove(Object object)
	{
		String string = this.convertToString(object);
		return this.stringSet.remove(string);
	}

	@Override
	public void clear()
	{
		this.stringSet.clear();
	}

}
