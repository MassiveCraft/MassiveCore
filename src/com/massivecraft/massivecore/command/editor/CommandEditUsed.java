package com.massivecraft.massivecore.command.editor;

import org.bukkit.command.CommandSender;

public class CommandEditUsed<O> extends CommandEditSimple<CommandSender, O>
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CommandEditUsed(EditSettings<O> settings)
	{
		// Super
		super(settings.getUsedSettings(), settings.getUsedProperty());
		
		// Aliases
		this.setAliases("used", "selected");
		
		// Desc
		this.setDesc("edit used " + this.getProperty().getValueType().getName());
	}
	
}
