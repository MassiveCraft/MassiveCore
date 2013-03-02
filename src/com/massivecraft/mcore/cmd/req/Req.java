package com.massivecraft.mcore.cmd.req;

import org.bukkit.command.CommandSender;

import com.massivecraft.mcore.Predictate;
import com.massivecraft.mcore.cmd.MCommand;

public interface Req extends Predictate<CommandSender>
{
	public boolean apply(CommandSender sender, MCommand command);
	
	// This just composes the error message and does NOT test the requirement at all.
	
	public String createErrorMessage(CommandSender sender);
	public String createErrorMessage(CommandSender sender, MCommand command);
}
