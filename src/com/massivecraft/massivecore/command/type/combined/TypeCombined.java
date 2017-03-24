package com.massivecraft.massivecore.command.type.combined;

import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.collections.MassiveList;
import com.massivecraft.massivecore.command.editor.Property;
import com.massivecraft.massivecore.command.editor.PropertyReflection;
import com.massivecraft.massivecore.command.type.Type;
import com.massivecraft.massivecore.command.type.TypeAbstract;
import com.massivecraft.massivecore.mson.Mson;
import com.massivecraft.massivecore.util.Txt;
import org.bukkit.command.CommandSender;

import java.util.AbstractMap.SimpleEntry;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;
import java.util.regex.Pattern;

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
	
	// Visual Mson
	private boolean visualMsonNullIncluded = true;
	public boolean isVisualMsonNullIncluded() { return this.visualMsonNullIncluded; }
	public void setVisualMsonNullIncluded(boolean visualMsonNullIncluded) { this.visualMsonNullIncluded = visualMsonNullIncluded; }
	
	private Mson visualMsonSeparator = Mson.SPACE;
	public Mson getVisualMsonSeparator() { return this.visualMsonSeparator; }
	public void setVisualMsonSeparator(Mson visualMsonSeparator) { this.visualMsonSeparator = visualMsonSeparator; }
	
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
	
	public TypeCombined(Class<?> clazz, Type<?>... innerTypes)
	{
		super(clazz);
		this.setInnerTypes(innerTypes);
		this.setSeparators(SEPARATORS_DEFAULT);
	}
	
	public TypeCombined(Class<T> clazz)
	{
		super(clazz);
		this.setInnerProperties(PropertyReflection.getAll(clazz, this));
		
		List<Type<?>> innerTypes = new MassiveList<>();
		for (Property<T, ?> property : this.getInnerProperties())
		{
			innerTypes.add(property.getValueType());
		}
		this.setInnerTypes(innerTypes);
		
		this.setSeparators(SEPARATORS_DEFAULT);
	}
	
	// -------------------------------------------- //
	// CORE
	// -------------------------------------------- //
	
	public T combine(List<Object> parts)
	{
		if ( ! this.hasInnerProperties()) throw new IllegalStateException("TypeCombined#combine must be implemented.");
		
		T ret = this.createNewInstance();
		if (ret == null) throw new IllegalStateException("Type#createNewInstance must be implemented.");
		
		int i = 0;
		for (Object part : parts)
		{
			Property<T, Object> property = this.getInnerProperty(i);
			property.setRaw(ret, part);
			
			i++;
		}
		
		return ret;
	}
	
	public List<Object> split(T value)
	{
		if ( ! this.hasInnerProperties()) throw new IllegalStateException("TypeCombined#split must be implemented.");
		
		List<Object> parts = new MassiveList<>();
		
		for (Property<T, ?> property : this.getInnerProperties())
		{
			parts.add(property.getValue(value));
		}
		
		return parts;
	}
	
	// -------------------------------------------- //
	// SPLIT ENTRIES
	// -------------------------------------------- //
	
	public List<Entry<Type<?>, Object>> splitEntries(T value)
	{
		// Create
		List<Entry<Type<?>, Object>> ret = new MassiveList<>();
		
		// Fill
		List<?> parts = this.split(value);
		if (parts.size() > this.getInnerTypes().size()) throw new RuntimeException("Too many parts!");
		for (int i = 0; i < parts.size(); i++)
		{
			Type<?> type = this.getInnerType(i);
			Object part = parts.get(i);
			SimpleEntry<Type<?>, Object> entry = new SimpleEntry<Type<?>, Object>(type, part);
			ret.add(entry);
		}
		
		// Return
		return ret;
	}
	
	// -------------------------------------------- //
	// SPLIT ENTRIES USER
	// -------------------------------------------- //
	// This entire section was made to handle the problem that the visual should contain what is nulled in user order.
	// Just because we don't want it when we read arguments in commands does not mean we can omit it from the visual.
	// The better and abstracted solution (that I skipped) would be to have multiple custom orders.
	// One for command parameters.
	// One for visuals.
	// TODO: Implement such an abstraction.
	// TODO: The current hack assumes we want all in the visuals. That may not be true.
	
	public List<Entry<Type<?>, Object>> splitEntriesUser(T value)
	{
		// Create
		List<Entry<Type<?>, Object>> ret = new MassiveList<>();
		
		// Fill
		List<Entry<Type<?>, Object>> tech = this.splitEntries(value);
		for (int i : this.getUserOrderAugmented())
		{
			ret.add(tech.get(i));
		}
		
		// Return
		return ret;
	}
	
	public List<Integer> getUserOrderAugmented()
	{
		// Create
		List<Integer> ret = new MassiveList<>(this.getUserOrder());
		
		// Fill
		for (int indexTech = 0; indexTech < this.getInnerTypes().size(); indexTech++)
		{
			if (ret.contains(indexTech)) continue;
			addSorted(ret, indexTech);
		}
		
		// Return
		return ret;
	}
	
	private static void addSorted(List<Integer> list, Integer element)
	{
		for (int index = 0; index < list.size(); index++)
		{
			Integer current = list.get(index);
			Integer next = (index + 1 < list.size() ? list.get(index + 1) : null);
			if ((element <= current) && (next == null || element >= next))
			{
				list.add(index, element);
				return;
			}
		}
		list.add(element);
	}
	
	// -------------------------------------------- //
	// META
	// -------------------------------------------- //
	
	@Override
	public String getName()
	{
		// Create
		List<String> parts = new MassiveList<>();
		
		// Fill
		for (Type<?> type : this.getInnerTypes())
		{
			parts.add(type.getName());
		}
		
		// Return
		return Txt.implode(parts, this.getTypeNameSeparator());
	}
	
	// -------------------------------------------- //
	// WRITE SHOW
	// -------------------------------------------- //
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Mson> getShowInner(T value, CommandSender sender)
	{
		if (this.hasInnerProperties())
		{
			return super.getShowInner(value, sender);
		}
		// Create
		List<Mson> ret = new MassiveList<>();
		
		// Fill
		for (Entry<Type<?>, Object> entry : this.splitEntriesUser(value))
		{
			Type<Object> type = (Type<Object>) entry.getKey();
			ret.addAll(type.getShow(entry.getValue()));
		}
		
		
		// Return
		return ret;
	}
	
	// -------------------------------------------- //
	// WRITE VISUAL MSON
	// -------------------------------------------- //
	
	@SuppressWarnings("unchecked")
	@Override
	public Mson getVisualMsonInner(T value, CommandSender sender)
	{
		// Create
		List<Mson> parts = new MassiveList<>();
		
		// Fill
		for (Entry<Type<?>, Object> entry : this.splitEntriesUser(value))
		{
			Type<Object> type = (Type<Object>) entry.getKey();
			Mson part = type.getVisualMson(entry.getValue(), sender);
			if ( ! this.isVisualMsonNullIncluded() && part == null) continue;
			parts.add(part);
		}
		
		// Return
		return Mson.implode(parts, this.getVisualMsonSeparator());
	}

	// -------------------------------------------- //
	// WRITE VISUAL
	// -------------------------------------------- //
	
	@SuppressWarnings("unchecked")
	@Override
	public String getVisualInner(T value, CommandSender sender)
	{
		// Create
		List<String> parts = new MassiveList<>();
		
		// Fill
		for (Entry<Type<?>, Object> entry : this.splitEntriesUser(value))
		{
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
		List<String> parts = new MassiveList<>();
		
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
		List<String> parts = new MassiveList<>();
		
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
		List<Object> ret = new MassiveList<>();
		
		// Fill
		List<String> innerArgs = this.getArgs(arg);
		
		if (innerArgs.size() > this.getInnerTypes().size()) throw new MassiveException().addMsg("<b>Too many arguments.");
		
		for (int i = 0; i < innerArgs.size(); i++)
		{
			String innerArg = innerArgs.get(i);
			Type<?> innerType = this.getInnerType(getIndexUser(i));
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
		List<String> ret = new MassiveList<>();
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
