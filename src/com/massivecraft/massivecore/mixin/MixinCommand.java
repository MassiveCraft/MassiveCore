package com.massivecraft.massivecore.mixin;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import com.massivecraft.massivecore.util.IdUtil;

public class MixinCommand extends MixinAbstract
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static MixinCommand d = new MixinCommand();
	private static MixinCommand i = d;
	public static MixinCommand get() { return i; }
	public static void set(MixinCommand i) { MixinCommand.i = i; }
	
	// -------------------------------------------- //
	// METHODS
	// -------------------------------------------- //
	
	public boolean dispatchCommand(Object senderObject, String commandLine)
	{
		return this.dispatchCommand(senderObject, senderObject, commandLine);
	}
	
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
