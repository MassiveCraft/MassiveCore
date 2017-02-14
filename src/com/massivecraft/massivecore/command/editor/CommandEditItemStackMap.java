package com.massivecraft.massivecore.command.editor;

import java.util.Map;

import org.bukkit.inventory.ItemStack;

public class CommandEditItemStackMap<O> extends CommandEditAbstract<O, Map<Integer, ItemStack>>
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CommandEditItemStackMap(EditSettings<O> settings, Property<O, Map<Integer, ItemStack>> property)
	{
		// Super
		super(settings, property, true);
		
		// Children
		this.addChild(new CommandEditShow<>(settings, property));
		
		if (property.isNullable())
		{
			this.addChild(new CommandEditCreate<>(settings, property));
			this.addChild(new CommandEditDelete<>(settings, property));
		}
		
		if (property.isEditable())
		{
			this.addChild(new CommandEditItemStacksOpenMap<O>(settings, property));
		}
	}
	
}
