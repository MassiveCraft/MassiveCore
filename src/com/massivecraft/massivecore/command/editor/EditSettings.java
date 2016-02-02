package com.massivecraft.massivecore.command.editor;

import java.util.Set;

import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;

import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.type.Type;
import com.massivecraft.massivecore.command.type.sender.TypeSender;
import com.massivecraft.massivecore.util.MUtil;
import com.massivecraft.massivecore.util.Txt;

public class EditSettings<O>
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	// This is the type of the object we are editing.
	protected final Type<O> objectType;
	public Type<O> getObjectType() { return this.objectType; }
	
	// This property is used to get the object we are editing from the sender.
	protected Property<CommandSender, O> usedProperty;
	public Property<CommandSender, O> getUsedProperty() { return this.usedProperty; }
	public void setUsedProperty(Property<CommandSender, O> usedProperty) { this.usedProperty = usedProperty; }
	
	// The Internal EditSettings<CommandSender> for setting the used.
	protected EditSettings<CommandSender> usedSettings = null;
	public EditSettings<CommandSender> getUsedSettings()
	{
		if (this.usedSettings == null)
		{
			this.usedSettings = this.createUsedSettings();			
		}
		return this.usedSettings;
	}
	protected EditSettings<CommandSender> createUsedSettings()
	{
		final EditSettings<O> main = this;
		return new EditSettings<CommandSender>(TypeSender.get(), new PropertyThis<CommandSender>(TypeSender.get())) {
			@Override
			public Permission getPropertyPermission(Property<CommandSender,?> property)
			{
				return main.getUsedPermission();
			}
		};
	}
	
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	public EditSettings(Type<O> objectType, Property<CommandSender, O> objectProperty)
	{
		this.objectType = objectType;
		this.usedProperty = objectProperty;
	}
	
	public EditSettings(Type<O> objectType)
	{
		this(objectType, null);
	}
	
	// -------------------------------------------- //
	// OBJECT
	// -------------------------------------------- //
	
	public O getUsed(CommandSender sender)
	{
		return this.getUsedProperty().getValue(sender);
	}
	
	public void setUsed(CommandSender sender, O used)
	{
		this.getUsedProperty().setValue(sender, used);
	}
	
	// -------------------------------------------- //
	// USED COMMAND
	// -------------------------------------------- //
	
	public CommandEditUsed<O> createCommandEditUsed()
	{
		return new CommandEditUsed<O>(this);
	}
	
	// -------------------------------------------- //
	// PERMISSONS
	// -------------------------------------------- //
	
	public Permission getPropertyPermission(Property<O, ?> property)
	{
		return null;
	}
	
	public Permission getUsedPermission()
	{
		return null;
	}
	
	// -------------------------------------------- //
	// TYPE READ UTILITY
	// -------------------------------------------- //
	
	// No nice constructors for TreeSet :(
	public static final Set<String> ALIASES_USED = MUtil.treeset("used", "selected", "chosen");
	
	public O getUsedOrCommandException(String arg, CommandSender sender) throws MassiveException
	{
		if (arg == null)
		{
			O ret = this.getUsed(sender);
			if (ret != null) return ret;
			String noun = this.getObjectType().getTypeName();
			String aan = Txt.aan(noun);
			throw new MassiveException().addMsg("<b>You must select %s %s for use to skip the optional argument.", aan, noun);
		}
		if (ALIASES_USED.contains(arg))
		{
			O ret = this.getUsed(sender);
			if (ret == null) throw new MassiveException().addMsg("<b>You have no selected %s.", this.getObjectType().getTypeName() );
			return ret;
		}
		
		return null;
	}
	
}
