package com.massivecraft.massivecore.command.requirement;

import org.bukkit.command.CommandSender;

import com.massivecraft.massivecore.command.MassiveCommand;
import com.massivecraft.massivecore.predicate.Predicate;

public interface Requirement extends Predicate<CommandSender>
{
	public boolean apply(CommandSender sender, MassiveCommand command);
	
	// This just composes the error message and does NOT test the requirement at all.
	
	public String createErrorMessage(CommandSender sender);
	public String createErrorMessage(CommandSender sender, MassiveCommand command);
}
