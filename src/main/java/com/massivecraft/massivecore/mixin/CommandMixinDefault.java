package com.massivecraft.massivecore.mixin;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import com.massivecraft.massivecore.util.IdUtil;

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
	public boolean dispatchCommand(Object presentObject, Object senderObject, String commandLine)
	{
		// Additional enforced presence
		CommandSender present = IdUtil.getSender(presentObject);
		if (present == null) return false;
		
		// We must then of course have the presence of the sender
		CommandSender sender = IdUtil.getSender(senderObject);
		if (sender == null) return false;
		
		// Great! Let's do it!
		Bukkit.getServer().dispatchCommand(sender, commandLine);
		return true;
	}

}