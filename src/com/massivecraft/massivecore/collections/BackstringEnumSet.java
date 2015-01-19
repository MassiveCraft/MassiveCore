package com.massivecraft.massivecore.collections;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class BackstringEnumSet<T extends Enum<?>> extends BackstringSet<T>
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	private Class<T> clazz;
	private Map<String, T> name2enum = new LinkedHashMap<String, T>();
	
	private void init(Class<T> clazz)
	{
		this.clazz = clazz;
		for (T t : this.clazz.getEnumConstants())
		{
			name2enum.put(t.name(), t);
		}
	}
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public BackstringEnumSet(Class<T> clazz)
	{
		super();
		init(clazz);
	}
	
	public BackstringEnumSet(Class<T> clazz, Collection<?> c)
	{
		super(c);
		init(clazz);
	}
	
	public BackstringEnumSet(Class<T> clazz, Object... objects)
	{
		super(objects);
		init(clazz);
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public T convertFromString(String string)
	{
		return this.name2enum.get(string);
	}

	@Override
	public String convertToString(Object t)
	{
		if (t == null) return null;
		
		if (t instanceof Enum<?>)
		{
			Enum<?> e = (Enum<?>)t;
			return e.name();
		}
		
		return t.toString();
	}

}
