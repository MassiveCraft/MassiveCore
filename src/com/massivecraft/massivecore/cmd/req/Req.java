package com.massivecraft.massivecore.cmd.req;

import org.bukkit.command.CommandSender;

import com.massivecraft.massivecore.Predicate;
import com.massivecraft.massivecore.cmd.MassiveCommand;

public interface Req extends Predicate<CommandSender>
{
	public boolean apply(CommandSender sender, MassiveCommand command);
	
	// This just composes the error message and does NOT test the requirement at all.
	
	public String createErrorMessage(CommandSender sender);
	public String createErrorMessage(CommandSender sender, MassiveCommand command);
}
