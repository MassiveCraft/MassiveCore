package com.massivecraft.massivecore.command.type.collection;

import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.collections.MassiveList;
import com.massivecraft.massivecore.command.editor.CommandEditAbstract;
import com.massivecraft.massivecore.command.editor.CommandEditMap;
import com.massivecraft.massivecore.command.editor.EditSettings;
import com.massivecraft.massivecore.command.editor.Property;
import com.massivecraft.massivecore.command.type.Type;
import com.massivecraft.massivecore.command.type.TypeAbstract;
import com.massivecraft.massivecore.util.Txt;
import org.bukkit.command.CommandSender;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public abstract class TypeMapAbstract<C extends Map<K, V>, K, V> extends TypeAbstract<C>
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //

	private Type<V> mapValueType = null;
	public void setMapValueType(Type<V> mapValueType) { this.mapValueType = mapValueType; }
	public Type<V> getMapValueType() { return mapValueType; }

	public void setMapKeyType(Type<K> mapKeyType) { this.setInnerType(mapKeyType); }
	public Type<K> getMapKeyType() { return this.getInnerType(); }

	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //

	public TypeMapAbstract(Type<K> mapKeyType, Type<V> mapValueType)
	{
		this.setMapKeyType(mapKeyType);
		this.setMapValueType(mapValueType);
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //

	@Override
	public String getTypeName()
	{
		return "Map: " + this.getMapKeyType().getTypeName() + " -> " + this.getMapValueType().getTypeName();
	}

	@Override
	public String getVisualInner(C value, CommandSender sender)
	{
		// Empty
		if (value.isEmpty()) return EMPTY;

		List<String> parts = new MassiveList<String>();
		
		for (Entry<K,V> entry : value.entrySet())
		{
			K entryKey = entry.getKey();
			String visualKey = this.getMapKeyType().getVisual(entryKey, sender);

			V entryValue = entry.getValue();
			String visualValue = this.getMapValueType().getVisual(entryValue, sender);

			String part = Txt.parse("<key>%s: <value>%s", visualKey, visualValue);
			parts.add(part);
		}
		
		return Txt.implode(parts, "\n");
	}

	@Override
	public String getNameInner(C value)
	{
		// Empty
		if (value.isEmpty()) return EMPTY;

		// Create
		StringBuilder builder = new StringBuilder();
		boolean first = true;

		// Fill
		for (Entry<K,V> entry : value.entrySet())
		{
			if ( ! first) builder.append(", ");

			// Append key
			K entryKey = entry.getKey();
			builder.append(this.getInnerType().getName(entryKey));

			// Add Colon & Space
			builder.append(':');
			builder.append(' ');

			// Append value
			V entryValue = entry.getValue();
			builder.append(this.getMapValueType().getName(entryValue));
			first = false;
		}

		// Return
		return builder.toString();
	}

	@Override
	public String getIdInner(C value)
	{
		// Empty
		if (value.isEmpty()) return EMPTY;
		
		// Create
		StringBuilder builder = new StringBuilder();
		boolean first = true;
		
		// Fill
		for (Entry<K,V> entry : value.entrySet())
		{
			if ( ! first) builder.append(", ");

			// Append key
			K entryKey = entry.getKey();
			builder.append(this.getInnerType().getId(entryKey));

			// Add Colon & Space
			builder.append(':');
			builder.append(' ');

			// Append value
			V entryValue = entry.getValue();
			builder.append(this.getMapValueType().getId(entryValue));
			first = false;
		}
		
		// Return
		return builder.toString();
	}
	
	@Override
	public C read(String arg, CommandSender sender) throws MassiveException
	{
		// Create
		C ret = this.createNewInstance();

		// Prepare
		List<String> args = new MassiveList<>(Txt.PATTERN_WHITESPACE.split(arg));
		if (args.size() % 2 != 0)
		{
			throw new MassiveException().setMsg("<b>There must be an even amount of arguments for a map.");
		}

		// Fill
		for (Iterator<String> it = args.iterator(); it.hasNext(); )
		{
			// Get Key
			String keyString = it.next();
			K key = this.getMapKeyType().read(keyString, sender);

			// Get Value
			String valueString = it.next();
			V value = this.getMapValueType().read(valueString, sender);

			ret.put(key, value);
		}

		// Return
		return ret;
	}
	
	@Override
	public Collection<String> getTabList(CommandSender sender, String arg)
	{
		// Because we accept multiple arguments pairs of the same type.
		// The passed arg might be more than one. We only want the latest.
		List<String> args = TypeCollection.getArgs(arg);
		String lastArg = args.isEmpty() ? null : args.get(args.size() - 1);

		// Type
		Type<?> type = args.size() % 2 == 1 ? this.getMapKeyType() : this.getMapValueType();

		// Return
		return type.getTabList(sender, lastArg);
	}
	
	@Override
	public List<String> getTabListFiltered(CommandSender sender, String arg)
	{
		// Because we accept multiple arguments pairs of the same type.
		// The passed arg might be more than one. We only want the latest.
		List<String> args = TypeCollection.getArgs(arg);
		String lastArg = args.isEmpty() ? null : args.get(args.size() - 1);

		// Type
		Type<?> type = args.size() % 2 == 1 ? this.getMapKeyType() : this.getMapValueType();

		// Return
		return type.getTabListFiltered(sender, lastArg);
	}
	
	@Override
	public boolean allowSpaceAfterTab()
	{
		return this.getMapKeyType().allowSpaceAfterTab() && this.getMapValueType().allowSpaceAfterTab();
	}
	
	@Override
	public <O> CommandEditAbstract<O, C> createEditCommand(EditSettings<O> settings, Property<O, C> property)
	{
		return new CommandEditMap<O, C>(settings, property, this.getMapValueType());
	}
	
}
