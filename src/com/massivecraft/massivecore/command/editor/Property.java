package com.massivecraft.massivecore.command.editor;

import com.massivecraft.massivecore.Named;
import com.massivecraft.massivecore.collections.MassiveList;
import com.massivecraft.massivecore.command.requirement.Requirement;
import com.massivecraft.massivecore.command.type.Type;
import com.massivecraft.massivecore.mson.Mson;
import com.massivecraft.massivecore.store.Entity;
import com.massivecraft.massivecore.util.MUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.ListIterator;
import java.util.Map.Entry;

public abstract class Property<O, V> implements Named
{
	// -------------------------------------------- //
	// CONSTANTS
	// -------------------------------------------- //
	
	public static final String SHOW_INDENT = "  "; // Two spaces
	
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
	
	protected boolean visible = true;
	public boolean isVisible() { return this.visible; }
	public void setVisible(boolean visible) { this.visible = visible; }
	
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
	
	protected List<String> names = new MassiveList<>();
	public List<String> getNames() { return this.names; }
	@Override public String getName() { return this.getNames().isEmpty() ? null : this.getNames().get(0); }
	public void setName(String name) { this.names = new MassiveList<>(name); }
	public void setNames(String... names) { this.names = new MassiveList<>(names); }
	
	// -------------------------------------------- //
	// REQUIREMENTS
	// -------------------------------------------- //
	
	protected List<Requirement> requirements = new ArrayList<>();
	
	public List<Requirement> getRequirements() { return this.requirements; }
	public void setRequirements(List<Requirement> requirements) { this.requirements = requirements; }
	public void addRequirements(Collection<Requirement> requirements) { this.requirements.addAll(requirements); }
	public void addRequirements(Requirement... requirements) { this.addRequirements(Arrays.asList(requirements)); }
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public Property(Type<O> objectType, Type<V> valueType, Collection<String> names)
	{
		this.objectType = objectType;
		this.valueType = valueType;
		this.names = new MassiveList<>(names);
	}
	
	public Property(Type<O> objectType, Type<V> valueType, String... names)
	{
		this(objectType, valueType, Arrays.asList(names));
	}
	
	// -------------------------------------------- //
	// ACCESS
	// -------------------------------------------- //
	
	public abstract V getRaw(O object);
	public abstract O setRaw(O object, V value);
	
	public V getValue(O object)
	{
		return this.getRaw(object);
	}
	
	public O setValue(CommandSender sender, O object, V value)
	{
		// Get Before
		V before = this.getRaw(object);
		
		// NoChange
		if (MUtil.equals(before, value)) return object;
		
		// Apply
		object = this.setRaw(object, value);
		
		// Mark Entity Changed
		Entity<?> entity = null;
		if (object instanceof Entity) entity = (Entity<?>)object;
		if (entity != null && entity.isLive()) entity.changed();
		
		// On Change
		this.onChange(sender, object, before, value);
		
		// Return Before
		return object;
	}
	
	// -------------------------------------------- //
	// ON CHANGE
	// -------------------------------------------- //
	
	public void onChange(CommandSender sender, O object, V before, V after)
	{
		
	}
	
	// -------------------------------------------- //
	// INHERITED
	// -------------------------------------------- //
	
	public Entry<O, V> getInheritedEntry(O object)
	{
		if (object == null) return new SimpleEntry<>(null, null);
		V value = this.getValue(object);
		return new SimpleEntry<>(object, value);
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
	
	public Mson getInheritedVisual(O object, O source, V value, CommandSender sender)
	{
		Mson mson = this.getValueType().getVisualMson(value, sender);
		return Mson.prepondfix(null, mson, this.getInheritanceSuffix(object, source));
	}
	public Mson getInheritedVisual(O object, CommandSender sender)
	{
		Entry<O, V> inherited = this.getInheritedEntry(object);
		O source = inherited.getKey();
		V value = inherited.getValue();
		return this.getInheritedVisual(object, source, value, sender);
	}
	
	public Mson getInheritanceSuffix(O object, O source)
	{
		Mson ret = null;
		if (source != null && ! source.equals(object))
		{
			ret = Mson.mson(
				"[",
				this.getObjectType().getVisualMson(source),
				"]"
			).color(ChatColor.GRAY);
		}
		return ret;
	}
	
	public Mson getInheritanceSuffix(O object)
	{
		return this.getInheritanceSuffix(object, this.getInheritedObject(object));
	}
	
	// -------------------------------------------- //
	// VISUAL
	// -------------------------------------------- //
	
	public Mson getDisplayNameMson()
	{
		return Mson.mson(this.getName()).color(ChatColor.AQUA);
	}
	
	public String getDisplayName()
	{
		return ChatColor.AQUA.toString() + this.getName();
	}
	
	public List<Mson> getShowLines(O object, CommandSender sender)
	{
		Mson prefix = Mson.mson(
			this.getDisplayNameMson(),
			Mson.mson(":").color(ChatColor.GRAY)
		);
		List<Mson> ret = Mson.prepondfix(prefix, this.getValueType().getShow(this.getInheritedValue(object), sender), this.getInheritanceSuffix(object));
		
		for (ListIterator<Mson> it = ret.listIterator(1); it.hasNext();)
		{
			Mson mson = it.next();
			it.set(mson.text(SHOW_INDENT + mson.getText()));
		}
		
		return ret;
	}
	
	public static <O> List<Mson> getShowLines(O object, CommandSender sender, Collection<? extends Property<O, ?>> properties)
	{
		// Create
		List<Mson> ret = new MassiveList<>();
		
		// Fill
		for (Property<O, ?> property : properties)
		{
			if ( ! property.isVisible()) continue;
			ret.addAll(property.getShowLines(object, sender));
		}
				
		// Return
		return ret;
	}
	
}