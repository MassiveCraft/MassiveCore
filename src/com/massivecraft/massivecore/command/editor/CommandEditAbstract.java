package com.massivecraft.massivecore.command.editor;

import java.util.List;
import java.util.Map.Entry;

import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;

import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.collections.MassiveList;
import com.massivecraft.massivecore.command.MassiveCommand;
import com.massivecraft.massivecore.command.requirement.RequirementEditorUse;
import com.massivecraft.massivecore.command.requirement.RequirementHasPerm;
import com.massivecraft.massivecore.command.type.Type;
import com.massivecraft.massivecore.event.EventMassiveCoreEditorEdit;
import com.massivecraft.massivecore.util.PermUtil;
import com.massivecraft.massivecore.util.Txt;

public class CommandEditAbstract<O, V> extends MassiveCommand
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	protected final EditSettings<O> settings;
	public EditSettings<O> getSettings() { return this.settings; }
	
	protected final Property<O, V> property;
	public Property<O, V> getProperty() { return this.property; }
	
	// Null is allowed and means unknown/either/neither.
	protected final Boolean write;
	public Boolean isWrite() { return this.write; }
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CommandEditAbstract(EditSettings<O> settings, Property<O, V> property, Boolean write)
	{
		// Fields
		this.settings = settings;
		this.property = property;
		this.write = write;
		
		// Aliases
		this.setAliases(this.getProperty().getNames());
		
		// Desc
		String descAction = property.isEditable() ? "edit " : "show ";
		this.setDesc(descAction + this.getProperty().getName());
		
		// Requirements
		this.addRequirements(RequirementEditorUse.get());
		
		if (this.isWrite() != null && this.isWrite())
		{
			Permission permission = this.getPropertyPermission();
			if (permission != null) this.addRequirements(RequirementHasPerm.get(permission.getName()));
		}
	}

	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform() throws MassiveException
	{
		if (this.isParent())
		{
			// If there is only one visible child, and it is a show command ....
			// Note: We use the visible children because HelpCommand is always present, but often invisible.
			List<MassiveCommand> children = this.getVisibleChildren(this.sender);
			if (children.size() == 1 && children.get(0) instanceof CommandEditShow)
			{
				// ... skip directly to it.
				CommandEditShow<?, ?> cmd = (CommandEditShow<?, ?>) children.get(0);
				List<MassiveCommand> chain = this.getChain();
				chain.add(this);
				cmd.execute(this.sender, this.args, chain);
			}
			else
			{
				super.perform();
			}
		}
		else
		{
			msg("<b>Not yet implemented.");
		}
	}
	
	// -------------------------------------------- //
	// ATTEMPT SET
	// -------------------------------------------- //
	
	public void attemptSet(V after)
	{
		// Editable
		if ( ! this.getProperty().isEditable())
		{
			msg("<b>The property <h>%s<b> is not editable.", this.getProperty().getDisplayName());
			return;
		}
		
		// Permission
		if ( ! PermUtil.has(sender, this.getPropertyPermission(), true)) return;
		
		// Inherited / Source / Before
		Entry<O, V> inherited = this.getInheritedEntry();
		O source = inherited.getKey();
		V before = inherited.getValue();
		
		// Event
		EventMassiveCoreEditorEdit<O, V> event = new EventMassiveCoreEditorEdit<O, V>(this, source, before, after);
		event.run();
		if (event.isCancelled()) return;
		after = event.getAfter();
		
		// Setup
		String descProperty = this.getProperty().getDisplayName();
		String descObject = this.getObjectVisual();
		String descValue = this.getInheritedVisual(source, before);
		
		// NoChange
		// We check, inform and cancel on equality.
		if (this.getValueType().equals(before, after))
		{
			msg("%s<silver> for %s<silver> already: %s", descProperty, descObject, descValue);
			return;
		}
		
		// Create messages
		List<String> messages = new MassiveList<>();

		// Before
		// We inform what the value was before.
		messages.add(Txt.parse("<k>Before: %s", descValue));

		// Apply
		// We set the new property value.
		this.getProperty().setValue(this.getObject(), after);

		// After
		// We inform what the value is after.
		descValue = this.getInheritedVisual();
		messages.add(Txt.parse("<k>After: %s", descValue));
		
		// Startup
		// We inform what property and object the edit is taking place on.
		// The visual might change after modification, so this should be added after we have made the change.
		descObject = this.getObjectVisual();
		messages.add(0, Txt.parse("%s<silver> for %s<silver> edited:", descProperty, descObject));
		
		message(messages);
	}
	
	// -------------------------------------------- //
	// SHORTCUTS > EDITORSETTINGS
	// -------------------------------------------- //
	
	public Type<O> getObjectType()
	{
		return this.getSettings().getObjectType();
	}
	
	public O getObject(CommandSender sender)
	{
		return this.getSettings().getUsed(sender);
	}
	
	public O getObject()
	{
		return this.getSettings().getUsed(sender);
	}
	
	public Permission getPropertyPermission()
	{
		return this.getSettings().getPropertyPermission(this.getProperty());
	}
	
	public Permission getUsedPermission()
	{
		return this.getSettings().getUsedPermission();
	}
	
	public String getObjectVisual()
	{
		return this.getObjectType().getVisual(this.getObject(), sender);
	}
	
	// -------------------------------------------- //
	// SHORTCUTS > PROPERTY
	// -------------------------------------------- //
	
	// Type
	public Type<V> getValueType()
	{
		return this.getProperty().getValueType();
	}
	
	// Name
	public String getPropertyName()
	{
		return this.getProperty().getName();
	}
	
	public List<String> getPropertyNames()
	{
		return this.getProperty().getNames();
	}
	
	// Access
	public V getValue()
	{
		return this.getProperty().getValue(this.getObject());
	}
	
	public V setValue(V value)
	{
		return this.getProperty().setValue(this.getObject(), value);
	}
	
	public Entry<O, V> getInheritedEntry()
	{
		return this.getProperty().getInheritedEntry(this.getObject());
	}
	
	public O getInheritedObject()
	{
		return this.getProperty().getInheritedObject(this.getObject());
	}
	
	public V getInheritedValue()
	{
		return this.getProperty().getInheritedValue(this.getObject());
	}
	
	public String getInheritedVisual(O source, V value)
	{
		return this.getProperty().getInheritedVisual(this.getObject(), source, value, sender);
	}
	
	public String getInheritedVisual()
	{
		return this.getProperty().getInheritedVisual(this.getObject(), sender);
	}
	
	// -------------------------------------------- //
	// SHORTCUTS > PROPERTY > TYPE
	// -------------------------------------------- //
	
	// Only to be used with collection type properties.
	public Type<Object> getValueInnerType()
	{
		return this.getProperty().getValueType().getInnerType();
	}
	
	// -------------------------------------------- //
	// UTIL
	// -------------------------------------------- //
	
	// Simply picks the last word in the class name.
	public String createCommandAlias()
	{
		// Split at uppercase letters
		final String[] words = this.getClass().getSimpleName().split("(?=[A-Z])");
		String alias = words[words.length - 1];
		alias = alias.toLowerCase();
		return alias;
	}
	
	public void show(CommandSender sender)
	{
		String descProperty = this.getProperty().getDisplayName();
		String descObject = this.getObjectVisual();
		String descValue = this.getInheritedVisual();
		msg("%s<silver> for %s<silver>: %s", descProperty, descObject, descValue);
	}
	
	public void requireNullable() throws MassiveException
	{
		if (this.getProperty().isNullable()) return;
		throw new MassiveException().addMsg("<h>%s<b> can not be null.", this.getPropertyName());
	}
	
}
