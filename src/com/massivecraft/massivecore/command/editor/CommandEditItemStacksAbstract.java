package com.massivecraft.massivecore.command.editor;

import com.massivecraft.massivecore.command.requirement.RequirementEditorPropertyCreated;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public abstract class CommandEditItemStacksAbstract<O> extends CommandEditAbstract<O, List<ItemStack>>
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CommandEditItemStacksAbstract(EditSettings<O> settings, Property<O, List<ItemStack>> property)
	{
		// Super
		super(settings, property, true);
		
		// Aliases
		String alias = this.createCommandAlias();
		this.setAliases(alias);
		
		// Desc
		this.setDesc(alias + " " + this.getPropertyName());
		
		// Requirements
		this.addRequirements(RequirementEditorPropertyCreated.get(true));
	}
	
}
