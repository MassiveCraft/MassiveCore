package com.massivecraft.massivecore.command.type.combined;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;
import java.util.regex.Pattern;
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
	// CONSTANTS
	// -------------------------------------------- //
	
	public static final String SEPARATORS_DEFAULT = ", ";
	public static final String SEPARATORS_LENIENT = " .,:-#";
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	private Pattern separatorsPattern = null;
	public Pattern getSeparatorsPattern() { return this.separatorsPattern; }
	public void setSeparatorsPattern(Pattern separatorsPattern) { this.separatorsPattern = separatorsPattern; }
	private void buildSeparatorsPattern() { this.separatorsPattern = buildSeparatorsPattern(this.separators); }
	
	private String separators = null;
	public String getSeparators() { return this.separators; }
	public void setSeparators(String separators)
	{
		this.separators = separators;
		this.buildSeparatorsPattern();
	}
	
	private String typeNameSeparator = " ";
	public String getTypeNameSeparator() { return this.typeNameSeparator; }
	public void setTypeNameSeparator(String typeNameSeparator) { this.typeNameSeparator = typeNameSeparator; }
	
	// Visual
	private boolean visualNullIncluded = true;
	public boolean isVisualNullIncluded() { return this.visualNullIncluded; }
	public void setVisualNullIncluded(boolean visualNullIncluded) { this.visualNullIncluded = visualNullIncluded; }
	
	private String visualSeparator = " ";
	public String getVisualSeparator() { return this.visualSeparator; }
	public void setVisualSeparator(String visualSeparator) { this.visualSeparator = visualSeparator; }
	
	// Name
	private boolean nameNullIncluded = true;
	public boolean isNameNullIncluded() { return this.nameNullIncluded; }
	public void setNameNullIncluded(boolean nameNullIncluded) { this.nameNullIncluded = nameNullIncluded; }
	
	private String nameSeparator = " ";
	public String getNameSeparator() { return this.nameSeparator; }
	public void setNameSeparator(String nameSeparator) { this.nameSeparator = nameSeparator; }
	
	// Id
	private boolean idNullIncluded = true;
	public boolean isIdNullIncluded() { return this.idNullIncluded; }
	public void setIdNullIncluded(boolean idNullIncluded) { this.idNullIncluded = idNullIncluded; }
	
	private String idSeparator = " ";
	public String getIdSeparator() { return this.idSeparator; }
	public void setIdSeparator(String idSeparator) { this.idSeparator = idSeparator; }
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public TypeCombined(Type<?>... innerTypes)
	{
		this.setInnerTypes(innerTypes);
		this.setSeparators(SEPARATORS_DEFAULT);
	}
	
	// -------------------------------------------- //
	// ABSTRACT
	// -------------------------------------------- //
	
	public abstract T combine(List<Object> parts);
	
	public abstract List<Object> split(T value);
	
	// -------------------------------------------- //
	// METHODS
	// -------------------------------------------- //
	
	public List<Entry<Type<?>, Object>> splitEntries(T value)
	{
		// Create
		List<Entry<Type<?>, Object>> ret = new MassiveList<Entry<Type<?>, Object>>();
		
		// Fill
		List<?> parts = this.split(value);
		if (parts.size() > this.getInnerTypes().size()) throw new RuntimeException("Too many parts!");
		for (int i = 0; i < parts.size(); i++)
		{
			Type<?> type = this.getInnerTypes().get(i);
			Object part = parts.get(i);
			SimpleEntry<Type<?>, Object> entry = new SimpleEntry<Type<?>, Object>(type, part);
			ret.add(entry);
		}
		
		// Return
		return ret;
	}
	
	// -------------------------------------------- //
	// META
	// -------------------------------------------- //
	
	@Override
	public String getTypeName()
	{
		// Create
		List<String> parts = new MassiveList<String>();
		
		// Fill
		for (Type<?> type : this.getInnerTypes())
		{
			parts.add(type.getTypeName());
		}
		
		// Return
		return Txt.implode(parts, this.getTypeNameSeparator());
	}
	
	// -------------------------------------------- //
	// WRITE VISUAL
	// -------------------------------------------- //
	
	@SuppressWarnings("unchecked")
	@Override
	public String getVisualInner(T value, CommandSender sender)
	{
		// Create
		List<String> parts = new MassiveList<String>();
		
		// Fill
		List<Entry<Type<?>, Object>> entries = this.splitEntries(value);
		for (int i = 0; i < entries.size(); i++)
		{
			Entry<Type<?>, Object> entry = entries.get(this.getIndexTech(i));
			Type<Object> type = (Type<Object>) entry.getKey();
			String part = type.getVisual(entry.getValue(), sender);
			if ( ! this.isVisualNullIncluded() && part == null) continue;
			parts.add(part);
		}
		
		// Return
		return Txt.implode(parts, this.getVisualSeparator());
	}

	// -------------------------------------------- //
	// WRITE NAME
	// -------------------------------------------- //
	
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
			if ( ! this.isNameNullIncluded() && part == null) continue;
			parts.add(part);
		}
		
		// Return
		return Txt.implode(parts, this.getNameSeparator());
	}

	// -------------------------------------------- //
	// WRITE ID
	// -------------------------------------------- //
	
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
			if ( ! this.isIdNullIncluded() && part == null) continue;
			parts.add(part);
		}
		
		// Return
		return Txt.implode(parts, this.getIdSeparator());
	}

	// -------------------------------------------- //
	// READ
	// -------------------------------------------- //
	
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
		List<String> innerArgs = this.getArgs(arg);
		
		if (innerArgs.size() > this.getInnerTypes().size()) throw new MassiveException().addMsg("<b>Too many arguments.");
		
		for (int i = 0; i < innerArgs.size(); i++)
		{
			String innerArg = innerArgs.get(i);
			Type<?> innerType = this.getInnerTypes().get(getIndexUser(i));
			Object part = innerType.read(innerArg, sender);
			ret.add(part);
		}
		
		// Return
		return ret;
	}
	
	// -------------------------------------------- //
	// TAB LIST
	// -------------------------------------------- //
	
	@Override
	public Collection<String> getTabList(CommandSender sender, String arg)
	{
		Type<?> innerType = this.getLastType(arg);
		if (innerType == null) return Collections.emptyList();
		String innerArg = this.getLastArg(arg);
		String prefix = arg.substring(0, arg.length() - innerArg.length());
		List<String> strings = innerType.getTabListFiltered(sender, innerArg);
		List<String> ret = new MassiveList<String>();
		for (String string : strings)
		{
			ret.add(prefix + string);
		}
		return ret;
	}
	
	@Override
	public boolean allowSpaceAfterTab()
	{
		return true;
	}
	
	// -------------------------------------------- //
	// ARGS
	// -------------------------------------------- //
	
	public List<String> getArgs(String string)
	{
		return Arrays.asList(this.getSeparatorsPattern().split(string, -1));
	}
	
	public String getLastArg(String string)
	{
		List<String> args = this.getArgs(string);
		if (args.isEmpty()) return null;
		return args.get(args.size() - 1);
	}
	
	public Type<?> getLastType(String string)
	{
		List<String> args = this.getArgs(string);
		if (args.isEmpty()) return null;
		if (args.size() > this.getInnerTypes().size()) return null;
		return this.getInnerType(getIndexTech(args.size() - 1));
	}
	
	// -------------------------------------------- //
	// UTIL
	// -------------------------------------------- //
	
	public static Pattern buildSeparatorsPattern(String separators)
	{
		StringBuilder regex = new StringBuilder();
		regex.append("[");
		for (char c : separators.toCharArray())
		{
			regex.append(Pattern.quote(String.valueOf(c)));
		}
		regex.append("]+");
		return Pattern.compile(regex.toString());
	}

}
