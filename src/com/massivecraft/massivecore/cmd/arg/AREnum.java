package com.massivecraft.massivecore.cmd.arg;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.bukkit.command.CommandSender;

import com.massivecraft.massivecore.util.Txt;

public class AREnum<T> extends ARAbstractSelect<T> implements ARAllAble<T>
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
		if ( ! clazz.isEnum()) throw new IllegalArgumentException("passed clazz param must be an enum");
		this.clazz = clazz;
	}
			
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public String getTypeName()
	{
		return Txt.getNicedEnumString(clazz.getSimpleName());
	}
	
	@Override
	public T select(String arg, CommandSender sender)
	{
		if (arg == null) return null;
		
		arg = getComparable(arg);
		
		// Algorithmic General Detection
		
		T startsWith = null;
		for (T value : getEnumValues(clazz))
		{
			String comparable = getComparable(value);
			if (comparable.equals(arg)) return value;
			if (comparable.startsWith(arg))
			{
				// If there already were a result
				// we have multiple results and stop.
				if (startsWith != null) return null;
				
				// Else we set the result.
				startsWith = value;
			}
		}
		
		// Nothing found
		return startsWith;
	}

	@Override
	public Collection<String> altNames(CommandSender sender)
	{
		List<String> ret = new ArrayList<String>();
		for (T value : getEnumValues(clazz))
		{
			ret.add(getComparable(value));
		}
		return ret;
	}

	@Override
	public Collection<String> getTabList(CommandSender sender, String arg)
	{
		return this.altNames(sender);
	}
	
	@Override
	public Collection<T> getAll(CommandSender sender)
	{
		return Arrays.asList(getEnumValues(clazz));
	}
	
	// -------------------------------------------- //
	// UTIL
	// -------------------------------------------- //
	
	public static <T> T[] getEnumValues(Class<T> clazz)
	{
		if (clazz == null) throw new IllegalArgumentException("passed clazz param is null");
		if ( ! clazz.isEnum()) throw new IllegalArgumentException("passed clazz param must be an enum");
		
		T[] ret = clazz.getEnumConstants();
		if (ret == null) throw new RuntimeException("failed to retrieve enum constants");
		
		return ret;
	}
	
	public static String getComparable(Object value)
	{
		if (value == null) return null;
		return getComparable(value.toString());
	}
	
	public static String getComparable(String string)
	{
		if (string == null) return null;
		return string.toLowerCase().replaceAll("[_\\-\\s]+", "");
	}

}
