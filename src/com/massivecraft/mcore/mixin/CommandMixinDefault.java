package com.massivecraft.mcore.mixin;

import org.bukkit.command.CommandSender;

import com.massivecraft.mcore.util.SenderUtil;

public class CommandMixinDefault extends CommandMixinAbstract
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static CommandMixinDefault i = new CommandMixinDefault();
	public static CommandMixinDefault get() { return i; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public boolean dispatchCommand(String sender, String commandLine)
	{
		CommandSender commandSender = SenderUtil.getSender(sender);
		if (commandSender == null) return false;
		return this.dispatchCommand(sender, commandLine);
	}

}