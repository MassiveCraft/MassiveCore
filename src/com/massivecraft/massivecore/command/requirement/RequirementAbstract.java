package com.massivecraft.massivecore.command.requirement;

import java.io.Serializable;

import org.bukkit.command.CommandSender;

import com.massivecraft.massivecore.command.MassiveCommand;

public abstract class RequirementAbstract implements Requirement, Serializable
{
	private static final long serialVersionUID = 1L;

	@Override
	public boolean apply(CommandSender sender)
	{
		return this.apply(sender, null);
	}

	@Override
	public String createErrorMessage(CommandSender sender)
	{
		return this.createErrorMessage(sender, null);
	}
	
	public static String getDesc(MassiveCommand command)
	{
		if (command == null) return "do that";
		return command.getDesc();
	}
	
}
