package com.massivecraft.mcore.mixin;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import com.massivecraft.mcore.store.SenderEntity;

public abstract class CommandMixinAbstract implements CommandMixin
{
	public boolean dispatchCommand(CommandSender sender, String commandLine)
	{
		return Bukkit.getServer().dispatchCommand(sender, commandLine);
	}
	
	@Override
	public boolean dispatchCommand(SenderEntity<?> sender, String commandLine)
	{
		return this.dispatchCommand(sender.getId(), commandLine);
	}
	
}
