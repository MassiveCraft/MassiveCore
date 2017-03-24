package com.massivecraft.massivecore.command.requirement;

import com.massivecraft.massivecore.command.MassiveCommand;
import com.massivecraft.massivecore.predicate.Predicate;
import org.bukkit.command.CommandSender;

public interface Requirement extends Predicate<CommandSender>
{
	boolean apply(CommandSender sender, MassiveCommand command);
	
	// This just composes the error message and does NOT test the requirement at all.
	
	String createErrorMessage(CommandSender sender);
	String createErrorMessage(CommandSender sender, MassiveCommand command);
}
