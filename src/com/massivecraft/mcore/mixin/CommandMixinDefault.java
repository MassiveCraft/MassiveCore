package com.massivecraft.mcore.mixin;

import org.bukkit.Bukkit;
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
	public boolean dispatchCommand(String presentId, String senderId, String commandLine)
	{
		// Additional enforced presence
		CommandSender present = SenderUtil.getSender(presentId);
		if (present == null) return false;
		
		// We must then of course have the presence of the sender
		CommandSender sender = SenderUtil.getSender(senderId);
		if (sender == null) return false;
		
		// Great! Let's do it!
		Bukkit.getServer().dispatchCommand(sender, commandLine);
		return true;
	}

}