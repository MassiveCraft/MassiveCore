package com.massivecraft.massivecore.command.editor;

import java.util.List;
import java.util.Map.Entry;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.AbstractMap.SimpleEntry;
import java.util.Arrays;
import java.util.Collection;

import com.massivecraft.massivecore.Named;
import com.massivecraft.massivecore.collections.MassiveList;
import com.massivecraft.massivecore.command.type.Type;
import com.massivecraft.massivecore.store.Entity;
import com.massivecraft.massivecore.util.MUtil;
import com.massivecraft.massivecore.util.Txt;

public abstract class Property<O, V> implements Named
{
	// -------------------------------------------- //
	// TYPE
	// -------------------------------------------- //
	
	protected Type<O> objectType = null;
	public Type<O> getObjectType() { return this.objectType; }
	public void setObjectType(Type<O> objectType) { this.objectType = objectType; }
	
	protected Type<V> valueType = null;
	public Type<V> getValueType() { return this.valueType; }
	public void setValueType(Type<V> valueType) { this.valueType = valueType; }
	
	// -------------------------------------------- //
	// SETTINGS
	// -------------------------------------------- //
	
	protected boolean inheritable = true;
	public boolean isInheritable() { return this.inheritable; }
	public void setInheritable(boolean inheritable) { this.inheritable = inheritable; }
	
	protected boolean editable = true;
	public boolean isEditable() { return this.editable; }
	public void setEditable(boolean editable) { this.editable = editable; }
	
	protected boolean nullable = true;
	public boolean isNullable() { return this.nullable; }
	public void setNullable(boolean nullable) { this.nullable = nullable; }
	
	// -------------------------------------------- //
	// NAME
	// -------------------------------------------- //
	
	protected List<String> names = new MassiveList<String>();
	public List<String> getNames() { return this.names; }
	@Override public String getName() { return this.getNames().isEmpty() ? null : this.getNames().get(0); }
	public void setName(String name) { this.names = new MassiveList<String>(name); }
	public void setNames(String... names) { this.names = new MassiveList<String>(names); }
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public Property(Type<O> objectType, Type<V> valueType, Collection<String> names)
	{
		this.objectType = objectType;
		this.valueType = valueType;
		this.names = new MassiveList<String>(names);
	}
	
	public Property(Type<O> objectType, Type<V> valueType, String... names)
	{
		this(objectType, valueType, Arrays.asList(names));
	}
	
	// -------------------------------------------- //
	// ACCESS
	// -------------------------------------------- //
	
	public abstract V getRaw(O object);
	public abstract void setRaw(O object, V value);
	
	public V getValue(O object)
	{
		return this.getRaw(object);
	}
	
	public V setValue(O object, V value)
	{
		// Get Before
		V before = this.getRaw(object);
		
		// Get Live Entity
		Entity<?> entity = null;
		if (object instanceof Entity) entity = (Entity<?>)object;
		if (entity != null && ! entity.isLive()) entity = null;
		
		// NoChange
		if (entity != null && MUtil.equals(before, value)) return before;
		
		// Apply
		this.setRaw(object, value);
		
		// Mark Change
		if (entity != null) entity.changed();
		
		// On Change
		this.onChange(object, before, value);
		
		// Return Before
		return before;
	}
	
	// -------------------------------------------- //
	// ON CHANGE
	// -------------------------------------------- //
	
	public void onChange(O object, V before, V after)
	{
		
	}
	
	// -------------------------------------------- //
	// INHERITED
	// -------------------------------------------- //
	
	public Entry<O, V> getInheritedEntry(O object)
	{
		if (object == null) return new SimpleEntry<O, V>(null, null);
		V value = this.getValue(object);
		return new SimpleEntry<O, V>(object, value);
	}
	
	public O getInheritedObject(O object)
	{
		return this.getInheritedEntry(object).getKey();
	}
	
	public V getInheritedValue(O object)
	{
		return this.getInheritedEntry(object).getValue();
	}
	
	// -------------------------------------------- //
	// SHORTCUTS
	// -------------------------------------------- //
	
	public CommandEditAbstract<O, V> createEditCommand(EditSettings<O> settings)
	{
		return this.getValueType().createEditCommand(settings, this);
	}
	
	public String getInheritedVisual(O object, O source, V value, CommandSender sender)
	{
		String string = this.getValueType().getVisual(value, sender);
		/*if (string == null)
		{
			System.out.println("value type " + this.getValueType());
			System.out.println("value type name" + this.getValueType().getTypeName());
			System.out.println("object " + object);
			System.out.println("source " + source);
			System.out.println("value " + value);
			System.out.println("sender " + sender);
		}*/
		
		String suffix = null;
		if (source != null && ! source.equals(object))
		{
			suffix = Txt.parse("<silver>[%s<silver>]", this.getObjectType().getVisual(source));
		}
		
		return Txt.prepondfix(null, string, suffix);
	}
	
	public String getInheritedVisual(O object, CommandSender sender)
	{
		Entry<O, V> inherited = this.getInheritedEntry(object);
		O source = inherited.getKey();
		V value = inherited.getValue();
		return this.getInheritedVisual(object, source, value, sender);
	}
	
	// -------------------------------------------- //
	// VISUAL
	// -------------------------------------------- //
	
	public String getDisplayName()
	{
		return ChatColor.AQUA.toString() + this.getName();
	}
	
	public List<String> getShowLines(O object, CommandSender sender)
	{
		String ret = Txt.parse("<aqua>%s<silver>: <pink>%s", this.getDisplayName(), this.getInheritedVisual(object, sender));
		return new MassiveList<String>(Txt.PATTERN_NEWLINE.split(ret));
	}
	
	public static <O> List<String> getShowLines(O object, CommandSender sender, Collection<? extends Property<O, ?>> properties)
	{
		// Create
		List<String> ret = new MassiveList<String>();
		
		// Fill
		for (Property<O, ?> property : properties)
		{
			ret.addAll(property.getShowLines(object, sender));
		}
				
		// Return
		return ret;
	}
	
}