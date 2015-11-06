package com.massivecraft.massivecore.command.type.combined;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;
import java.util.AbstractMap.SimpleEntry;

import org.bukkit.command.CommandSender;

import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.collections.MassiveList;
import com.massivecraft.massivecore.command.type.Type;
import com.massivecraft.massivecore.command.type.TypeAbstract;
import com.massivecraft.massivecore.util.Txt;

public abstract class TypeCombined<T> extends TypeAbstract<T>
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	protected List<Type<?>> types;
	public List<Type<?>> getTypes() { return this.types; }
	
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	public TypeCombined(Collection<Type<?>> types)
	{
		this.types = new MassiveList<Type<?>>(types); 
	}
	
	public TypeCombined(Type<?>... types)
	{
		this(Arrays.asList(types));
	}
	
	// -------------------------------------------- //
	// ABSTRACT
	// -------------------------------------------- //
	
	public abstract T combine(List<Object> parts);
	
	public abstract List<Object> split(T value);
	
	public List<Entry<Type<?>, Object>> splitEntries(T value)
	{
		// Create
		List<Entry<Type<?>, Object>> ret = new MassiveList<Entry<Type<?>, Object>>();
		
		// Fill
		List<?> parts = this.split(value);
		if (parts.size() > this.getTypes().size()) throw new RuntimeException("Too many parts!");
		for (int i = 0; i < parts.size(); i++)
		{
			Type<?> type = this.getTypes().get(i);
			Object part = parts.get(i);
			SimpleEntry<Type<?>, Object> entry = new SimpleEntry<Type<?>, Object>(type, part);
			ret.add(entry);
		}
		
		// Return
		return ret;
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public String getTypeName()
	{
		// Create
		List<String> parts = new MassiveList<String>();
		
		// Fill
		for (Type<?> type : this.getTypes())
		{
			parts.add(type.getTypeName());
		}
		
		// Return
		return Txt.implode(parts, " ");
	}
	
	@Override
	public String getVisualInner(T value, CommandSender sender)
	{
		// Create
		List<String> parts = new MassiveList<String>();
		
		// Fill
		for (Entry<Type<?>, Object> entry : this.splitEntries(value))
		{
			@SuppressWarnings("unchecked")
			Type<Object> type = (Type<Object>) entry.getKey();
			String part = type.getVisual(entry.getValue(), sender);
			parts.add(part);
		}
		
		// Return
		return Txt.implode(parts, " ");
	}

	@Override
	public String getNameInner(T value)
	{
		// Create
		List<String> parts = new MassiveList<String>();
		
		// Fill
		for (Entry<Type<?>, Object> entry : this.splitEntries(value))
		{
			@SuppressWarnings("unchecked")
			Type<Object> type = (Type<Object>) entry.getKey();
			String part = type.getName(entry.getValue());
			parts.add(part);
		}
		
		// Return
		return Txt.implode(parts, " ");
	}

	@Override
	public String getIdInner(T value)
	{
		// Create
		List<String> parts = new MassiveList<String>();
		
		// Fill
		for (Entry<Type<?>, Object> entry : this.splitEntries(value))
		{
			@SuppressWarnings("unchecked")
			Type<Object> type = (Type<Object>) entry.getKey();
			String part = type.getId(entry.getValue());
			parts.add(part);
		}
		
		// Return
		return Txt.implode(parts, " ");
	}

	@Override
	public T read(String arg, CommandSender sender) throws MassiveException
	{
		List<Object> parts = this.readParts(arg, sender);
		return this.combine(parts);
	}
	
	public List<Object> readParts(String arg, CommandSender sender) throws MassiveException
	{
		// Create
		List<Object> ret = new MassiveList<Object>();
		
		// Fill
		List<String> argParts = Arrays.asList(arg.split("[, ]+"));
		
		if (argParts.size() > this.getTypes().size())
		{
			throw new MassiveException().addMsg("<b>Too many parts!");
		}
		
		for (int i = 0; i < argParts.size(); i++)
		{
			String argPart = argParts.get(i);
			Type<?> type = this.getTypes().get(i);
			
			Object part = type.read(argPart, sender);
			
			ret.add(part);
		}
		
		// Return
		return ret;
	}
	
	//TODO: How to do this?
	@Override
	public Collection<String> getTabList(CommandSender sender, String arg)
	{
		return null; // ???
	}
	
	//TODO: How to do this?
	@Override
	public boolean allowSpaceAfterTab()
	{
		// ???
		return false;
	}

}
