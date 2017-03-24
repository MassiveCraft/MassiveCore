package com.massivecraft.massivecore.command.editor;

import org.bukkit.inventory.ItemStack;

import java.util.List;

public class CommandEditItemStacks<O> extends CommandEditAbstract<O, List<ItemStack>>
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CommandEditItemStacks(EditSettings<O> settings, Property<O, List<ItemStack>> property)
	{
		// Super
		super(settings, property, true);
		
		// Children
		this.addChild(new CommandEditShow<O, List<ItemStack>>(settings, property));
		
		if (property.isNullable())
		{
			this.addChild(new CommandEditCreate<O, List<ItemStack>>(settings, property));
			this.addChild(new CommandEditDelete<O, List<ItemStack>>(settings, property));
		}
		
		if (property.isEditable())
		{
			this.addChild(new CommandEditItemStacksOpen<O>(settings, property));	
		}
	}
	
}
