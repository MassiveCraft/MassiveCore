package com.massivecraft.massivecore.command.editor;

import org.bukkit.command.CommandSender;

import com.massivecraft.massivecore.command.type.sender.TypeSender;

public abstract class PropertyUsed<V> extends Property<CommandSender, V>
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public PropertyUsed(EditSettings<V> settings)
	{
		super(TypeSender.get(), settings.getObjectType(), "used " + settings.getObjectType().getName());
	}

}
