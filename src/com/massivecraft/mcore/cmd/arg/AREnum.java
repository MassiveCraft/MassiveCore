package com.massivecraft.mcore.cmd.arg;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.bukkit.command.CommandSender;

import com.massivecraft.mcore.util.Txt;

public class AREnum<T> extends ARAbstractSelect<T>
{
	// -------------------------------------------- //
	// FIELD
	// -------------------------------------------- //
	
	private final Class<T> clazz;
	
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	public static <T> AREnum<T> get(Class<T> clazz)
	{
		return new AREnum<T>(clazz);
	}
	
	public AREnum(Class<T> clazz)
	{
		this.clazz = clazz;
	}
			
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public String typename()
	{
		return Txt.getNicedEnumString(clazz.getSimpleName());
	}

	@Override
	public T select(String arg, CommandSender sender)
	{
		arg = getComparable(arg);
		
		// Algorithmic General Detection
		for (T value : getEnumValues(this.clazz))
		{
			if (getComparable(value.toString()).equals(arg)) return value;
		}
		
		// Nothing found
		return null;
	}

	@Override
	public Collection<String> altNames(CommandSender sender)
	{
		List<String> ret = new ArrayList<String>();
		for (T value : getEnumValues(this.clazz))
		{
			ret.add(getComparable(value.toString()));
		}
		return ret;
	}
	
	// -------------------------------------------- //
	// UTIL
	// -------------------------------------------- //
	
	public static String getComparable(String string)
	{
		return string.toLowerCase().replaceAll("[_\\-\\s]+", "");
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T[] getEnumValues(Class<T> clazz)
	{
		try
		{
			Method method = clazz.getMethod("values");
			Object o = method.invoke(null);
			return (T[]) o;
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
}
