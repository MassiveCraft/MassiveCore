package com.massivecraft.massivecore.command.editor;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.bukkit.command.CommandSender;

import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.collections.MassiveList;
import com.massivecraft.massivecore.command.requirement.Requirement;
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
		return new EditSettings<CommandSender>(TypeSender.get(), new PropertyThis<CommandSender>(TypeSender.get()));
	}
	
	// Requirements to edit the used object.
	private List<Requirement> usedRequirements = new MassiveList<>();
	public List<Requirement> getUsedRequirements() { return this.usedRequirements; }
	public void setUsedRequirements(List<Requirement> requirements) { this.usedRequirements = requirements; }
	public void addUsedRequirements(Collection<Requirement> requirements) { this.usedRequirements.addAll(requirements); }
	public void addUsedRequirements(Requirement... requirements) { this.addUsedRequirements(Arrays.asList(requirements)); }
	
	// Requirements to edit properties. Common stuff shared by all properties.
	private List<Requirement> propertyRequirements = new MassiveList<>();
	public List<Requirement> getPropertyRequirements() { return this.propertyRequirements; }
	public void setPropertyRequirements(List<Requirement> requirements) { this.propertyRequirements = requirements; }
	public void addPropertyRequirements(Collection<Requirement> requirements) { this.propertyRequirements.addAll(requirements); }
	public void addPropertyRequirements(Requirement... requirements) { this.addPropertyRequirements(Arrays.asList(requirements)); }
	
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
		this.getUsedProperty().setValue(sender, sender, used);
	}
	
	// -------------------------------------------- //
	// USED COMMAND
	// -------------------------------------------- //
	
	public CommandEditUsed<O> createCommandEditUsed()
	{
		return new CommandEditUsed<O>(this);
	}
	
	// -------------------------------------------- //
	// TYPE READ UTILITY
	// -------------------------------------------- //
	
	public static final Set<String> ALIASES_USED = MUtil.treeset("used", "selected", "chosen");
	
	public O getUsedOrCommandException(String arg, CommandSender sender) throws MassiveException
	{
		if (arg == null)
		{
			O ret = this.getUsed(sender);
			if (ret != null) return ret;
			String noun = this.getObjectType().getName();
			String aan = Txt.aan(noun);
			throw new MassiveException().addMsg("<b>You must select %s %s for use to skip the optional argument.", aan, noun);
		}
		if (ALIASES_USED.contains(arg))
		{
			O ret = this.getUsed(sender);
			if (ret == null) throw new MassiveException().addMsg("<b>You have no selected %s.", this.getObjectType().getName() );
			return ret;
		}
		
		return null;
	}
	
}
