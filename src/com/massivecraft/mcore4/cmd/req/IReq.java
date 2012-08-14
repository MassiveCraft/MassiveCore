package com.massivecraft.mcore4.cmd.req;

import org.bukkit.command.CommandSender;

import com.massivecraft.mcore4.cmd.MCommand;

public interface IReq
{
	// This just tests wether the requirement is met or not.
	public boolean test(CommandSender sender, MCommand command);
	
	// This just composes the error message and does NOT test the requirement at all.
	public String createErrorMessage(CommandSender sender, MCommand command);
}
