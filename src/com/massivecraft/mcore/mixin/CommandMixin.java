package com.massivecraft.mcore.mixin;

import org.bukkit.command.CommandSender;

import com.massivecraft.mcore.store.SenderEntity;

public interface CommandMixin
{
	public boolean dispatchCommand(CommandSender sender, String commandLine);
	public boolean dispatchCommand(SenderEntity<?> sender, String commandLine);
	public boolean dispatchCommand(String senderId, String commandLine);
	public boolean dispatchCommand(String presentId, String senderId, String commandLine); // This one is non-abstract	
}
